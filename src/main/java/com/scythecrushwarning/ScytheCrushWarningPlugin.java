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
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
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
@PluginDescriptor(
	name = "Scythe Crush Warning",
	description = "Show a warning if your Scythe is on crush", tags = {"scythe", "sra", "soulreaper", "crush", "araxxor", "nightmare", "hueycoatl", "cerberus", "royal titans", "amoxliatl"}
)
public class ScytheCrushWarningPlugin extends Plugin
{
	public static final String CONFIG_GROUP = "scythecrushwarning";
	private final int CRUSH_ATTACK_STYLE = 2;
	private final int scytheItemId = ItemID.SCYTHE_OF_VITUR;
	private final int sraItemId = ItemID.BETA_ITEM_1; // soulreaper axe (25484)
	private Collection<Integer> SCYTHE_VARIATION_IDS;
	private Collection<Integer> SRA_VARIATION_IDS;

	private final List<Integer> SRA_ITEM_IDS = List.of(sraItemId);
	private final List<Integer> SCYTHE_ITEM_IDS = List.of(
		scytheItemId,
		ItemID.SCYTHE_OF_VITUR_OR, // holy
		ItemID.SCYTHE_OF_VITUR_BL // sanguine
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

	@Getter
	private String equippedWeaponName;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);

		SCYTHE_VARIATION_IDS = getAllVariations(SCYTHE_ITEM_IDS);
		SRA_VARIATION_IDS = getAllVariations(SRA_ITEM_IDS);

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
		if (event.getVarpId() == VarPlayerID.COM_MODE || event.getVarbitId() == VarbitID.COMBAT_WEAPON_CATEGORY)
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
		final boolean containsScythe = SCYTHE_VARIATION_IDS.contains(currentWeaponId);
		final boolean containsSra = SRA_VARIATION_IDS.contains(currentWeaponId);

		if (currentWeaponId == null || (!containsScythe && !containsSra))
		{
			reset();
			return;
		}

		final int currentAttackStyle = client.getVarpValue(VarPlayerID.COM_MODE);
		scytheOnCrush = currentAttackStyle == CRUSH_ATTACK_STYLE;

		if (containsScythe)
		{
			equippedWeaponName = "Scythe";
		}
		else
		{
			equippedWeaponName = "SRA";
		}
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

	private Collection<Integer> getAllVariations(List<Integer> itemIds)
	{
		return itemIds.stream()
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
