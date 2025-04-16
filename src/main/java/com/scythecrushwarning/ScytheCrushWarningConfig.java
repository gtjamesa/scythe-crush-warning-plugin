package com.scythecrushwarning;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(ScytheCrushWarningPlugin.CONFIG_GROUP)
public interface ScytheCrushWarningConfig extends Config
{
	@ConfigSection(
		name = "Overlay Options",
		description = "Customise the overlay",
		position = 0
	)
	String overlaySection = "overlaySection";

	@ConfigSection(
		name = "Ignore Monsters",
		description = "Customise the monsters to ignore",
		position = 1
	)
	String ignoreSection = "ignoreSection";

	@Alpha
	@ConfigItem(keyName = "overlayColor", name = "Overlay Color", description = "Overlay Background Color", section = overlaySection, position = 1)
	default Color overlayColor()
	{
		return new Color(255, 0, 0, 150);
	}

	@ConfigItem(keyName = "showInventoryOverlay", name = "Show inventory overlay", description = "Add a text label overlay when not in a boss region", section = overlaySection, position = 2)
	default boolean showInventoryOverlay()
	{
		return false;
	}

	@Alpha
	@ConfigItem(keyName = "inventoryOverlayColor", name = "Inventory text color", description = "Change the inventory text label color", section = overlaySection, position = 3)
	default Color inventoryOverlayColor()
	{
		return new Color(255, 0, 0, 255);
	}

	@ConfigItem(keyName = "ignoreAraxxor", name = "Ignore at Araxxor", description = "Do not show warning at Araxxor", section = ignoreSection)
	default boolean ignoreAraxxor()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreNightmare", name = "Ignore at Nightmare", description = "Do not show warning at Nightmare", section = ignoreSection)
	default boolean ignoreNightmare()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreHueycoatl", name = "Ignore at Hueycoatl", description = "Do not show warning at Hueycoatl", section = ignoreSection)
	default boolean ignoreHueycoatl()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreCerberus", name = "Ignore at Cerberus", description = "Do not show warning at Cerberus", section = ignoreSection)
	default boolean ignoreCerberus()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreRoyalTitans", name = "Ignore at Royal Titans", description = "Do not show warning at Royal Titans", section = ignoreSection)
	default boolean ignoreRoyalTitans()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreAmoxliatl", name = "Ignore at Amoxliatl", description = "Do not show warning at Amoxliatl", section = ignoreSection)
	default boolean ignoreAmoxliatl()
	{
		return true;
	}
}
