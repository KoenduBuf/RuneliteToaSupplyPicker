package com.buffplugin;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "TOASupplyPlugin"
)
public class TOASupplyPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TOASupplyConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("[TOA Supply Plugin] Started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("[TOA Supply Plugin] Stopped!");
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e)
	{
		log.info("[TOA Supply Plugin] Widget : " + e.toString());

		Widget w = client.getWidget(777, 0);

		if (w != null) {
			String[] categories = new String[] {"Life", "Chaos", "Power"};
			Map<Integer, String> supplyMap = new HashMap<>();

			supplyMap.put(27315, "Nectar");
			supplyMap.put(27327, "Tears");
			supplyMap.put(27347, "Ambrosia");
			supplyMap.put(27323, "Silk");
			supplyMap.put(27335, "Scarab");
			supplyMap.put(27343, "Salts");
			supplyMap.put(27339, "Adrenaline");

			Set<String> categorySet = new HashSet<>(Arrays.asList(categories));

			Stack<Widget> widgetQueue = new Stack<>();
			widgetQueue.add(w);

			Map<String, Map<String, Integer>> supplyTracker = new HashMap<>();
			int quantity = 0;

			while (!widgetQueue.isEmpty()) {
				Widget currentWidget = widgetQueue.pop();
				Widget[] children = currentWidget.getChildren();

				if (children == null) {
					continue;
				}

				String key = null;
				Map<String, Integer> subMap = new HashMap<>();
				for (Widget child : children) {
					widgetQueue.push(child);

					if (categorySet.contains(child.getText())) {
						key = child.getText();
					}

					if (supplyMap.containsKey(child.getItemId())) {
						quantity += child.getItemQuantity();
						subMap.put(supplyMap.get(child.getItemId()), child.getItemQuantity());
					}
				}
				if (key != null) {
					supplyTracker.put(key, subMap);
				}
				log.info("[TOA Supply Plugin] Quantity : " + quantity);
			}

			log.info("[TOA Supply Plugin] Widget : " + w.getId());
		}




	}

	@Provides
	TOASupplyConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TOASupplyConfig.class);
	}
}
