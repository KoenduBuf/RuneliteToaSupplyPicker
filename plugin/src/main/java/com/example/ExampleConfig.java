package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import static com.example.ExampleConfig.CONFIG_GROUP;

@ConfigGroup(CONFIG_GROUP)
public interface ExampleConfig extends Config
{
	public static String CONFIG_GROUP = "Toa Supplies";

	@ConfigItem(
		keyName = "minimalNectar",
		name = "Minimal Nectar",
		description = "The minimal amount of nectar you want"
	)
	default int minimalNectar()
	{
		return 4;
	}

	@ConfigItem(
			keyName = "minimalTears",
			name = "Minimal Tears",
			description = "The minimal amount of tears you want"
	)
	default int minimalTears()
	{
		return 4;
	}

	@ConfigItem(
			keyName = "excessPriority",
			name = "Excess Supplies Priority",
			description = "The amount of excess supplies you get. This value for each player will be added up." +
					"Then the amount of excess supplies you get will be `(yourPriority / totalPriority) * 100` %"
	)
	default int excessPriority()
	{
		return 100;
	}
}
