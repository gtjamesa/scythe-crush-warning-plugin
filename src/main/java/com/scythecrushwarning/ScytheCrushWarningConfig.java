package com.scythecrushwarning;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(ScytheCrushWarningPlugin.CONFIG_GROUP)
public interface ScytheCrushWarningConfig extends Config
{
	@ConfigItem(keyName = "ignoreAraxxor", name = "Ignore at Araxxor", description = "Do not show warning at Araxxor")
	default boolean ignoreAraxxor()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreNightmare", name = "Ignore at Nightmare", description = "Do not show warning at Nightmare")
	default boolean ignoreNightmare()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreHueycoatl", name = "Ignore at Hueycoatl", description = "Do not show warning at Hueycoatl")
	default boolean ignoreHueycoatl()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreCerberus", name = "Ignore at Cerberus", description = "Do not show warning at Cerberus")
	default boolean ignoreCerberus()
	{
		return true;
	}

	@ConfigItem(keyName = "ignoreRoyalTitans", name = "Ignore at Royal Titans", description = "Do not show warning at Royal Titans")
	default boolean ignoreRoyalTitans()
	{
		return true;
	}

	@Alpha
	@ConfigItem(keyName = "overlayColor", name = "Overlay Color", description = "Overlay Background Color")
	default Color overlayColor()
	{
		return new Color(255, 0, 0, 150);
	}
}
