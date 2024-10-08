package io.github.tavstal.respawntimer;

import io.github.tavstal.respawntimer.models.ConfigField;

public class CommonConfig {
    @ConfigField(order = 1, comment = "Shows more logs than usual. Helps locating errors.")
    public boolean EnableDebugMode;
    @ConfigField(order = 2, comment = "Messages related to respawning will be sent to the title screen.")
    public  boolean EnableTitleScreen;
    @ConfigField(order = 3, comment = "Messages related to respawning will be sent to the chat.")
    public boolean EnableChatMessages;
    @ConfigField(order = 4, comment = "Messages related to respawning will be sent to above the action bar.")
    public boolean EnableActionBarMessage;
    @ConfigField(order = 5, comment = "Ignores the players who died in creative mode.")
    public boolean IgnoreCreativePlayers;
    @ConfigField(order = 6, comment = "Clears the items of the player on death.")
    public boolean ClearInventory;
    @ConfigField(order = 7, comment = "Drops the items of the player on death.")
    public boolean DropInventory;
    @ConfigField(order = 8, comment = "Allows the player to respawn at their death location if they do not have home.")
    public  boolean AllowLocationRespawn;
    @ConfigField(order = 9, comment = "If the player has bed they will be respawned there, instead of at the world spawn")
    public boolean AllowHomeRespawn;

    //#region Messages
    @ConfigField(order = 10, comment = "")
    public String DiedText;
    @ConfigField(order = 11, comment = "")
    public String RespawnTimerMessage;
    @ConfigField(order = 12, comment = "")
    public String RespawnMessage;

    //#region Delays
    @ConfigField(order = 13, comment = "Any kind of arrow hitting something")
    public int ArrowDeathDuration;
    @ConfigField(order = 14, comment = "Explosion caused by using a bed or respawn anchor in the wrong dimension")
    public int BadRespawnPointDeathDuration;
    @ConfigField(order = 15, comment = "Touching a cactus")
    public int CactusDeathDuration;
    @ConfigField(order = 16, comment = "Touching a campfire or soul campfire")
    public int CampfireDeathDuration;
    @ConfigField(order = 17, comment = "When too many mobs are in one place")
    public int CrammingDeathDuration;
    @ConfigField(order = 18, comment = "Unused")
    public int DragonBreathDeathDuration;
    @ConfigField(order = 19, comment = "Snow golems, blazes, endermen, and striders in water/rain")
    public int DrownDeathDuration;
    @ConfigField(order = 20, comment = "When dolphins and axolotls are out of water for too long")
    public int DryOutDeathDuration;
    @ConfigField(order = 21, comment = "(1.21.2) Teleporting with an ender pearl")
    public int EnderPearlDeathDuration;
    @ConfigField(order = 22, comment = "TNT exploding when its fuse runs out")
    public int ExplosionDeathDuration;
    @ConfigField(order = 23, comment = "Falling too far")
    public int FallDeathDuration;
    @ConfigField(order = 24, comment = "A falling anvil hits something")
    public int FallingAnvilDeathDuration;
    @ConfigField(order = 25, comment = "A non-anvil, non-dripstone falling block hitting an entity")
    public int FallingBlockDeathDuration;
    @ConfigField(order = 26, comment = "Falling pointed dripstone hitting something")
    public int FallingStalactiteDeathDuration;
    @ConfigField(order = 27, comment = "A ghast or blaze fireball with an owner hitting an entity directly")
    public int FireballDeathDuration;
    @ConfigField(order = 28, comment = "An elytra user when their firework explodes")
    public int FireworksDeathDuration;
    @ConfigField(order = 29, comment = "Colliding with terrain using an elytra")
    public int FlyIntoWallDeathDuration;
    @ConfigField(order = 30, comment = "Ticking damage while freezing in powder snow")
    public int FreezeDeathDuration;
    @ConfigField(order = 31, comment = "Simulating the player_hurt_entity trigger when punching an interaction entity")
    public int GenericDeathDuration;
    @ConfigField(order = 32, comment = "Using /kill")
    public int GenericKillDeathDuration;
    @ConfigField(order = 33, comment = "Standing on a magma block")
    public int HotFloorDeathDuration;
    @ConfigField(order = 34, comment = "Ticking damage while standing in a fire or soul fire block")
    public int InFireDeathDuration;
    @ConfigField(order = 35, comment = "Ticking damage while suffocating")
    public int InWallDeathDuration;
    @ConfigField(order = 36, comment = "Instant damage from a harming/healing potion (drink, splash, or lingering)")
    public int IndirectMagicDeathDuration;
    @ConfigField(order = 37, comment = "Ticking damage while in lava")
    public int LavaDeathDuration;
    @ConfigField(order = 38, comment = "When struck by lightning")
    public int LightningDeathDuration;
    @ConfigField(order = 39, comment = "When hit by a mace smash attack")
    public int MaceSmashDeathDuration;
    @ConfigField(order = 40, comment = "Ticking damage from a Harming/Healing effect (i.e. not initial contact)")
    public int MagicDeathDuration;
    @ConfigField(order = 41, comment = "Melee attack dealt by most mobs")
    public int MobDeathDuration;
    @ConfigField(order = 42, comment = "A goat ramming")
    public int MobNoAggroDeathDuration;
    @ConfigField(order = 43, comment = "A shulker bullet hitting something")
    public int MobProjectileDeathDuration;
    @ConfigField(order = 44, comment = "Ticking damage while fire is wearing off")
    public int OnFireDeathDuration;
    @ConfigField(order = 45, comment = "Ticking damage in the void")
    public int OutOfWorldDeathDuration;
    @ConfigField(order = 46, comment = "Ticking damage while outside the world border")
    public int OutsideBorderDeathDuration;
    @ConfigField(order = 47, comment = "A player hitting a mob")
    public int PlayerAttackDeathDuration;
    @ConfigField(order = 48, comment = "Anything in explosion, when it can be determined a player was directly responsible")
    public int PlayerExplosionDeathDuration;
    @ConfigField(order = 49, comment = "The ranged attack of a warden")
    public int SonicBoomDeathDuration;
    @ConfigField(order = 50, comment = "A llama spitting")
    public int SpitDeathDuration;
    @ConfigField(order = 51, comment = "Falling too far onto pointed dripstone")
    public int StalagmiteDeathDuration;
    @ConfigField(order = 52, comment = "Ticking damage from having no food")
    public int StarveDeathDuration;
    @ConfigField(order = 53, comment = "Melee attack dealt by a bee")
    public int StingDeathDuration;
    @ConfigField(order = 54, comment = "Ticking damage while moving in a berry bush block")
    public int BushDeathDuration;
    @ConfigField(order = 55, comment = "Recoil damage from attacking a guardian")
    public int ThornsDeathDuration;
    @ConfigField(order = 56, comment = "A snowball, egg, or ender pearl hitting something")
    public int ThrownDeathDuration;
    @ConfigField(order = 57, comment = "A trident hitting something")
    public int TridentDeathDuration;
    @ConfigField(order = 58, comment = "A wind charge, thrown or fire from a breeze, hitting something")
    public int WindChargeDeathDuration;
    @ConfigField(order = 59, comment = "Ticking damage from a Wither effect")
    public int WitherDeathDuration;
    @ConfigField(order = 60, comment = "A wither skull with an owner hitting something")
    public int WitherSkullDeathDuration;
    //#endregion

