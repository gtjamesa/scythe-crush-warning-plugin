package com.scythecrushwarning.regions;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum AmoxliatlRegion
{
	AMOXLIATL(5446),
	TWILIGHT_TEMPLE(6450),
	TWILIGHT_TEMPLE_TELEPORT(6706),
	INSIDE_TEMPLE(6550);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (AmoxliatlRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	AmoxliatlRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
