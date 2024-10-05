package com.scythecrushwarning;

import com.google.inject.Provides;
import java.util.Collection;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(name = "Scythe Crush Warning", description = "Show a warning if your Scythe is on crush", tags = {"scythe", "crush", "araxxor", "nightmare"})
public class ScytheCrushWarningPlugin extends Plugin
{
	private final int CRUSH_ATTACK_STYLE = 2;
	private final Collection<Integer> SCYTHE_VARIATION_IDS = ItemVariationMapping.getVariations(ItemID.SCYTHE_OF_VITUR);

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

	@Getter
	private boolean scytheOnCrush = false;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);

		clientThread.invoke(() -> {
			reset();

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

	private void checkWeapon()
	{
		if (!SCYTHE_VARIATION_IDS.contains(getCurrentWeaponId()))
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
	}

	private int getCurrentWeaponId()
	{
		return client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON);
	}

	@Provides
	ScytheCrushWarningConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ScytheCrushWarningConfig.class);
	}
}
