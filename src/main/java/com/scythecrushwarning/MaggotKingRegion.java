package com.scythecrushwarning;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum MaggotKingRegion
{
	MAGGOT_KING(11645),
	CASTLE_DRAKAN(12664),
	VAMPYRIUM(10106),
	SANGVESTI(10362),
	SANGVESTI_FOREST(10618);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (MaggotKingRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	MaggotKingRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
