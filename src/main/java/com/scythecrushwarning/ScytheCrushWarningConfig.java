package com.scythecrushwarning;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(ScytheCrushWarningPlugin.CONFIG_GROUP)
public interface ScytheCrushWarningConfig extends Config
{
	@ConfigSection(name = "Weapons", description = "Weapons to show warnings for", position = 2)
	String WEAPONS_SECTION = "weapons";

	@ConfigSection(name = "Bosses", description = "Bosses where crush is expected", position = 3)
	String BOSSES_SECTION = "bosses";

	@ConfigSection(name = "Custom regions", description = "Your own regions where crush is expected", position = 4)
	String CUSTOM_REGIONS_SECTION = "custom";

	@ConfigItem(keyName = "warnScythe", name = "Scythe of vitur", description = "Show warnings for the Scythe of vitur", section = WEAPONS_SECTION, position = 0)
	default boolean warnScythe()
	{
		return true;
	}

	@ConfigItem(keyName = "warnSoulreaperAxe", name = "Soulreaper axe", description = "Show warnings for the Soulreaper axe", section = WEAPONS_SECTION, position = 1)
	default boolean warnSoulreaperAxe()
	{
		return true;
	}

	@ConfigItem(keyName = "warnDharokGreataxe", name = "Dharok's greataxe", description = "Show warnings for Dharok's greataxe", section = WEAPONS_SECTION, position = 2)
	default boolean warnDharokGreataxe()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreAraxxor", name = "Ignore at Araxxor", description = "Do not show warning at Araxxor", section = BOSSES_SECTION)
	default boolean ignoreAraxxor()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreNightmare", name = "Ignore at Nightmare", description = "Do not show warning at Nightmare", section = BOSSES_SECTION)
	default boolean ignoreNightmare()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreHueycoatl", name = "Ignore at Hueycoatl", description = "Do not show warning at Hueycoatl", section = BOSSES_SECTION)
	default boolean ignoreHueycoatl()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreCerberus", name = "Ignore at Cerberus", description = "Do not show warning at Cerberus", section = BOSSES_SECTION)
	default boolean ignoreCerberus()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreRoyalTitans", name = "Ignore at Royal Titans", description = "Do not show warning at Royal Titans", section = BOSSES_SECTION)
	default boolean ignoreRoyalTitans()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreAmoxliatl", name = "Ignore at Amoxliatl", description = "Do not show warning at Amoxliatl", section = BOSSES_SECTION)
	default boolean ignoreAmoxliatl()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreMaggotKing", name = "Ignore at Maggot King", description = "Do not show warning at Maggot King", section = BOSSES_SECTION)
	default boolean ignoreMaggotKing()
	{
		return true;
	}

	@ConfigItem(keyName = "markRegionMenu", name = "Shift-click to mark", description = "Shift + right-click a tile to mark or unmark its region as a crush region", section = CUSTOM_REGIONS_SECTION, position = 0)
	default boolean markRegionMenu()
	{
		return false;
	}

	@ConfigItem(keyName = ScytheCrushWarningPlugin.CUSTOM_REGIONS_KEY, name = "Crush regions", description = "Region IDs where crush is expected, separated by commas. Shift + right-click a tile in game to add or remove regions", section = CUSTOM_REGIONS_SECTION, position = 1)
	default String customRegions()
	{
		return "";
	}

	@Range(min = 0, max = 2)
	@ConfigItem(keyName = "adjacentRegions", name = "Adjacent regions", description = "When marking or unmarking, also include this many regions in each direction. A region is 64x64 tiles. 0 = only the clicked region, 1 = a 3x3 block (9 regions), 2 = a 5x5 block (25 regions)", section = CUSTOM_REGIONS_SECTION, position = 2)
	default int adjacentRegions()
	{
		return 0;
	}

	@ConfigItem(keyName = "displaySlashWarning", name = "Display Slash Warning", description = "Warn On Slash In Crush Areas", position = 0)
	default boolean displaySlashWarning()
	{
		return true;
	}

	@Alpha
	@ConfigItem(keyName = "overlayColor", name = "Overlay Color", description = "Overlay Background Color", position = 1)
	default Color overlayColor()
	{
		return new Color(255, 0, 0, 150);
	}
}
