package io.github.tavstaldev.respawntimer;

import io.github.tavstaldev.minecorelib.PluginBase;
import io.github.tavstaldev.minecorelib.core.PluginLogger;
import io.github.tavstaldev.minecorelib.core.PluginTranslator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class RespawnTimer extends PluginBase {
    public static RespawnTimer Instance;
    private final PluginLogger _logger;
    private final PluginTranslator _translator;
    public static PluginLogger Logger() {
        return Instance.getCustomLogger();
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
        _logger = getCustomLogger();
        _translator = getTranslator();
    }

    /**
     * Called when the plugin is enabled.
     * Initializes the plugin, registers events and commands, loads configurations and localizations.
     */
    @Override
    public void onEnable() {
        Instance = this;
        _logger.Info("Loading RespawnTimer...");

        // Register Events
        EventListener.init();

        // Generate config file
        saveDefaultConfig();

        // Load Localizations
        if (!_translator.Load())
        {
            _logger.Error("Failed to load localizations... Unloading...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Schedule a task to run every second
        _logger.Ok("RespawnTimer has been successfully loaded.");
        isUpToDate().thenAccept(upToDate -> {
            if (upToDate) {
                _logger.Ok("Plugin is up to date!");
            } else {
                _logger.Warn("A new version of the plugin is available: " + getDownloadUrl());
            }
        }).exceptionally(e -> {
            _logger.Error("Failed to determine update status: " + e.getMessage());
            return null;
        });
    }

    /**
     * Called when the plugin is disabled.
     * Logs an informational message indicating that the RespawnTimer plugin has been successfully unloaded.
     */
    @Override
    public void onDisable() {
        _logger.Ok("RespawnTimer has been successfully unloaded.");
    }

    /**
     * Reloads the plugin configuration and localizations.
     */
    public void reload() {
        _logger.Info("Reloading RespawnTimer...");
        _logger.Debug("Reloading localizations...");
        _translator.Load();
        _logger.Debug("Localizations reloaded.");
        _logger.Debug("Reloading configuration...");
        this.reloadConfig();
        _logger.Debug("Configuration reloaded.");
        _logger.Info("RespawnTimer reloaded.");
    }
}
