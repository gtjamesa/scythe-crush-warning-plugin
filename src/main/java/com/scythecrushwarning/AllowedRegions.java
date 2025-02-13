package com.scythecrushwarning;

import java.util.ArrayList;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

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
	}

	public boolean regionChanged()
	{
		return lastRegionId != getRegionId();
	}

	/**
	 * Update the last region and check if the player is within a boss region
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
