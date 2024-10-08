package io.github.tavstal.respawntimer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerConnection;

public class FabricMain implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();

        // Player Connected Event
        ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> CommonEvents.OnPlayerConnected(handler.player));

        // Player Disconnected Event
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> CommonEvents.OnPlayerDisconnected(handler.player));

        ServerTickEvents.START_SERVER_TICK.register((server) -> CommonEvents.OnServerTick(server));

        // Player Death
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> CommonEvents.OnPlayerDeath(entity, damageSource));
    }
}
