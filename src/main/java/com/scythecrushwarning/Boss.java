package com.scythecrushwarning;

import lombok.Getter;

@Getter
public enum Boss
{
	ARAXXOR("Araxxor"),
	NIGHTMARE("Nightmare");

	private final String name;

	Boss(String name)
	{
		this.name = name;
	}
}
