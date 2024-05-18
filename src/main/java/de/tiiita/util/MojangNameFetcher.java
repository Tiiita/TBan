package de.tiiita.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.UUID;
import java.util.logging.Level;

public class MojangNameFetcher {

    private final static String SERVER_URL = "https://api.mojang.com/user/profile/%s";

    /**
     * Fetches the name from the mojang api by using the uuid.
     * This code blocks the thread for about 250-500 ms!
     * @param uniqueId the uuid of the player.
     * @return the uuid
     * @throws IOException
     */
    public static String fetchName(UUID uniqueId) throws IOException, Exception {

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

        JsonElement name = getFromJson(response.toString(), "name");

        if (name == null) {
            throw new Exception("Name not found of uuid: " + uniqueId + " Error Message: " + getFromJson(response.toString(), "error"));
        }
        return name.getAsString();
    }

    public static String fetchNameSafely(UUID uuid) {
        try {
            return fetchName(uuid);
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
            return "/";
        }
    }

    private static JsonElement getFromJson(String json, String key) {
        return JsonParser.parseString(json).getAsJsonObject().get(key);
    }
}
