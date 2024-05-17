package de.tiiita.util;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class MojangNameFetcher {

    private final static String SERVER_URL = "https://api.mojang.com/user/profile/%s";

    public static CompletableFuture<String> fetchName(UUID uniqueId)  {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(String.format(SERVER_URL, uniqueId.toString())).openConnection();
                connection.setReadTimeout(2000);
                connection.setRequestMethod("GET");


                if (connection.getResponseCode() != 200) {
                    ProxyServer.getInstance().getLogger().log(Level.WARNING, "Did not get response code as expected (name fetcher)");
                    return null;
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return JsonParser.parseString(response.toString()).getAsJsonObject().get("name").getAsString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
