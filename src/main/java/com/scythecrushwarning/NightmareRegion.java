package com.scythecrushwarning;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum NightmareRegion
{
	NIGHTMARE(15515),
	SLEPE_GRAVEYARD(14899),
	SLEPE_DOCK(14643),
	SANCTUARY_1(14999),
	SANCTUARY_2(15255),
	SANCTUARY_3(15257),
	SANCTUARY_NIGHTMARE(15256);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (NightmareRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	NightmareRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
