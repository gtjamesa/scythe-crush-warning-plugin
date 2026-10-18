package com.scythecrushwarning;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AllowedRegionsTest
{
	@Test
	public void testAdjacentRegions()
	{
		assertEquals(List.of(15515), AllowedRegions.getAdjacentRegions(15515, 0));

		assertEquals(List.of(15258, 15259, 15260, 15514, 15515, 15516, 15770, 15771, 15772),
			AllowedRegions.getAdjacentRegions(15515, 1));

		final List<Integer> radius2 = AllowedRegions.getAdjacentRegions(15515, 2);
		assertEquals(25, radius2.size());
		assertTrue(radius2.containsAll(List.of(15001, 15005, 16025, 16029)));
	}
}
