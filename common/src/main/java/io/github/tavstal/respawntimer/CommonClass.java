package io.github.tavstal.respawntimer;

import io.github.tavstal.respawntimer.commands.RespawnCommand;
import io.github.tavstal.respawntimer.utils.ConfigUtils;
import io.github.tavstal.respawntimer.utils.LocaleUtils;
import io.github.tavstal.respawntimer.utils.PlayerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;

import static net.minecraft.world.damagesource.DamageTypes.*;

public class CommonClass {
    /** The unique identifier for the mod. */
    public static final String MOD_ID = "respawntimer";
    /** The display name of the mod. */
    public static final String MOD_NAME = "RespawnTImer";
    /** Logger instance for logging messages related to the mod. */
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    /**
     * Indicates whether the mod is running as a plugin.
     */
    private static boolean _isPlugin;
    /**
     * Checks if the mod is running as a plugin.
     *
     * @return {@code true} if the mod is a plugin; {@code false} otherwise.
     */
    public static boolean IsPlugin() {
        return _isPlugin;
    }

    private final static Dictionary<String, LocalDateTime> _deadPlayers = new Hashtable<>();

    public static Dictionary<String, LocalDateTime> GetPlayerDataList() {
        return _deadPlayers;
    }

    /**
     * Holds the configuration settings for the mod.
     * This instance is initialized to {@code null} until the configuration is loaded.
     */
    private static CommonConfig _config = null;

    /**
     * Retrieves the singleton instance of {@link CommonConfig} for global configuration access.
     * <p>
     * This method provides a central point to access the configuration settings
     * across the application, ensuring a consistent state is used throughout.
     * </p>
     *
     * @return The {@link CommonConfig} instance containing current configuration values.
     */
    public static CommonConfig CONFIG() {
        if (_config == null) {
            _config = ConfigUtils.loadConfig();
        }
        return _config;
    }

    /**
     * Updates the configuration settings with the provided values.
     *
     * @param newValue an instance of {@code CommonConfig} containing the updated configuration values.
     */
    public static void UpdateConfig(CommonConfig newValue) {
        _config = newValue;
        ConfigUtils.saveConfig(newValue);
    }

    /**
     * Initializes the mod or plugin with the given server instance and mode.
     * <p>
     * This method sets up necessary configurations or states for the provided
     * {@code MinecraftServer} instance, depending on whether the initialization is
     * for a plugin or mod.
     * </p>
     *
     * @param server    The {@link MinecraftServer} instance to initialize, providing access
     *                  to server resources, configurations, and management functions.
     * @param isPlugin  A {@code boolean} indicating if the initialization is for a plugin
     *                  ({@code true}) or for a standalone setup ({@code false}).
     */
    public static void init(MinecraftServer server, boolean isPlugin) {
        try {
            _isPlugin = isPlugin;
            if (CONFIG().EnableDebugMode) {
                SetLogLevel("DEBUG");
            }
            LocaleUtils.init();

            var commandDispatcher = server.getCommands().getDispatcher();
            RespawnCommand.register(commandDispatcher);

            LOG.info(MOD_NAME + " has been loaded.");
        }
        catch (Exception ex) {
            CommonClass.LOG.error("Failed to initialize the CommonClass:");
            CommonClass.LOG.error(ex.getLocalizedMessage());
        }
    }

    /**
     * Sets the logging level for the application.
     * <p>
     * This method configures the application's logging output based on the specified {@code level}.
     * Acceptable values may include levels like "DEBUG", "INFO", "WARN", and "ERROR",
     * depending on the logging framework used.
     * </p>
     *
     * @param level The desired logging level as a {@link String}. Valid levels are typically
     *              "DEBUG", "INFO", "WARN", "ERROR", etc.
     */
    private static void SetLogLevel(String level) {
        try {
            // Set the logging level for the logger
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            LoggerConfig loggerConfig = loggerContext.getConfiguration().getLoggerConfig(LOG.getName());
            loggerConfig.setLevel(Level.valueOf(level.toUpperCase()));
            loggerContext.updateLoggers();
        }
        catch (Exception ex)
        {
            LOG.error("Failed to set the log level:");
            LOG.error(ex.getLocalizedMessage());
        }
    }

