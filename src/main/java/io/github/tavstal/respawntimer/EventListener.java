package io.github.tavstal.respawntimer;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import io.github.tavstal.respawntimer.utils.ChatUtils;
import io.github.tavstal.respawntimer.utils.LocaleUtils;
import io.github.tavstal.respawntimer.utils.LoggerUtils;
import io.github.tavstal.respawntimer.utils.RespawnUtils;
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
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class EventListener implements Listener {
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
            LoggerUtils.LogDebug("PLAYER_CONNECT was called by " + event.getPlayer().name());
            if (RespawnUtils.IsPlayerDead(event.getPlayer().getUniqueId())) {
                LoggerUtils.LogDebug("Player is dead, setting player to dead state.");
                RespawnUtils.SetPlayerDead(event.getPlayer(), null);
            }
        }
        catch (Exception ex)
        {
            LoggerUtils.LogError("Error during executing event 'OnPlayerConnected':");
            LoggerUtils.LogError(ex.getLocalizedMessage());
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
            LoggerUtils.LogDebug("PLAYER_DISCONNECT was called by " + event.getPlayer().name());
            if (RespawnUtils.IsPlayerDead(event.getPlayer().getUniqueId())) {
                LoggerUtils.LogDebug("Player is dead, temporarily respawning player.");
                RespawnUtils.RespawnPlayer(event.getPlayer(), true);
            }
        }
        catch (Exception ex)
        {
            LoggerUtils.LogError("Error during executing event 'OnPlayerDisconnected':");
            LoggerUtils.LogError(ex.getLocalizedMessage());
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
            LoggerUtils.LogDebug("ENTITY_DEATH was called by " + event.getPlayer().name());
            var player = event.getPlayer();
            if (RespawnTimer.GetConfig().getBoolean("ignoreCreativePlayers") && player.getGameMode() == GameMode.CREATIVE)
                return;

            LoggerUtils.LogDebug("Player is not in creative mode, continuing with death handling.");
            var mainHandItem = player.getInventory().getItemInMainHand();
            var offhandItem = player.getInventory().getItemInOffHand();
            List<String> totemIds = RespawnTimer.GetConfig().getStringList("totemIds");
            if (totemIds.contains(mainHandItem.toString()) || totemIds.contains(offhandItem.toString()))
                return;

            LoggerUtils.LogDebug("Player does not have a totem of undying, continuing with death handling.");
            if (RespawnTimer.GetConfig().getBoolean("clearInventory")) {
                LoggerUtils.LogDebug("Clearing player inventory.");
                player.getInventory().clear();
            }
            else if (RespawnTimer.GetConfig().getBoolean("dropInventory")) {
                LoggerUtils.LogDebug("Dropping player inventory.");
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                player.getInventory().clear();
            }

            LoggerUtils.LogDebug("Setting player to dead state.");
            RespawnUtils.SetPlayerDead(player, event.getDamageSource());
            LoggerUtils.LogDebug("Broadcasting death message.");
            var deathMsg = event.deathMessage();
            if (deathMsg != null)
                Bukkit.broadcast(deathMsg);

            LoggerUtils.LogDebug("Sending death messages to player.");
            if (RespawnTimer.GetConfig().getBoolean("enableChatMessages")) {
                ChatUtils.sendLocalizedMsg(player, "Died");
            }
            if (RespawnTimer.GetConfig().getBoolean("enableTitleScreen")) {
                player.showTitle(Title.title(ChatUtils.translateColors(LocaleUtils.Localize(player, "Died"), true), ChatUtils.translateColors(LocaleUtils.Localize(player, "Died"), true)));
            }
            if (RespawnTimer.GetConfig().getBoolean("enableActionBarMessage")) {
                player.sendActionBar(ChatUtils.translateColors(LocaleUtils.Localize(player, "Died"), true));
            }

            LoggerUtils.LogDebug("Cancelling event.");
            event.setCancelled(true);
        }
        catch (Exception ex)
        {
            LoggerUtils.LogError("Error during executing event 'OnPlayerDeath':");
            LoggerUtils.LogError(ex.getLocalizedMessage());
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

        LoggerUtils.LogDebug("Checking for dead players...");
        var deadPlayerDictionary = RespawnUtils.GetPlayerDataList();
        var deadPlayers = Collections.list(deadPlayerDictionary.keys());
        for (UUID playerId : deadPlayers) {
            try {
                var player = Bukkit.getPlayer(playerId);
                if (player == null)
                    continue;

                LoggerUtils.LogDebug(String.format("%s is dead, checking if player should be respawned.", player.getName()));
                long duration = Duration.between(LocalDateTime.now(), deadPlayerDictionary.get(playerId)).getSeconds();
                if (duration <= 0) {
                    LoggerUtils.LogDebug("Player should be respawned.");
                    RespawnUtils.RespawnPlayer(player, false);

                    LoggerUtils.LogDebug("Sending respawn messages to player.");
                    if (RespawnTimer.GetConfig().getBoolean("enableChatMessages")) {
                        ChatUtils.sendLocalizedMsg(player, "Respawned");
                    }
                    if (RespawnTimer.GetConfig().getBoolean("enableTitleScreen")) {
                        player.showTitle(Title.title(Component.empty(),
                                ChatUtils.translateColors(LocaleUtils.Localize(player, "Respawned"), true)));
                    }
                    if (RespawnTimer.GetConfig().getBoolean("enableActionBarMessage")) {
                        player.sendActionBar(ChatUtils.translateColors(LocaleUtils.Localize(player, "Respawned"), true));
                    }
                    continue;
                }

                LoggerUtils.LogDebug("Player should not be respawned yet.");
                long minutes = duration / 60;
                long remainingSeconds = duration % 60;
                String time = String.format(LocaleUtils.Localize(player, "Time"), minutes, remainingSeconds);
                LoggerUtils.LogDebug(String.format("Sending respawn messages to player. Time remaining: %s", time));
                if (RespawnTimer.GetConfig().getBoolean("enableChatMessages")) {
                    ChatUtils.sendLocalizedMsg(player, "Respawning", new Hashtable<>() {{
                        put("time", time);
                    }});
                }
                if (RespawnTimer.GetConfig().getBoolean("enableTitleScreen")) {
                    player.showTitle(Title.title(
                            ChatUtils.translateColors(LocaleUtils.Localize(player, "Died"), true),
                            ChatUtils.translateColors(LocaleUtils.Localize(player, "Respawning").replace("%time%", time), true),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ofSeconds(1))
                    ));
                }
                if (RespawnTimer.GetConfig().getBoolean("enableActionBarMessage")) {
                    player.sendActionBar(ChatUtils.translateColors(LocaleUtils.Localize(player, "Respawning").replace("%time%", time), true));
                }
            }
            catch (Exception ex)
            {
                LoggerUtils.LogError("Error during executing event 'OnServerTick':");
                LoggerUtils.LogError(ex.getMessage());
            }
        }
    }
}
