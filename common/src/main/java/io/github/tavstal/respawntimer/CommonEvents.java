package io.github.tavstal.respawntimer;

import io.github.tavstal.respawntimer.utils.EntityUtils;
import io.github.tavstal.respawntimer.utils.LocaleUtils;
import io.github.tavstal.respawntimer.utils.PlayerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public class CommonEvents {
    private static int _time;

    public static void OnPlayerConnected(Player player) {
        try
        {
            CommonClass.LOG.debug("PLAYER_CONNECT was called by {}", EntityUtils.GetName(player));
            if (player instanceof ServerPlayer serverPlayer && CommonClass.IsPlayerDead(player.getStringUUID()))
                CommonClass.SetPlayerDead(serverPlayer, null);
        }
        catch (Exception ex)
        {
            CommonClass.LOG.error("Error during executing event 'OnPlayerConnected':");
            CommonClass.LOG.error(ex.getLocalizedMessage());
        }
    }

    public static void OnPlayerDisconnected(Player player) {
        try {
            CommonClass.LOG.debug("PLAYER_DISCONNECT was called by {}", EntityUtils.GetName(player));
            if (player instanceof ServerPlayer serverPlayer && CommonClass.IsPlayerDead(player.getStringUUID()))
                CommonClass.RespawnPlayer(serverPlayer, true);
        }
        catch (Exception ex)
        {
            CommonClass.LOG.error("Error during executing event 'OnPlayerDisconnected':");
            CommonClass.LOG.error(ex.getLocalizedMessage());
        }
    }

    public static boolean OnPlayerDeath(LivingEntity entity, DamageSource damageSource) {
        try {
            CommonClass.LOG.debug("ENTITY_DEATH was called by {}", EntityUtils.GetName(entity));
            if (!(entity instanceof ServerPlayer player)) {
                return true;
            }

            if (CommonClass.CONFIG().IgnoreCreativePlayers && player.isCreative())
                return true;

            var mainHandItem = player.getMainHandItem();
            var offhandItem = player.getOffhandItem();
            if (CommonClass.CONFIG().TotemIds.contains(mainHandItem.getItem().toString()) || CommonClass.CONFIG().TotemIds.contains(offhandItem.getItem().toString()))
                return true;

            if (CommonClass.CONFIG().ClearInventory) {
                player.getInventory().clearContent();
            }
            else if (CommonClass.CONFIG().DropInventory) {
                player.getInventory().dropAll();
            }

            CommonClass.SetPlayerDead(player, damageSource);
            player.getServer().getPlayerList().broadcastSystemMessage(damageSource.getLocalizedDeathMessage(player), false);

            if (CommonClass.CONFIG().EnableChatMessages) {
                player.sendSystemMessage(LocaleUtils.GetLocaleCompPrefix("messages.died"));
            }
            if (CommonClass.CONFIG().EnableTitleScreen) {
                PlayerUtils.SendTitleMessage(player, LocaleUtils.GetLocale("messages.died"), LocaleUtils.GetLocale("messages.died"), 5);
            }
            if (CommonClass.CONFIG().EnableActionBarMessage) {
                player.displayClientMessage(LocaleUtils.GetLocaleComp("messages.died"), true);
            }

            return false;
        }
        catch (Exception ex)
        {
            CommonClass.LOG.error("Error during executing event 'OnPlayerDeath':");
            CommonClass.LOG.error(ex.getLocalizedMessage());
            return  true;
        }
    }

    public static void OnServerTick(MinecraftServer server) {
        if (server.getTickCount() % 20 != 0)
            return;

        var deadPlayerDictionary = CommonClass.GetPlayerDataList();
        var deadPlayers = Collections.list(deadPlayerDictionary.keys());
        var playerList = server.getPlayerList();
        for (String i : deadPlayers) {
            ServerPlayer player = playerList.getPlayer(UUID.fromString(i));
            if (player == null)
                continue;

            long duration = Duration.between(LocalDateTime.now(), deadPlayerDictionary.get(i)).getSeconds();
            if (duration <= 0)
            {
                CommonClass.RespawnPlayer(player, false);

                if (CommonClass.CONFIG().EnableChatMessages) {
                    player.sendSystemMessage(LocaleUtils.GetLocaleCompPrefix("messages.respawned"));
                }
                if (CommonClass.CONFIG().EnableTitleScreen) {
                    PlayerUtils.SendTitleMessage(player, "", LocaleUtils.GetLocale("messages.respawned"), 5);
                }
                if (CommonClass.CONFIG().EnableActionBarMessage) {
                    player.displayClientMessage(LocaleUtils.GetLocaleComp("messages.respawned"), true);
                }

                continue;
            }

            if (CommonClass.CONFIG().EnableChatMessages) {
                player.sendSystemMessage(LocaleUtils.GetLocaleCompPrefix("messages.respawning", duration));
            }
            if (CommonClass.CONFIG().EnableTitleScreen) {
                PlayerUtils.SendTitleMessage(player, LocaleUtils.GetLocale("messages.died"), LocaleUtils.GetLocale("messages.respawning", duration), 0);
            }
            if (CommonClass.CONFIG().EnableActionBarMessage) {
                player.displayClientMessage(LocaleUtils.GetLocaleComp("messages.respawning", duration), true);
            }

        }
    }
}
