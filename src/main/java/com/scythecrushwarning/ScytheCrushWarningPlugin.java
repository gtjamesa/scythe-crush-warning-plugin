package com.scythecrushwarning;

import com.google.inject.Provides;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(name = "Scythe Crush Warning", description = "Show a warning if your Scythe is on crush", tags = {"scythe", "crush", "araxxor", "nightmare", "hueycoatl, cerberus"})
public class ScytheCrushWarningPlugin extends Plugin
{
	public static final String CONFIG_GROUP = "scythecrushwarning";
	private final int CRUSH_ATTACK_STYLE = 2;
	private Collection<Integer> SCYTHE_VARIATION_IDS;

	private final List<Integer> SCYTHE_ITEM_IDS = List.of(
		ItemID.SCYTHE_OF_VITUR,
		ItemID.HOLY_SCYTHE_OF_VITUR,
		ItemID.SANGUINE_SCYTHE_OF_VITUR
	);

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ScytheCrushWarningConfig config;

	@Inject
	private ScytheCrushWarningOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AllowedRegions allowedRegions;

	@Getter
	private boolean scytheOnCrush = false;

	@Getter
	private boolean inAllowedRegion = false;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);

		SCYTHE_VARIATION_IDS = getAllScytheVariations();

		clientThread.invoke(() -> {
			allowedRegions.buildAllowedRegions();
			reset();

//			log.debug("Scythe variations: {}", SCYTHE_VARIATION_IDS);

			if (client.getGameState() == GameState.LOGGED_IN)
			{
				checkWeapon();
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			reset();
			checkWeapon();
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == VarPlayer.ATTACK_STYLE || event.getVarbitId() == Varbits.EQUIPPED_WEAPON_TYPE)
		{
			checkWeapon();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		checkWeapon();
	}

	@Subscribe(priority = -1)
	public void onGameTick(GameTick event)
	{
		inAllowedRegion = allowedRegions.updateRegion();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(CONFIG_GROUP))
		{
			allowedRegions.buildAllowedRegions();
		}
	}

	private void checkWeapon()
	{
		final Integer currentWeaponId = getCurrentWeaponId();

		if (currentWeaponId == null || !SCYTHE_VARIATION_IDS.contains(currentWeaponId))
		{
			reset();
			return;
		}

		final int currentAttackStyle = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		scytheOnCrush = currentAttackStyle == CRUSH_ATTACK_STYLE;
	}

	private void reset()
	{
		scytheOnCrush = false;
		inAllowedRegion = false;
	}

	private Integer getCurrentWeaponId()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return null;
		}

		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return null;
		}

		PlayerComposition playerComposition = player.getPlayerComposition();
		if (playerComposition == null)
		{
			return null;
		}

		return playerComposition.getEquipmentId(KitType.WEAPON);
	}

	private Collection<Integer> getAllScytheVariations()
	{
		return SCYTHE_ITEM_IDS.stream()
			.map(ItemVariationMapping::getVariations)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	@Provides
	ScytheCrushWarningConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ScytheCrushWarningConfig.class);
	}
}
