package com.scythecrushwarning;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum HueycoatlRegion
{
	HUEYCOATL(5939),
	HUEYCOATL_EAST(6195);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (HueycoatlRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	HueycoatlRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
