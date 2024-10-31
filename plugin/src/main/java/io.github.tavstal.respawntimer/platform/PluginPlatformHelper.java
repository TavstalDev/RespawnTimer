package io.github.tavstal.respawntimer.platform;

import io.github.tavstal.respawntimer.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class PluginPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Paper";
    }

    // Note: plugin is always server side
    @Override
    public boolean isClientSide() {

        return false;
    }

    @Override
    public boolean isServerSide() {

        return true;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return false;
    }

    @Override
    public boolean isPlugin() {
        return true;
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return false;
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission) {
        CraftPlayer craftPlayer = player.getBukkitEntity();
        return  craftPlayer.hasPermission(permission);
    }
}
