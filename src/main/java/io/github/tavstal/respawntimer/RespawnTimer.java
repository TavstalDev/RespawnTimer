package io.github.tavstal.respawntimer;

import io.github.tavstal.respawntimer.utils.LocaleUtils;
import io.github.tavstal.respawntimer.utils.LoggerUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class RespawnTimer extends JavaPlugin {
    //#region Constants
    public static final String PROJECT_NAME = "RespawnTimer";
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR = "Tavstal";
    public static final String DOWNLOAD_URL = "https://github.com/TavstalDev/RespawnTimer/releases/latest";
    //#endregion
    public static RespawnTimer Instance;
    /**
     * Gets the plugin configuration.
     * @return The FileConfiguration object.
     */
    public static FileConfiguration GetConfig(){
        return Instance.getConfig();
    }

    /**
     * Called when the plugin is enabled.
     * Initializes the plugin, registers events and commands, loads configurations and localizations.
     */
    @Override
    public void onEnable() {
        Instance = this;
        LoggerUtils.LogInfo("Loading RespawnTimer...");

        // Register Events
        EventListener.init();

        // Generate config file
        saveDefaultConfig();

        // Load Localizations
        if (!LocaleUtils.Load())
        {
            LoggerUtils.LogError("Failed to load localizations... Unloading...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Schedule a task to run every second
        LoggerUtils.LogInfo("RespawnTimer has been successfully loaded.");
        if (!isUpToDate())
            LoggerUtils.LogWarning("A new version of RespawnTimer is available! Download it at: " + DOWNLOAD_URL);
    }

    /**
     * Called when the plugin is disabled.
     * Logs an informational message indicating that the RespawnTimer plugin has been successfully unloaded.
     */
    @Override
    public void onDisable() {
        LoggerUtils.LogInfo("RespawnTimer has been successfully unloaded.");
    }

    /**
     * Reloads the plugin configuration and localizations.
     */
    public void reload() {
        LoggerUtils.LogInfo("Reloading RespawnTimer...");
        LoggerUtils.LogDebug("Reloading localizations...");
        LocaleUtils.Load();
        LoggerUtils.LogDebug("Localizations reloaded.");
        LoggerUtils.LogDebug("Reloading configuration...");
        this.reloadConfig();
        LoggerUtils.LogDebug("Configuration reloaded.");
        LoggerUtils.LogInfo("RespawnTimer reloaded.");
    }

    /**
     * Checks if the plugin is up to date by comparing the current version with the latest release version.
     * @return true if the plugin is up to date, false otherwise.
     */
    public boolean isUpToDate() {
        String version;
        LoggerUtils.LogDebug("Checking for updates...");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            LoggerUtils.LogDebug("Sending request to GitHub...");
            HttpGet request = new HttpGet(DOWNLOAD_URL);
            HttpResponse response = httpClient.execute(request);
            LoggerUtils.LogDebug("Received response from GitHub.");
            String jsonResponse = EntityUtils.toString(response.getEntity());
            LoggerUtils.LogDebug("Parsing response...");
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            LoggerUtils.LogDebug("Parsing release version...");
            version = jsonObject.get("tag_name").toString();
        } catch (IOException e) {
            LoggerUtils.LogError("Failed to check for updates.");
            return false;
        } catch (ParseException e) {
            LoggerUtils.LogError("Failed to parse release version.");
            return false;
        }

        LoggerUtils.LogDebug("Current version: " + VERSION);
        LoggerUtils.LogDebug("Latest version: " + version);
        return version.equalsIgnoreCase(VERSION);
    }
}
