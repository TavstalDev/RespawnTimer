package io.github.tavstal.respawntimer.platform;

import io.github.tavstal.respawntimer.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isClientSide() {

        return FMLLoader.getDist().isClient();
    }

    @Override
    public boolean isServerSide() {

        return FMLLoader.getDist().isDedicatedServer();
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isPlugin() {
        return false;
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission) {
        return  player.hasPermissions(4);
    }
}