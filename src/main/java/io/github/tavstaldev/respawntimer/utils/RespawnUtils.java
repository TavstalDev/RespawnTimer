package io.github.tavstaldev.respawntimer.utils;

import io.github.tavstaldev.minecorelib.core.PluginLogger;
import io.github.tavstaldev.respawntimer.RespawnTimer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.intellij.lang.annotations.Subst;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.bukkit.damage.DamageType.*;

/**
 * Utility class for handling player respawn logic.
 */
public class RespawnUtils {
    private static final PluginLogger _logger = RespawnTimer.Logger().WithModule(RespawnUtils.class);
    
    private final static Map<UUID, LocalDateTime> _deadPlayers = new HashMap<>();

    /**
     * Gets the list of dead players and their death times.
     *
     * @return Dictionary of player UUIDs and their death times.
     */
    public static Map<UUID, LocalDateTime> GetPlayerDataList() {
        return _deadPlayers;
    }

    /**
     * Checks if a player is dead.
     *
     * @param uuid The UUID of the player.
     * @return True if the player is dead, false otherwise.
     */
    public static boolean IsPlayerDead(UUID uuid) {
        return _deadPlayers.get(uuid) != null;
    }

    /**
     * Sets a player as dead and applies necessary effects.
     *
     * @param player The player to set as dead.
     * @param source The source of the damage that killed the player.
     */
    public static void SetPlayerDead(Player player, DamageSource source) {
        try {
            _logger.Debug("Setting player as dead: " + player.getName());
            _logger.Debug("Setting minecraft parameters...");
            player.setGameMode(GameMode.SPECTATOR);
            player.setHealth(20);
            player.clearActivePotionEffects();
            player.setFoodLevel(20);
            player.setSaturation(5F);
            player.setFireTicks(0);
            try {
                _logger.Debug("Playing death sound...");
                player.playSound(getSound(RespawnTimer.GetConfig().getString("sounds.deathSound")));
            } catch (Exception ex) {
                _logger.Debug("Error during playing death sound:");
                _logger.Debug(ex.getMessage());
            }

            // If the player is already dead, update the death time
            if (IsPlayerDead(player.getUniqueId())) {
                long duration = Duration.between(LocalDateTime.now(), _deadPlayers.get(player.getUniqueId())).getSeconds();
                PotionEffect blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, (int) duration * 20, 1);
                player.addPotionEffect(blindnessEffect);
                return;
            }

            _logger.Debug("Getting respawn time...");
            long respawnTime = GetRespawnTime(source);
            _logger.Debug("Calculating duration...");
            long duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(respawnTime)).getSeconds();
            PotionEffect blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, (int) duration * 20, 1);
            _logger.Debug("Adding blindness effect...");
            player.addPotionEffect(blindnessEffect);
            _logger.Debug("Setting player statistics...");
            player.setStatistic(Statistic.DEATHS, player.getStatistic(Statistic.DEATHS) + 1);
            player.setStatistic(Statistic.TIME_SINCE_REST, 0);
            player.setStatistic(Statistic.TIME_SINCE_DEATH, 0);
            _logger.Debug("Updating dead players...");
            _deadPlayers.put(player.getUniqueId(), LocalDateTime.now().plusSeconds(respawnTime));
        }
        catch (Exception ex) {
            _logger.Error("Error during setting player as dead:");
            _logger.Error(ex.getMessage());
        }
    }

    /**
     * Respawns a player at their respawn location or the world spawn location.
     *
     * @param player The player to respawn.
     * @param isTemporal If true, the player is temporarily respawned.
     */
    public static void RespawnPlayer(Player player, boolean isTemporal) {
        if (!isTemporal)
            _deadPlayers.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
        player.clearActivePotionEffects();

        if (RespawnTimer.GetConfig().getBoolean("allowHomeRespawn")) {
            if (player.getRespawnLocation() != null) {
                player.teleport(player.getRespawnLocation());
                return;
            }
        }
        World overworld = Bukkit.getServer().getWorld("world");
        if (overworld != null) {
            player.teleport(overworld.getSpawnLocation());
        }
    }

    /**
     * Gets the respawn time based on the damage source.
     *
     * @param source The source of the damage.
     * @return The respawn time in seconds.
     */
    @SuppressWarnings("UnstableApiUsage")
    private static long GetRespawnTime(DamageSource source) {
        int respawnTime = 60;
        var damageType = source.getDamageType();

        if (damageType == ARROW) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.arrowDeathDuration");
        }
        else if (damageType == BAD_RESPAWN_POINT) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.badRespawnPointDeathDuration");
        }
        else if (damageType == SWEET_BERRY_BUSH) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.bushDeathDuration");
        }
        else if (damageType == CACTUS) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.cactusDeathDuration");
        }
        else if (damageType == CAMPFIRE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.campfireDeathDuration");
        }
        else if (damageType == CRAMMING) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.crammingDeathDuration");
        }
        else if (damageType == DRAGON_BREATH) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.dragonBreathDeathDuration");
        }
        else if (damageType == DROWN) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.drownDeathDuration");
        }
        else if (damageType == DRY_OUT) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.dryOutDeathDuration");
        }
        else if (damageType == ENDER_PEARL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.enderPearlDeathDuration");
        }
        else if (damageType == EXPLOSION) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.explosionDeathDuration");
        }
        else if (damageType == FALL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.fallDeathDuration");
        }
        else if (damageType == FALLING_BLOCK) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.fallingAnvilDeathDuration");
        }
        else if (damageType == FALLING_STALACTITE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.fallingStalactiteDeathDuration");
        }
        else if (damageType == FIREBALL || damageType == UNATTRIBUTED_FIREBALL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.fireballDeathDuration");
        }
        else if (damageType == FIREWORKS) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.fireworksDeathDuration");
        }
        else if (damageType == FLY_INTO_WALL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.flyIntoWallDeathDuration");
        }
        else if (damageType == FREEZE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.freezeDeathDuration");
        }
        else if (damageType == GENERIC) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.genericDeathDuration");
        }
        else if (damageType == GENERIC_KILL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.genericKillDeathDuration");
        }
        else if (damageType == HOT_FLOOR) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.hotFloorDeathDuration");
        }
        else if (damageType == INDIRECT_MAGIC) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.indirectMagicDeathDuration");
        }
        else if (damageType == IN_FIRE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.inFireDeathDuration");
        }
        else if (damageType == IN_WALL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.inWallDeathDuration");
        }
        else if (damageType == LAVA) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.lavaDeathDuration");
        }
        else if (damageType == LIGHTNING_BOLT) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.lightningDeathDuration");
        }
        else if (damageType == MACE_SMASH) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.maceSmashDeathDuration");
        }
        else if (damageType == MAGIC) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.magicDeathDuration");
        }
        else if (damageType == MOB_ATTACK) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.mobDeathDuration");
        }
        else if (damageType == MOB_ATTACK_NO_AGGRO) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.mobNoAggroDeathDuration");
        }
        else if (damageType == MOB_PROJECTILE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.mobProjectileDeathDuration");
        }
        else if (damageType == ON_FIRE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.onFireDeathDuration");
        }
        else if (damageType == OUT_OF_WORLD) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.outOfWorldDeathDuration");
        }
        else if (damageType == PLAYER_ATTACK) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.playerAttackDeathDuration");
        }
        else if (damageType == PLAYER_EXPLOSION) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.playerExplosionDeathDuration");
        }
        else if (damageType == SONIC_BOOM) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.sonicBoomDeathDuration");
        }
        else if (damageType == SPIT) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.spitDeathDuration");
        }
        else if (damageType == STALAGMITE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.stalagmiteDeathDuration");
        }
        else if (damageType == STARVE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.starveDeathDuration");
        }
        else if (damageType == STING) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.stingDeathDuration");
        }
        else if (damageType == THORNS) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.thornsDeathDuration");
        }
        else if (damageType == THROWN) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.thrownDeathDuration");
        }
        else if (damageType == TRIDENT) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.tridentDeathDuration");
        }
        else if (damageType == WIND_CHARGE) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.windChargeDeathDuration");
        }
        else if (damageType == WITHER) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.witherDeathDuration");
        }
        else if (damageType == WITHER_SKULL) {
            respawnTime = RespawnTimer.GetConfig().getInt("durations.witherSkullDeathDuration");
        }

        return respawnTime;
    }

    /**
     * Gets the sound based on the provided name.
     *
     * @param name The name of the sound.
     * @return The Sound object.
     */
    public static Sound getSound(@Subst("") String name) {
        // Fixes null pointer exception
        if ("none".equalsIgnoreCase(name))
            return null;

        String key = name == null ? "minecraft:block.note_block.harp" : "minecraft:" + name;
        return Sound.sound(Key.key(key), Sound.Source.PLAYER, 1.0f, 1.0f);
    }
}
