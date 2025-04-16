package com.scythecrushwarning.regions;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum RoyalTitansRegion
{
	ROYAL_TITANS(11669),
	ROYAL_TITANS_OUTSIDE_CAVE(11925);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (RoyalTitansRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	RoyalTitansRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
