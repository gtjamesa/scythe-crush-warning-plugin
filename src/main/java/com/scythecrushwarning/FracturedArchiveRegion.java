package com.scythecrushwarning;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public enum FracturedArchiveRegion
{
	FRACTURED_ARCHIVE_1(16214),
	FRACTURED_ARCHIVE_2(16215),
	FRACTURED_ARCHIVE_3(16216),
	FRACTURED_ARCHIVE_4(16470),
	FRACTURED_ARCHIVE_5(16471),
	FRACTURED_ARCHIVE_6(16472),
	FRACTURED_ARCHIVE_7(16473),
	FRACTURED_ARCHIVE_8(16474),
	FRACTURED_ARCHIVE_9(16726),
	FRACTURED_ARCHIVE_10(16727),
	FRACTURED_ARCHIVE_11(16728);

	private final int region;

	private static final ArrayList<Integer> ALL_REGIONS = new ArrayList<>();

	static
	{
		for (FracturedArchiveRegion e : values())
		{
			ALL_REGIONS.add(e.region);
		}
	}

	FracturedArchiveRegion(int region)
	{
		this.region = region;
	}

	public static ArrayList<Integer> getAllRegions()
	{
		return ALL_REGIONS;
	}
}
