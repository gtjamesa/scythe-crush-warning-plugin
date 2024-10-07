package com.scythecrushwarning;

public enum OverlayDisplay
{
	SIMPLE("Simple"),
	FANCY("Fancy");

	private final String name;

	OverlayDisplay(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
