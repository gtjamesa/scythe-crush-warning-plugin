package com.scythecrushwarning;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("scythecrushwarning")
public interface ScytheCrushWarningConfig extends Config
{
	@ConfigItem(keyName = "overlayDisplay", name = "Overlay Display", description = "Overlay display style")
	default OverlayDisplay overlayDisplay()
	{
		return OverlayDisplay.FANCY;
	}

	@Alpha
	@ConfigItem(keyName = "overlayColor", name = "Overlay Color", description = "Overlay Background Color")
	default Color overlayColor()
	{
		return new Color(255, 0, 0, 150);
	}
}
