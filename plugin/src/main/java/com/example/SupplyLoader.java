package com.example;

import net.runelite.api.widgets.Widget;

import java.util.*;

public class SupplyLoader {
    private static final String[] CATEGORIES = new String[] {"Life", "Chaos", "Power"};
    private static final Set<String> CATEGORY_SET = new HashSet<>(Arrays.asList(CATEGORIES));

    private static final Map<Integer, String> SUPPLY_MAP = new HashMap<>();

    static {
        for (Supply supply : Supply.values()) {
            SUPPLY_MAP.put(supply.value, supply.name());
        }
    }

    public Map<String, Map<String, Integer>> loadSupplies(Widget w) {
        Stack<Widget> widgetQueue = new Stack<>();
        widgetQueue.add(w);

        Map<String, Map<String, Integer>> supplyTracker = new HashMap<>();

        while (!widgetQueue.isEmpty()) {
            Widget currentWidget = widgetQueue.pop();
            Widget[] children = currentWidget.getChildren();

            if (children == null) {
                continue;
            }

            Map<String, Integer> subMap = new HashMap<>();
            String key = null;
            for (Widget child : children) {
                widgetQueue.push(child);
                key = updateKey(child, key);
                addSupplies(child, subMap);
            }
            if (key != null) {
                supplyTracker.put(key, subMap);
            }
        }

        return supplyTracker;
    }

    private String updateKey(Widget widget, String key) {
        if (CATEGORY_SET.contains(widget.getText())) {
            return widget.getText();
        }
        return key;
    }

    private void addSupplies(Widget widget, Map<String, Integer> supplyMapPartial) {
        if (SUPPLY_MAP.containsKey(widget.getItemId())) {
            String supplyName = SUPPLY_MAP.get(widget.getItemId())
            supplyMapPartial.put(supplyName, widget.getItemQuantity());
        }
    }
}