    //#endregion

    @ConfigField(order = 100, comment = "DO NOT TOUCH THIS. This helps handlig config related changes after updates.")
    public int FileVersion;

    public CommonConfig() {
        EnableDebugMode = false;
        EnableTitleScreen = true;
        EnableActionBarMessage = false;
        EnableChatMessages = false;
        IgnoreCreativePlayers = true;
        ClearInventory = false;
        DropInventory = false;
        AllowLocationRespawn = false;
        AllowHomeRespawn = true;

        DiedText = "§cYou died.";
        RespawnTimerMessage = "§cYou will respawn in {0}s.";
        RespawnMessage = "§aYou have    respawned.";

        ArrowDeathDuration = 120;
        BadRespawnPointDeathDuration = 10;
        CactusDeathDuration = 300;
        CampfireDeathDuration = 300;
        CrammingDeathDuration = 20;
        DragonBreathDeathDuration = 120;
        DrownDeathDuration = 300;
        DryOutDeathDuration = 300;
        EnderPearlDeathDuration = 180;
        ExplosionDeathDuration = 120;
        FallDeathDuration = 240;
        FallingAnvilDeathDuration = 300;
        FallingBlockDeathDuration = 300;
        FallingStalactiteDeathDuration = 300;
        FireballDeathDuration = 120;
        FireworksDeathDuration = 180;
        FlyIntoWallDeathDuration = 300;
        FreezeDeathDuration = 120;
        GenericDeathDuration = 60;
        GenericKillDeathDuration = 20;
        HotFloorDeathDuration = 120;
        InFireDeathDuration = 120;
        InWallDeathDuration = 240;
        IndirectMagicDeathDuration = 240;
        LavaDeathDuration = 300;
        LightningDeathDuration = 60;
        MaceSmashDeathDuration = 60;
        MagicDeathDuration = 300;
        MobDeathDuration = 60;
        MobNoAggroDeathDuration = 30;
        MobProjectileDeathDuration = 120;
        OnFireDeathDuration = 120;
        OutOfWorldDeathDuration = 60;
        OutsideBorderDeathDuration = 30;
        PlayerAttackDeathDuration = 30;
        PlayerExplosionDeathDuration = 30;
        SonicBoomDeathDuration = 60;
        SpitDeathDuration = 60;
        StalagmiteDeathDuration = 60;
        StarveDeathDuration = 300;
        StingDeathDuration = 120;
        BushDeathDuration = 120;
        ThornsDeathDuration = 90;
        ThrownDeathDuration = 60;
        TridentDeathDuration = 60;
        WindChargeDeathDuration = 240;
        WitherDeathDuration = 120;
        WitherSkullDeathDuration = 120;

        FileVersion = 1;
    }
}
