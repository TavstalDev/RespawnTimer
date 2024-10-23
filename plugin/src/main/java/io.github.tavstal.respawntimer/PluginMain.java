package io.github.tavstal.respawntimer;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        CommonClass.init(((CraftServer)Bukkit.getServer()).getServer(), true);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ServerPlayer mcPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
        CommonEvents.OnPlayerConnected(mcPlayer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ServerPlayer mcPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
        CommonEvents.OnPlayerDisconnected(mcPlayer);

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        ServerPlayer mcPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
        DamageSource source = mcPlayer.getLastDamageSource();
        event.setCancelled(!CommonEvents.OnPlayerDeath(mcPlayer, source));
    }

    @EventHandler
    public void onServerTick(ServerTickEndEvent event) {

        CommonEvents.OnServerTick(((CraftServer)this.getServer()).getServer());
    }
}
