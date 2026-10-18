package com.scythecrushwarning;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.util.Text;

class AllowedRegions
{
	@Inject
	private Client client;

	@Inject
	private ScytheCrushWarningConfig config;

	private ArrayList<Integer> allowedRegions = new ArrayList<>();

	@Getter
	@Setter
	private int lastRegionId = -1;

	public boolean isWithinBossRegion()
	{
		return isWithinBossRegion(getRegionId());
	}

	public boolean isWithinBossRegion(int regionId)
	{
		return allowedRegions.contains(regionId);
	}

	public void buildAllowedRegions()
	{
		allowedRegions.clear();

		if (config.ignoreAraxxor())
		{
			allowedRegions.addAll(AraxxorRegion.getAllRegions());
		}

		if (config.ignoreNightmare())
		{
			allowedRegions.addAll(NightmareRegion.getAllRegions());
		}

		if (config.ignoreHueycoatl())
		{
			allowedRegions.addAll(HueycoatlRegion.getAllRegions());
		}

		if (config.ignoreCerberus())
		{
			allowedRegions.addAll(CerberusRegion.getAllRegions());
		}

		if (config.ignoreRoyalTitans())
		{
			allowedRegions.addAll(RoyalTitansRegion.getAllRegions());
		}

		if (config.ignoreAmoxliatl())
		{
			allowedRegions.addAll(AmoxliatlRegion.getAllRegions());
		}

		if (config.ignoreMaggotKing())
		{
			allowedRegions.addAll(MaggotKingRegion.getAllRegions());
		}

		allowedRegions.addAll(getCustomRegions());
	}

	public Set<Integer> getCustomRegions()
	{
		Set<Integer> regions = new LinkedHashSet<>();
		for (String region : Text.fromCSV(config.customRegions()))
		{
			try
			{
				regions.add(Integer.parseInt(region));
			}
			catch (NumberFormatException e)
			{
				// skip entries that aren't region IDs
			}
		}
		return regions;
	}

	/**
	 * The region plus every region within radius in each direction, a square of (2 * radius + 1)^2 regions
	 */
	public static List<Integer> getAdjacentRegions(int regionId, int radius)
	{
		final int regionX = regionId >> 8;
		final int regionY = regionId & 0xFF;
		final List<Integer> regions = new ArrayList<>();
		for (int dx = -radius; dx <= radius; dx++)
		{
			for (int dy = -radius; dy <= radius; dy++)
			{
				regions.add((regionX + dx) << 8 | (regionY + dy));
			}
		}
		return regions;
	}

	public boolean regionChanged()
	{
		return lastRegionId != getRegionId();
	}

	/**
	 * Update the last region and check if the player is within a crush region
	 *
	 * @return boolean
	 */
	public boolean updateRegion()
	{
		lastRegionId = getRegionId();
		return isWithinBossRegion(lastRegionId);
	}

	public int getRegionId()
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return -1;
		}

		return WorldPoint.fromLocalInstance(client, player.getLocalLocation()).getRegionID();
	}
}
