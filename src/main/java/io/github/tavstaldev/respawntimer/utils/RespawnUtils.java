package io.github.tavstaldev.respawntimer.utils;

import io.github.tavstaldev.minecorelib.core.PluginLogger;
import io.github.tavstaldev.respawntimer.RespawnTimer;
import org.bukkit.*;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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
    @SuppressWarnings("UnstableApiUsage")
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
            playSound(player, RespawnTimer.GetConfig().getString("sounds.death"));

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
        try {
            if (!isTemporal)
                _deadPlayers.remove(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
            player.clearActivePotionEffects();

            if (RespawnTimer.GetConfig().getBoolean("allowHomeRespawn")) {
                if (player.getRespawnLocation() != null) {
                    player.teleport(player.getRespawnLocation());
                    playSound(player, RespawnTimer.GetConfig().getString("sounds.respawn"));
                    return;
                }
            }
            World overworld = Bukkit.getServer().getWorld("world");
            if (overworld != null) {
                player.teleport(overworld.getSpawnLocation());
            }
            playSound(player, RespawnTimer.GetConfig().getString("sounds.respawn"));
        }
        catch (Exception ex) {
            _logger.Error("Error during respawning player:");
            _logger.Error(ex.getMessage());
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
        try {
            var damageType = source.getDamageType();
            var config = RespawnTimer.GetConfig();

            var damageTypeName = damageType.getKey().value();
            switch (damageTypeName.toLowerCase()) {
                case "arrow": {
                    respawnTime = config.getInt("durations.arrowDeathDuration");
                    break;
                }
                case "bad_respawn_point": {
                    respawnTime = config.getInt("durations.badRespawnPointDeathDuration");
                    break;
                }
                case "sweet_berry_bush": {
                    respawnTime = config.getInt("durations.bushDeathDuration");
                    break;
                }
                case "cactus": {
                    respawnTime = config.getInt("durations.cactusDeathDuration");
                    break;
                }
                case "campfire": {
                    respawnTime = config.getInt("durations.campfireDeathDuration");
                    break;
                }
                case "cramming": {
                    respawnTime = config.getInt("durations.crammingDeathDuration");
                    break;
                }
                case "dragon_breath": {
                    respawnTime = config.getInt("durations.dragonBreathDeathDuration");
                    break;
                }
                case "drown": {
                    respawnTime = config.getInt("durations.drownDeathDuration");
                    break;
                }
                case "dry_out": {
                    respawnTime = config.getInt("durations.dryOutDeathDuration");
                    break;
                }
                case "ender_pearl": {
                    respawnTime = config.getInt("durations.enderPearlDeathDuration");
                    break;
                }
                case "explosion": {
                    respawnTime = config.getInt("durations.explosionDeathDuration");
                    break;
                }
                case "fall": {
                    respawnTime = config.getInt("durations.fallDeathDuration");
                    break;
                }
                case "falling_block": {
                    respawnTime = config.getInt("durations.fallingAnvilDeathDuration");
                    break;
                }
                case "falling_stalactite": {
                    respawnTime = config.getInt("durations.fallingStalactiteDeathDuration");
                    break;
                }
                case "fireball":
                case "unattributed_fireball":  // Unattributed fireballs are used by some entities like ghasts
                {
                    respawnTime = config.getInt("durations.fireballDeathDuration");
                    break;
                }
                case "fireworks": {
                    respawnTime = config.getInt("durations.fireworksDeathDuration");
                    break;
                }
                case "fly_into_wall": {
                    respawnTime = config.getInt("durations.flyIntoWallDeathDuration");
                    break;
                }
                case "freeze": {
                    respawnTime = config.getInt("durations.freezeDeathDuration");
                    break;
                }
                case "generic": {
                    respawnTime = config.getInt("durations.genericDeathDuration");
                    break;
                }
                case "generic_kill": {
                    respawnTime = config.getInt("durations.genericKillDeathDuration");
                    break;
                }
                case "hot_floor": {
                    respawnTime = config.getInt("durations.hotFloorDeathDuration");
                    break;
                }
                case "indirect_magic": {
                    respawnTime = config.getInt("durations.indirectMagicDeathDuration");
                    break;
                }
                case "in_fire": {
                    respawnTime = config.getInt("durations.inFireDeathDuration");
                    break;
                }
                case "in_wall": {
                    respawnTime = config.getInt("durations.inWallDeathDuration");
                    break;
                }
                case "lava": {
                    respawnTime = config.getInt("durations.lavaDeathDuration");
                    break;
                }
                case "lightning_bolt": {
                    respawnTime = config.getInt("durations.lightningDeathDuration");
                    break;
                }
                case "mace_smash": {
                    respawnTime = config.getInt("durations.maceSmashDeathDuration");
                    break;
                }
                case "magic": {
                    respawnTime = config.getInt("durations.magicDeathDuration");
                    break;
                }
                case "mob_attack": {
                    respawnTime = config.getInt("durations.mobDeathDuration");
                    break;
                }
                case "mob_attack_no_aggro": {
                    respawnTime = config.getInt("durations.mobNoAggroDeathDuration");
                    break;
                }
                case "mob_projectile": {
                    respawnTime = config.getInt("durations.mobProjectileDeathDuration");
                    break;
                }
                case "on_fire": {
                    respawnTime = config.getInt("durations.onFireDeathDuration");
                    break;
                }
                case "out_of_world": {
                    respawnTime = config.getInt("durations.outOfWorldDeathDuration");
                    break;
                }
                case "player_attack": {
                    respawnTime = config.getInt("durations.playerAttackDeathDuration");
                    break;
                }
                case "player_explosion": {
                    respawnTime = config.getInt("durations.playerExplosionDeathDuration");
                    break;
                }
                case "sonic_boom": {
                    respawnTime = config.getInt("durations.sonicBoomDeathDuration");
                    break;
                }
                case "spit": {
                    respawnTime = config.getInt("durations.spitDeathDuration");
                    break;
                }
                case "stalagmite": {
                    respawnTime = config.getInt("durations.stalagmiteDeathDuration");
                    break;
                }
                case "starve": {
                    respawnTime = config.getInt("durations.starveDeathDuration");
                    break;
                }
                case "sting": {
                    respawnTime = config.getInt("durations.stingDeathDuration");
                    break;
                }
                case "thorns": {
                    respawnTime = config.getInt("durations.thornsDeathDuration");
                    break;
                }
                case "thrown": {
                    respawnTime = config.getInt("durations.thrownDeathDuration");
                    break;
                }
                case "trident": {
                    respawnTime = config.getInt("durations.tridentDeathDuration");
                    break;
                }
                case "wind_charge": {
                    respawnTime = config.getInt("durations.windChargeDeathDuration");
                    break;
                }
                case "wither": {
                    respawnTime = config.getInt("durations.witherDeathDuration");
                    break;
                }
                case "wither_skull": {
                    respawnTime = config.getInt("durations.witherSkullDeathDuration");
                    break;
                }
                default:
                {
                    _logger.Warn("Unknown damage type: " + damageTypeName + ". Using default respawn time.");
                    break;
                }
            }
        }
        catch (Exception ex) {
            _logger.Error("Error during getting respawn time:");
            _logger.Error(ex.getMessage());
        }

        return respawnTime;
    }

    /**
     * Plays a sound for the specified player.
     *
     * @param player The player for whom the sound will be played. Must not be null.
     * @param key The key of the sound to play. If null, empty, or "none" (case-insensitive), no sound will be played.
     * <br/>
     * The method attempts to retrieve the sound using the provided key and plays it for the player.
     * If the sound is not found, a warning is logged. Any exceptions during execution are caught and logged as errors.
     */
    public static void playSound(@NotNull Player player, String key) {
        try {
            // Check if the key is null, empty, or "none", and return early if so
            if (key == null || key.isEmpty() || "none".equalsIgnoreCase(key)) {
                _logger.Debug("No sound key provided or sound is set to 'none'. Not playing any sound.");
                return;
            }

            // Log debug information about the player for whom the sound is being played
            _logger.Debug("Playing sound for player: " + player.getName());

            // Retrieve the sound object using the provided key
            Sound sound = getSound(key);

            // If the sound is found, play it for the player; otherwise, log a warning
            if (sound != null) {
                player.playSound(
                        player.getLocation(),  // location of the sound
                        sound,
                        1.0f,
                        1.0f
                );
            } else {
                _logger.Warn("Sound not found: " + key);
            }
        } catch (Exception ex) {
            // Log any exceptions that occur during the execution of the method
            _logger.Error("Error during playing sound:");
            _logger.Error(ex.getMessage());
        }
    }

    /**
     * Gets the sound based on the provided name.
     *
     * @param name The name of the sound.
     * @return The Sound object.
     */
    public static Sound getSound(@NotNull String name) {
        String key = name.toLowerCase(Locale.ROOT);
        // Fixes null pointer exception
        if ("none".equalsIgnoreCase(key))
            return null;

        var namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null) {
            _logger.Warn("Invalid sound name: " + key);
            return null;
        }

        return Registry.SOUNDS.get(namespacedKey);
    }
}