    public static boolean IsPlayerDead(String UUID) {
        return _deadPlayers.get(UUID) != null;
    }

    public static void SetPlayerDead(ServerPlayer player, DamageSource source) {
        player.setGameMode(GameType.SPECTATOR);
        player.setHealth(20);
        player.removeAllEffects();
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(5F);
        player.clearFire();
        try {
            player.level().playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS);
        }
        catch (Exception ex) { /* ignore */ }

        if (IsPlayerDead(player.getStringUUID())) {
            long duration = Duration.between(LocalDateTime.now(), _deadPlayers.get(player.getStringUUID())).getSeconds();
            MobEffectInstance blindnessEffect = new MobEffectInstance(MobEffects.BLINDNESS, (int)duration * 20, 1);
            player.addEffect(blindnessEffect);
            return;
        }

        long respawnTime = GetRespawnTime(source);
        MobEffectInstance blindnessEffect = new MobEffectInstance(MobEffects.BLINDNESS, (int)respawnTime * 20, 1);
        player.addEffect(blindnessEffect);
        player.awardStat(Stats.DEATHS);
        player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));

        _deadPlayers.put(player.getStringUUID(), LocalDateTime.now().plusSeconds(respawnTime));
    }

    public static void RespawnPlayer(ServerPlayer player, boolean isTemporal) {
        if (!isTemporal)
            _deadPlayers.remove(player.getStringUUID());
        player.setGameMode(GameType.SURVIVAL);
        player.removeAllEffects();

        if (CONFIG().AllowHomeRespawn && player.getRespawnPosition() != null) {
            var position = player.getRespawnPosition();
            var dimension = player.getRespawnDimension();
            var angle = player.getRespawnAngle();
            var level = player.server.getLevel(dimension);
            player.teleportTo(level, (double)position.getX(), (double)position.getY(), (double)position.getZ(), angle, angle);

        }
        else if (!CONFIG().AllowLocationRespawn) {
            var overworld = player.server.overworld();
            var position = overworld.getSharedSpawnPos();
            player.teleportTo(overworld, (double)position.getX(), (double)position.getY(), (double)position.getZ(), overworld.getSharedSpawnAngle(), overworld.getSharedSpawnAngle());
        }
    }

    private static long GetRespawnTime(DamageSource source) {
        int respawnTime = 60;
        if (source.is(ARROW)) {
            respawnTime = CONFIG().ArrowDeathDuration;
        }
        else if (source.is(BAD_RESPAWN_POINT)) {
            respawnTime = CONFIG().BadRespawnPointDeathDuration;
        }
        else if (source.is(SWEET_BERRY_BUSH)) {
            respawnTime = CONFIG().BushDeathDuration;
        }
        else if (source.is(CACTUS)) {
            respawnTime = CONFIG().CactusDeathDuration;
        }
        else if (source.is(CAMPFIRE)) {
            respawnTime = CONFIG().CampfireDeathDuration;
        }
        else if (source.is(CRAMMING)) {
            respawnTime = CONFIG().CrammingDeathDuration;
        }
        else if (source.is(DRAGON_BREATH)) {
            respawnTime = CONFIG().DragonBreathDeathDuration;
        }
        else if (source.is(DROWN)) {
            respawnTime = CONFIG().DrownDeathDuration;
        }
        else if (source.is(DRY_OUT)) {
            respawnTime = CONFIG().DryOutDeathDuration;
        }
        /*else if (source.is(DamageTypes.)) {
            respawnTime = CONFIG().EnderPearlDeathDuration;
        }*/
        else if (source.is(EXPLOSION)) {
            respawnTime = CONFIG().ExplosionDeathDuration;
        }
        else if (source.is(FALL)) {
            respawnTime = CONFIG().FallDeathDuration;
        }
        else if (source.is(FALLING_BLOCK)) {
            respawnTime = CONFIG().FallingAnvilDeathDuration;
        }
        else if (source.is(FALLING_BLOCK)) {
            respawnTime = CONFIG().FallingBlockDeathDuration;
        }
        else if (source.is(FALLING_STALACTITE)) {
            respawnTime = CONFIG().FallingStalactiteDeathDuration;
        }
        else if (source.is(FIREBALL) || source.is(UNATTRIBUTED_FIREBALL)) {
            respawnTime = CONFIG().FireballDeathDuration;
        }
        else if (source.is(FIREWORKS)) {
            respawnTime = CONFIG().FireworksDeathDuration;
        }
        else if (source.is(FLY_INTO_WALL)) {
            respawnTime = CONFIG().FlyIntoWallDeathDuration;
        }
        else if (source.is(FREEZE)) {
            respawnTime = CONFIG().FreezeDeathDuration;
        }
        else if (source.is(GENERIC)) {
            respawnTime = CONFIG().GenericDeathDuration;
        }
        else if (source.is(GENERIC_KILL)) {
            respawnTime = CONFIG().GenericKillDeathDuration;
        }
        else if (source.is(HOT_FLOOR)) {
            respawnTime = CONFIG().HotFloorDeathDuration;
        }
        else if (source.is(INDIRECT_MAGIC)) {
            respawnTime = CONFIG().IndirectMagicDeathDuration;
        }
        else if (source.is(IN_FIRE)) {
            respawnTime = CONFIG().InFireDeathDuration;
        }
        else if (source.is(IN_WALL)) {
            respawnTime = CONFIG().InWallDeathDuration;
        }
        else if (source.is(LAVA)) {
            respawnTime = CONFIG().LavaDeathDuration;
        }
        else if (source.is(LIGHTNING_BOLT)) {
            respawnTime = CONFIG().LightningDeathDuration;
        }
        /*else if (source.is(DamageTypes.MA)) {
            respawnTime = CONFIG().MaceSmashDeathDuration;
        }*/
        else if (source.is(MAGIC)) {
            respawnTime = CONFIG().MagicDeathDuration;
        }
        else if (source.is(MOB_ATTACK)) {
            respawnTime = CONFIG().MobDeathDuration;
        }
        else if (source.is(MOB_ATTACK_NO_AGGRO)) {
            respawnTime = CONFIG().MobNoAggroDeathDuration;
        }
        else if (source.is(MOB_PROJECTILE)) {
            respawnTime = CONFIG().MobProjectileDeathDuration;
        }
        else if (source.is(ON_FIRE)) {
            respawnTime = CONFIG().OnFireDeathDuration;
        }
        else if (source.is(FELL_OUT_OF_WORLD)) {
            respawnTime = CONFIG().OutOfWorldDeathDuration;
        }
        else if (source.is(PLAYER_ATTACK)) {
            respawnTime = CONFIG().PlayerAttackDeathDuration;
        }
        else if (source.is(PLAYER_EXPLOSION)) {
            respawnTime = CONFIG().PlayerExplosionDeathDuration;
        }
        else if (source.is(SONIC_BOOM)) {
            respawnTime = CONFIG().SonicBoomDeathDuration;
        }
        else if (source.is(SPIT)) {
            respawnTime = CONFIG().SpitDeathDuration;
        }
        else if (source.is(STALAGMITE)) {
            respawnTime = CONFIG().StalagmiteDeathDuration;
        }
        else if (source.is(STARVE)) {
            respawnTime = CONFIG().StarveDeathDuration;
        }
        else if (source.is(STING)) {
            respawnTime = CONFIG().StingDeathDuration;
        }
        else if (source.is(THORNS)) {
            respawnTime = CONFIG().ThornsDeathDuration;
        }
        else if (source.is(THROWN)) {
            respawnTime = CONFIG().ThrownDeathDuration;
        }
        else if (source.is(TRIDENT)) {
            respawnTime = CONFIG().TridentDeathDuration;
        }
        else if (source.is(WIND_CHARGE)) {
            respawnTime = CONFIG().WindChargeDeathDuration;
        }
        else if (source.is(WITHER)) {
            respawnTime = CONFIG().WitherDeathDuration;
        }
        else if (source.is(WITHER_SKULL)) {
            respawnTime = CONFIG().WitherSkullDeathDuration;
        }

        return respawnTime;
    }
}