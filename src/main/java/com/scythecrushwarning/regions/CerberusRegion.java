package com.scythecrushwarning.regions;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum CerberusRegion
{
	CERBERUS(5139),
	CERBERUS_NORTH(5140),
	CERBERUS_EAST(5395),
	CERBERUS_WEST(4883);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (CerberusRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	CerberusRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
