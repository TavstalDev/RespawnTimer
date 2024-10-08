package io.github.tavstal.respawntimer.platform;

import io.github.tavstal.respawntimer.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
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
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }
}