package io.github.tavstal.respawntimer;

import io.github.tavstal.minecorelib.PluginBase;
import io.github.tavstal.minecorelib.core.PluginLogger;
import io.github.tavstal.minecorelib.core.PluginTranslator;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class RespawnTimer extends PluginBase {
    public static RespawnTimer Instance;
    public static PluginLogger Logger() {
        return Instance.getCustomLogger();
    }
    public static PluginTranslator Translator() {
        return Instance.getTranslator();
    }
    /**
     * Gets the plugin configuration.
     * @return The FileConfiguration object.
     */
    public static FileConfiguration GetConfig(){
        return Instance.getConfig();
    }

    public RespawnTimer() {
        super("RespawnTimer",
                "1.0.0",
                "Tavstal",
                "https://github.com/TavstalDev/RespawnTimer/releases/latest",
                new String[] { "eng", "hun" }
        );
    }

    /**
     * Called when the plugin is enabled.
     * Initializes the plugin, registers events and commands, loads configurations and localizations.
     */
    @Override
    public void onEnable() {
        Instance = this;
        getCustomLogger().Info("Loading RespawnTimer...");

        // Register Events
        EventListener.init();

        // Generate config file
        saveDefaultConfig();

        // Load Localizations
        if (!getTranslator().Load())
        {
            getCustomLogger().Error("Failed to load localizations... Unloading...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Schedule a task to run every second
        getCustomLogger().Info("RespawnTimer has been successfully loaded.");
        if (!isUpToDate())
            getCustomLogger().Warn("A new version of RespawnTimer is available! Download it at: " + getDownloadUrl());
    }

    /**
     * Called when the plugin is disabled.
     * Logs an informational message indicating that the RespawnTimer plugin has been successfully unloaded.
     */
    @Override
    public void onDisable() {
        getCustomLogger().Info("RespawnTimer has been successfully unloaded.");
    }

    /**
     * Reloads the plugin configuration and localizations.
     */
    public void reload() {
        getCustomLogger().Info("Reloading RespawnTimer...");
        getCustomLogger().Debug("Reloading localizations...");
        getTranslator().Load();
        getCustomLogger().Debug("Localizations reloaded.");
        getCustomLogger().Debug("Reloading configuration...");
        this.reloadConfig();
        getCustomLogger().Debug("Configuration reloaded.");
        getCustomLogger().Info("RespawnTimer reloaded.");
    }

    /**
     * Checks if the plugin is up to date by comparing the current version with the latest release version.
     * @return true if the plugin is up to date, false otherwise.
     */
    public boolean isUpToDate() {
        String version;
        getCustomLogger().Debug("Checking for updates...");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            getCustomLogger().Debug("Sending request to GitHub...");
            HttpGet request = new HttpGet(getDownloadUrl());
            HttpResponse response = httpClient.execute(request);
            getCustomLogger().Debug("Received response from GitHub.");
            String jsonResponse = EntityUtils.toString(response.getEntity());
            getCustomLogger().Debug("Parsing response...");
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            getCustomLogger().Debug("Parsing release version...");
            version = jsonObject.get("tag_name").toString();
        } catch (IOException e) {
            getCustomLogger().Error("Failed to check for updates.");
            return false;
        } catch (ParseException e) {
            getCustomLogger().Error("Failed to parse release version.");
            return false;
        }

        getCustomLogger().Debug("Current version: " + getVersion());
        getCustomLogger().Debug("Latest version: " + version);
        return version.equalsIgnoreCase(getVersion());
    }
}
