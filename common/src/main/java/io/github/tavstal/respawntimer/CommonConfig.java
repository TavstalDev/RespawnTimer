package io.github.tavstal.respawntimer;

import io.github.tavstal.respawntimer.models.ConfigField;
import io.github.tavstal.respawntimer.models.EFieldType;
import io.github.tavstal.respawntimer.models.ValueEditor;

public class CommonConfig {
    @ConfigField(comment = "Shows more logs than usual. Helps locating errors.")
    public boolean EnableDebugMode;
    @ConfigField(comment = "Specifies the language used by the application.")
    public String Language;
    @ConfigField(comment = "Messages related to respawning will be sent to the title screen.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public  boolean EnableTitleScreen;
    @ConfigField(comment = "Messages related to respawning will be sent to the chat.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public boolean EnableChatMessages;
    @ConfigField(comment = "Messages related to respawning will be sent to above the action bar.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public boolean EnableActionBarMessage;
    @ConfigField(comment = "Ignores the players who died in creative mode.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public boolean IgnoreCreativePlayers;
    @ConfigField(comment = "Clears the items of the player on death.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public boolean ClearInventory;
    @ConfigField(comment = "Drops the items of the player on death.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public boolean DropInventory;
    @ConfigField(comment = "Allows the player to respawn at their death location if they do not have home.")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public  boolean AllowLocationRespawn;
    @ConfigField(comment = "If the player has bed they will be respawned there, instead of at the world spawn")
    @ValueEditor(type = EFieldType.BOOLEAN)
    public boolean AllowHomeRespawn;
    @ConfigField(comment = "What items should be recognised as totems.")
    @ValueEditor(type = EFieldType.TEXT)
    public String TotemIds;
    //#region Delays
    @ConfigField(comment = "Any kind of arrow hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int ArrowDeathDuration;
    @ConfigField(comment = "Explosion caused by using a bed or respawn anchor in the wrong dimension")
    @ValueEditor(type = EFieldType.NUMBER)
    public int BadRespawnPointDeathDuration;
    @ConfigField(comment = "Touching a cactus")
    @ValueEditor(type = EFieldType.NUMBER)
    public int CactusDeathDuration;
    @ConfigField(comment = "Touching a campfire or soul campfire")
    @ValueEditor(type = EFieldType.NUMBER)
    public int CampfireDeathDuration;
    @ConfigField(comment = "When too many mobs are in one place")
    @ValueEditor(type = EFieldType.NUMBER)
    public int CrammingDeathDuration;
    @ConfigField(comment = "Unused")
    @ValueEditor(type = EFieldType.NUMBER)
    public int DragonBreathDeathDuration;
    @ConfigField(comment = "Snow golems, blazes, endermen, and striders in water/rain")
    @ValueEditor(type = EFieldType.NUMBER)
    public int DrownDeathDuration;
    @ConfigField(comment = "When dolphins and axolotls are out of water for too long")
    @ValueEditor(type = EFieldType.NUMBER)
    public int DryOutDeathDuration;
    @ConfigField(comment = "(1.21.2) Teleporting with an ender pearl")
    @ValueEditor(type = EFieldType.NUMBER)
    public int EnderPearlDeathDuration;
    @ConfigField(comment = "TNT exploding when its fuse runs out")
    @ValueEditor(type = EFieldType.NUMBER)
    public int ExplosionDeathDuration;
    @ConfigField(comment = "Falling too far")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FallDeathDuration;
    @ConfigField(comment = "A falling anvil hits something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FallingAnvilDeathDuration;
    @ConfigField(comment = "A non-anvil, non-dripstone falling block hitting an entity")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FallingBlockDeathDuration;
    @ConfigField(comment = "Falling pointed dripstone hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FallingStalactiteDeathDuration;
    @ConfigField(comment = "A ghast or blaze fireball with an owner hitting an entity directly")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FireballDeathDuration;
    @ConfigField(comment = "An elytra user when their firework explodes")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FireworksDeathDuration;
    @ConfigField(comment = "Colliding with terrain using an elytra")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FlyIntoWallDeathDuration;
    @ConfigField(comment = "Ticking damage while freezing in powder snow")
    @ValueEditor(type = EFieldType.NUMBER)
    public int FreezeDeathDuration;
    @ConfigField(comment = "Simulating the player_hurt_entity trigger when punching an interaction entity")
    @ValueEditor(type = EFieldType.NUMBER)
    public int GenericDeathDuration;
    @ConfigField(comment = "Using /kill")
    @ValueEditor(type = EFieldType.NUMBER)
    public int GenericKillDeathDuration;
    @ConfigField(comment = "Standing on a magma block")
    @ValueEditor(type = EFieldType.NUMBER)
    public int HotFloorDeathDuration;
    @ConfigField(comment = "Ticking damage while standing in a fire or soul fire block")
    @ValueEditor(type = EFieldType.NUMBER)
    public int InFireDeathDuration;
    @ConfigField(comment = "Ticking damage while suffocating")
    @ValueEditor(type = EFieldType.NUMBER)
    public int InWallDeathDuration;
    @ConfigField(comment = "Instant damage from a harming/healing potion (drink, splash, or lingering)")
    @ValueEditor(type = EFieldType.NUMBER)
    public int IndirectMagicDeathDuration;
    @ConfigField(comment = "Ticking damage while in lava")
    @ValueEditor(type = EFieldType.NUMBER)
    public int LavaDeathDuration;
    @ConfigField(comment = "When struck by lightning")
    @ValueEditor(type = EFieldType.NUMBER)
    public int LightningDeathDuration;
    @ConfigField(comment = "When hit by a mace smash attack")
    @ValueEditor(type = EFieldType.NUMBER)
    public int MaceSmashDeathDuration;
    @ConfigField(comment = "Ticking damage from a Harming/Healing effect (i.e. not initial contact)")
    @ValueEditor(type = EFieldType.NUMBER)
    public int MagicDeathDuration;
    @ConfigField(comment = "Melee attack dealt by most mobs")
    @ValueEditor(type = EFieldType.NUMBER)
    public int MobDeathDuration;
    @ConfigField(comment = "A goat ramming")
    @ValueEditor(type = EFieldType.NUMBER)
    public int MobNoAggroDeathDuration;
    @ConfigField(comment = "A shulker bullet hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int MobProjectileDeathDuration;
    @ConfigField(comment = "Ticking damage while fire is wearing off")
    @ValueEditor(type = EFieldType.NUMBER)
    public int OnFireDeathDuration;
    @ConfigField(comment = "Ticking damage in the void")
    @ValueEditor(type = EFieldType.NUMBER)
    public int OutOfWorldDeathDuration;
    @ConfigField(comment = "Ticking damage while outside the world border")
    @ValueEditor(type = EFieldType.NUMBER)
    public int OutsideBorderDeathDuration;
    @ConfigField(comment = "A player hitting a mob")
    @ValueEditor(type = EFieldType.NUMBER)
    public int PlayerAttackDeathDuration;
    @ConfigField(comment = "Anything in explosion, when it can be determined a player was directly responsible")
    @ValueEditor(type = EFieldType.NUMBER)
    public int PlayerExplosionDeathDuration;
    @ConfigField(comment = "The ranged attack of a warden")
    @ValueEditor(type = EFieldType.NUMBER)
    public int SonicBoomDeathDuration;
    @ConfigField(comment = "A llama spitting")
    @ValueEditor(type = EFieldType.NUMBER)
    public int SpitDeathDuration;
    @ConfigField(comment = "Falling too far onto pointed dripstone")
    @ValueEditor(type = EFieldType.NUMBER)
    public int StalagmiteDeathDuration;
    @ConfigField(comment = "Ticking damage from having no food")
    @ValueEditor(type = EFieldType.NUMBER)
    public int StarveDeathDuration;
    @ConfigField(comment = "Melee attack dealt by a bee")
    @ValueEditor(type = EFieldType.NUMBER)
    public int StingDeathDuration;
    @ConfigField(comment = "Ticking damage while moving in a berry bush block")
    @ValueEditor(type = EFieldType.NUMBER)
    public int BushDeathDuration;
    @ConfigField(comment = "Recoil damage from attacking a guardian")
    @ValueEditor(type = EFieldType.NUMBER)
    public int ThornsDeathDuration;
    @ConfigField(comment = "A snowball, egg, or ender pearl hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int ThrownDeathDuration;
    @ConfigField(comment = "A trident hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int TridentDeathDuration;
    @ConfigField(comment = "A wind charge, thrown or fire from a breeze, hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int WindChargeDeathDuration;
    @ConfigField(comment = "Ticking damage from a Wither effect")
    @ValueEditor(type = EFieldType.NUMBER)
    public int WitherDeathDuration;
    @ConfigField(comment = "A wither skull with an owner hitting something")
    @ValueEditor(type = EFieldType.NUMBER)
    public int WitherSkullDeathDuration;
    //#endregion

    @ConfigField(comment = "DO NOT TOUCH THIS. This helps handlig config related changes after updates.")
    public int FileVersion;

    public CommonConfig() {
        EnableDebugMode = false;
        Language = "en";
        EnableTitleScreen = true;
        EnableActionBarMessage = false;
        EnableChatMessages = false;
        IgnoreCreativePlayers = true;
        ClearInventory = false;
        DropInventory = false;
        AllowLocationRespawn = false;
        AllowHomeRespawn = true;

        TotemIds =
        "minecraft:totem_of_undying;totemexpansion:totem_fire;totemexpansion:totem_falling;totemexpansion:totem_breathing;totemexpansion:totem_explosion";

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
