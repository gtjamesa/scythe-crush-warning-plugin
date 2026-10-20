package com.scythecrushwarning;

import com.google.inject.Provides;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.Tile;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
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
	description = "Show a warning if your Scythe is on crush", tags = {"scythe", "sra", "soulreaper", "dharok", "crush", "araxxor", "nightmare", "hueycoatl", "cerberus", "royal titans", "amoxliatl", "maggot king", "fractured archive", "tfa"}
)
public class ScytheCrushWarningPlugin extends Plugin
{
	public static final String CONFIG_GROUP = "scythecrushwarning";
	public static final String CUSTOM_REGIONS_KEY = "customRegions";
	private final int CRUSH_ATTACK_STYLE = 2;
	private final int scytheItemId = ItemID.SCYTHE_OF_VITUR;
	private final int sraItemId = ItemID.BETA_ITEM_1; // soulreaper axe (25484)
	private Collection<Integer> SCYTHE_VARIATION_IDS;
	private Collection<Integer> SRA_VARIATION_IDS;
	private Collection<Integer> DHAROK_VARIATION_IDS;

	private final List<Integer> SRA_ITEM_IDS = List.of(sraItemId);
	private final List<Integer> DHAROK_ITEM_IDS = List.of(ItemID.BARROWS_DHAROK_WEAPON); // variations include degraded
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
	private ConfigManager configManager;

	@Inject
	private ScytheCrushWarningOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AllowedRegions allowedRegions;

	@Getter
	private boolean weaponEquipped = false;

	@Getter
	private int currentAttackStyle = 0;

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
		DHAROK_VARIATION_IDS = getAllVariations(DHAROK_ITEM_IDS);

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
			// list is read on the client thread each GameTick
			clientThread.invoke(allowedRegions::buildAllowedRegions);
			clientThread.invoke(this::checkWeapon);
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final MenuAction menuAction = event.getMenuEntry().getType();
		if ((menuAction != MenuAction.WALK && menuAction != MenuAction.SET_HEADING)
			|| !client.isKeyPressed(KeyCode.KC_SHIFT) || !config.markRegionMenu())
		{
			return;
		}

		final WorldView wv = client.getWorldView(event.getMenuEntry().getWorldViewId());
		if (wv == null)
		{
			return;
		}

		final Tile selectedSceneTile = wv.getSelectedSceneTile();
		if (selectedSceneTile == null)
		{
			return;
		}

		final int regionId = WorldPoint.fromLocalInstance(client, selectedSceneTile.getLocalLocation()).getRegionID();
		final boolean marked = allowedRegions.getCustomRegions().contains(regionId);

		client.getMenu().createMenuEntry(-1)
			.setOption(marked ? "Unmark crush region" : "Mark crush region")
			.setTarget("(region: " + regionId + ")")
			.setType(MenuAction.RUNELITE)
			.onClick(e -> markRegion(regionId, !marked));
	}

	private void markRegion(int regionId, boolean mark)
	{
		final Set<Integer> regions = allowedRegions.getCustomRegions();
		final List<Integer> adjacentRegions = AllowedRegions.getAdjacentRegions(regionId, config.adjacentRegions());
		if (mark)
		{
			regions.addAll(adjacentRegions);
		}
		else
		{
			regions.removeAll(adjacentRegions);
		}

		// fires ConfigChanged, which rebuilds the crush regions
		configManager.setConfiguration(CONFIG_GROUP, CUSTOM_REGIONS_KEY, regions.stream()
			.map(String::valueOf)
			.collect(Collectors.joining(",")));
	}

	private void checkWeapon()
	{
		final Integer currentWeaponId = getCurrentWeaponId();
		final boolean containsScythe = config.warnScythe() && SCYTHE_VARIATION_IDS.contains(currentWeaponId);
		final boolean containsSra = config.warnSoulreaperAxe() && SRA_VARIATION_IDS.contains(currentWeaponId);
		final boolean containsDharok = config.warnDharokGreataxe() && DHAROK_VARIATION_IDS.contains(currentWeaponId);

		if (currentWeaponId == null || (!containsScythe && !containsSra && !containsDharok))
		{
			weaponEquipped = false;
			return;
		}

		weaponEquipped = true;
		currentAttackStyle = client.getVarpValue(VarPlayerID.COM_MODE);

		if (containsScythe)
		{
			equippedWeaponName = "Scythe";
		}
		else if (containsSra)
		{
			equippedWeaponName = "SRA";
		}
		else
		{
			equippedWeaponName = "Dharok's axe";
		}
	}

	private void reset()
	{
		weaponEquipped = false;
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
