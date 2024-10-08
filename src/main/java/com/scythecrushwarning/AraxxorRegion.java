package com.scythecrushwarning;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum AraxxorRegion
{
	ARAXXOR(14489),
	ARAXXOR_OUTSIDE_CAVE(14645),
	ARAXXOR_INSIDE_CAVE(14745),
	ARAXXOR_DARKMEYER_EAST(14644),
	ARAXXOR_SEPULCHRE(9565);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (AraxxorRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	AraxxorRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
