package io.github.tavstal.respawntimer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class EventListener {
    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        CommonEvents.OnServerTick(event.getServer());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        CommonEvents.OnPlayerConnected(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        CommonEvents.OnPlayerDisconnected(event.getEntity());
    }

    @SubscribeEvent
    public  void onPlayerDeath(LivingDeathEvent event) {
        CommonEvents.OnPlayerDeath(event.getEntity(), event.getSource());
    }
}
