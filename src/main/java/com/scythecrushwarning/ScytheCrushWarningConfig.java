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

	@Alpha
	@ConfigItem(keyName = "overlayColor", name = "Overlay Color", description = "Overlay Background Color")
	default Color overlayColor()
	{
		return new Color(255, 0, 0, 150);
	}
}
