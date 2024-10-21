package com.example;

import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	public ExampleConfig config;

	@Inject
	public OkHttpClient okHttpClient;

	@Inject
	public Gson gson;

	public PartyChecker partyChecker;

	@Override
	protected void startUp() throws Exception
	{
		partyChecker = new PartyChecker();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOADING)
		{
			int r = partyChecker.getCurrentRegionId(client);
			log.info("[TOA SUPPLY CHECKER] Loading finished: " + r);
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event) {
		partyChecker.update(client);
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event)
	{
//		if (!event.getGroup().equals(ExampleConfig.CONFIG_GROUP)) {
//			return;
//		}

		new SupplyRequest().performRequest(client, this);
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
