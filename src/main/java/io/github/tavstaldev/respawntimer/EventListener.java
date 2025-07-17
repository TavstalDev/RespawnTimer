package io.github.tavstaldev.respawntimer;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.github.tavstaldev.minecorelib.core.PluginLogger;
import io.github.tavstaldev.minecorelib.utils.ChatUtils;
import io.github.tavstaldev.respawntimer.utils.RespawnUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class EventListener implements Listener {
    private final PluginLogger _logger = RespawnTimer.Logger().WithModule(EventListener.class);
    
    /**
     * Initializes the event listener by registering it with the plugin manager.
     */
    public static void init() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), RespawnTimer.Instance);
    }


    /**
     * Handles the player join event.
     *
     * @param event The player join event.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try
        {
            _logger.Debug("PLAYER_CONNECT was called by " + event.getPlayer().name());
            if (RespawnUtils.IsPlayerDead(event.getPlayer().getUniqueId())) {
                _logger.Debug("Player is dead, setting player to dead state.");
                RespawnUtils.SetPlayerDead(event.getPlayer(), null);
            }
        }
        catch (Exception ex)
        {
            _logger.Error("Error during executing event 'OnPlayerConnected':");
            _logger.Error(ex.getLocalizedMessage());
        }
    }

    /**
     * Handles the player quit event.
     *
     * @param event The player quit event.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            _logger.Debug("PLAYER_DISCONNECT was called by " + event.getPlayer().name());
            if (RespawnUtils.IsPlayerDead(event.getPlayer().getUniqueId())) {
                _logger.Debug("Player is dead, temporarily respawning player.");
                RespawnUtils.RespawnPlayer(event.getPlayer(), true);
            }
        }
        catch (Exception ex)
        {
            _logger.Error("Error during executing event 'OnPlayerDisconnected':");
            _logger.Error(ex.getLocalizedMessage());
        }
    }

    /**
     * Handles the player death event.
     *
     * @param event The player death event.
     */
    @SuppressWarnings("UnstableApiUsage")
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        try {
            _logger.Debug("ENTITY_DEATH was called by " + event.getPlayer().name());
            var player = event.getPlayer();
            if (RespawnTimer.GetConfig().getBoolean("ignoreCreativePlayers") && player.getGameMode() == GameMode.CREATIVE)
                return;

            _logger.Debug("Player is not in creative mode, continuing with death handling.");
            var mainHandItem = player.getInventory().getItemInMainHand();
            var offhandItem = player.getInventory().getItemInOffHand();
            List<String> totemIds = RespawnTimer.GetConfig().getStringList("totemIds");
            if (totemIds.contains(mainHandItem.toString()) || totemIds.contains(offhandItem.toString()))
                return;

            _logger.Debug("Player does not have a totem of undying, continuing with death handling.");
            if (RespawnTimer.GetConfig().getBoolean("clearInventory")) {
                _logger.Debug("Clearing player inventory.");
                player.getInventory().clear();
            }
            else if (RespawnTimer.GetConfig().getBoolean("dropInventory")) {
                _logger.Debug("Dropping player inventory.");
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                player.getInventory().clear();
            }

            _logger.Debug("Setting player to dead state.");
            RespawnUtils.SetPlayerDead(player, event.getDamageSource());
            _logger.Debug("Broadcasting death message.");
            var deathMsg = event.deathMessage();
            if (deathMsg != null)
                Bukkit.broadcast(deathMsg);

            _logger.Debug("Sending death messages to player.");
            if (RespawnTimer.GetConfig().getBoolean("enableChatMessages")) {
                RespawnTimer.Instance.sendLocalizedMsg(player, "Died");
            }
            if (RespawnTimer.GetConfig().getBoolean("enableTitleScreen")) {
                player.showTitle(Title.title(ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Died"), true), ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Died"), true)));
            }
            if (RespawnTimer.GetConfig().getBoolean("enableActionBarMessage")) {
                player.sendActionBar(ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Died"), true));
            }

            _logger.Debug("Cancelling event.");
            event.setCancelled(true);
        }
        catch (Exception ex)
        {
            _logger.Error("Error during executing event 'OnPlayerDeath':");
            _logger.Error(ex.getLocalizedMessage());
        }
    }

    /**
     * Handles the server tick end event.
     *
     * @param event The server tick end event.
     */
    @EventHandler
    public void onServerTick(ServerTickEndEvent event) {
        if (Bukkit.getServer().getCurrentTick() % 20 != 0)
            return;

        _logger.Debug("Checking for dead players...");
        var deadPlayerDictionary = RespawnUtils.GetPlayerDataList();
        for (UUID playerId : deadPlayerDictionary.keySet()) {
            try {
                var player = Bukkit.getPlayer(playerId);
                if (player == null)
                    continue;

                _logger.Debug(String.format("%s is dead, checking if player should be respawned.", player.getName()));
                long duration = Duration.between(LocalDateTime.now(), deadPlayerDictionary.get(playerId)).getSeconds();
                if (duration <= 0) {
                    _logger.Debug("Player should be respawned.");
                    RespawnUtils.RespawnPlayer(player, false);

                    _logger.Debug("Sending respawn messages to player.");
                    if (RespawnTimer.GetConfig().getBoolean("enableChatMessages")) {
                        RespawnTimer.Instance.sendLocalizedMsg(player, "Respawned");
                    }
                    if (RespawnTimer.GetConfig().getBoolean("enableTitleScreen")) {
                        player.showTitle(Title.title(Component.empty(),
                                ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Respawned"), true)));
                    }
                    if (RespawnTimer.GetConfig().getBoolean("enableActionBarMessage")) {
                        player.sendActionBar(ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Respawned"), true));
                    }
                    continue;
                }

                _logger.Debug("Player should not be respawned yet.");
                long minutes = duration / 60;
                long remainingSeconds = duration % 60;
                String time = String.format(RespawnTimer.Instance.Localize(player, "Time"), minutes, remainingSeconds);
                _logger.Debug(String.format("Sending respawn messages to player. Time remaining: %s", time));
                if (RespawnTimer.GetConfig().getBoolean("enableChatMessages")) {
                    RespawnTimer.Instance.sendLocalizedMsg(player, "Respawning", new HashMap<>() {{
                        put("time", time);
                    }});
                }
                if (RespawnTimer.GetConfig().getBoolean("enableTitleScreen")) {
                    player.showTitle(Title.title(
                            ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Died"), true),
                            ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Respawning").replace("%time%", time), true),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ofSeconds(1))
                    ));
                }
                if (RespawnTimer.GetConfig().getBoolean("enableActionBarMessage")) {
                    player.sendActionBar(ChatUtils.translateColors(RespawnTimer.Instance.Localize(player, "Respawning").replace("%time%", time), true));
                }
            }
            catch (Exception ex)
            {
                _logger.Error("Error during executing event 'OnServerTick':");
                _logger.Error(ex.getMessage());
            }
        }
    }
}
