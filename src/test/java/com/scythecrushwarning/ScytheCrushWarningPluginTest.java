package com.scythecrushwarning;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ScytheCrushWarningPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ScytheCrushWarningPlugin.class);
		RuneLite.main(args);
	}
}