package com.example;

import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

import java.util.*;

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

	public SupplyLoader supplyLoader;

	@Override
	protected void startUp() throws Exception
	{
		partyChecker = new PartyChecker();
		supplyLoader = new SupplyLoader();
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
		if (partyChecker.checkRegion(client)) {
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e)
	{
		log.info("[TOA Supply Plugin] Widget : " + e.toString());

		Widget w = client.getWidget(777, 0);

		if (w != null) {
			log.info("[TOA Supply Plugin] Widget : " + w.getId());

			Map<String, Map<String, Integer>> trackedSupplies = supplyLoader.loadSupplies(w);
			new SupplyRequest().performRequest(client, this, trackedSupplies);
		}
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
