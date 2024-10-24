package com.example;

import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.runelite.http.api.RuneLiteAPI.JSON;

@Slf4j
public class SupplyRequest {
    public static String TOA_SUPPLY_SERVER_URL = "http://127.0.0.1:5000";


    @AllArgsConstructor
    public static class SupplyRequestData {
        public String myName;
        public String[] partyNames;

        public int minimalNectar;
        public int minimalTears;
        public int excessPriority;

        Map<String, Map<String, Integer>> iHave;
    }

    public void performRequest(Client client, ExamplePlugin plugin, Map<String, Map<String, Integer>> supplies) {
        SupplyRequestData data = new SupplyRequestData(
                client.getLocalPlayer().getName(),
                plugin.partyChecker.partyPlayerMap.keySet().toArray(new String[0]),
                plugin.config.minimalNectar(),
                plugin.config.minimalTears(),
                plugin.config.excessPriority(),
                supplies
        );

        Request request = new Request.Builder()
                .url(TOA_SUPPLY_SERVER_URL + "/toa_supply")
                .post(RequestBody.create(JSON, plugin.gson.toJson(data)))
                .build();

        Call call = plugin.okHttpClient.newCall(request);
        call.timeout().timeout(30 * 60, TimeUnit.SECONDS);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.debug("Failed to submit: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (response) {
                    if (!response.isSuccessful()) {
                        log.debug("Failed to submit: {}", response.code());
                        return;
                    }
                    log.info("Response: " + response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
