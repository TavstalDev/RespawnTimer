package io.github.tavstal.respawntimer;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListener {
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
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
