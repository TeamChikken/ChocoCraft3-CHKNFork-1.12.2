package net.slayer5934.chococraft.common;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.slayer5934.chococraft.Chococraft;

//@Config(modid = Chococraft.MODID)
@Mod.EventBusSubscriber(modid = Chococraft.MODID)
@Config(modid = Chococraft.MODID)
public class ChocoConfig
{
    @Config.Comment({ "World generation related configuration" })
    @Config.Name("world")
    public static WorldConfig world = new WorldConfig();
    @Config.Name("chocobo")
    public static ChocoboConfig chocobo = new ChocoboConfig();
    @Config.Name("breeding")
    public static BreedingConfig breeding = new BreedingConfig();

    public static class WorldConfig
    {
        @Config.RangeInt(min = 0)
        @Config.Comment("Controls the weight compared to other world gen")
        @Config.RequiresMcRestart
        public int gysahlGreenSpawnWeight = 3;

        @Config.RangeInt(min = 0)
        public int gysahlGreenPatchSize = 64;

        @Config.RangeDouble(min = 0, max = 1)
        public double gysahlGreenSpawnChance = 0.1;
        
        public boolean gysahlGreensSpawnOnlyInOverworld = true;

        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int chocoboSpawnWeight = 10;

        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int chocoboPackSizeMin = 1;

        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int chocoboPackSizeMax = 3;

        @Config.RequiresMcRestart
        public int retrogenId = 1;

        public boolean addAbilityFruitsToDungeonLoot = true;
        @Config.Comment("This number controls the weight ability fruit has compared to other items in a loot table.")
        @Config.RangeInt(min = 0)
        public int abilityFruitDungeonLootWeight = 5;
    }

    public static class ChocoboConfig
    {
    	@Config.Comment("This multiplier controls the tame chance per gysahl used, so .15 results in 15% chance to tame.")
        @Config.RangeDouble(min = 0, max = 1)
        public double tameChance = 0.15;

        @Config.RangeDouble(min = 0)
        public float sprintStaminaCost = 0.01f;
        @Config.RangeDouble(min = 0)
        public float glideStaminaCost = 0.00f;
        @Config.Comment("This float controls the amount of stamina consumed per tick by each ability.")
        @Config.RangeDouble(min = 0)
        public float flyStaminaCost = 0.05f;
        @Config.RangeDouble(min = 0)
        public float jumpStaminaCost = 0.00f;
        @Config.Comment("This float controls the amount of stamina recharged per tick.")
        @Config.RangeDouble(min = 0)
        public float staminaRegenRate = 0.025f;

        @Config.RangeInt(min = 0)
        public int defaultStamina = 10;
        @Config.RangeInt(min = 0)
        public int defaultSpeed = 20;
        @Config.Comment("This number controls the default stats a chocobo spawns with.")
        @Config.RangeInt(min = 0)
        public int defaultHealth = 20;
        
        @Config.RangeInt(min = 1)
        public int ExpCostSprint = 1500;
        @Config.RangeInt(min = 1)
        public int ExpCostGlide = 1500;
        @Config.Comment("This number controls the amount of exp required to unlock abilities.")
        @Config.RangeInt(min = 1)
        public int ExpCostDive = 1000;
        @Config.RangeInt(min = 1)
        public int ExpCostFly = 2000;
    }

    public static class BreedingConfig
    {
    	@Config.Comment("This number controls the max stats a chocobo can have.")
        @Config.RangeInt(min = 0)
        public int maxHealth = 50;
        @Config.RangeInt(min = 0)
        public int maxSpeed = 35;
        @Config.RangeInt(min = 0)
        public float maxStamina = 25;
        
        @Config.Comment("This multiplier controls the max stat gain, for example 0.05 would result in a max gain of 5% so 20 to 21.")
        @Config.RangeDouble(min = 0)
        public float posgainHealth = .1f;
        @Config.RangeDouble(min = 0)
        public float posgainSpeed = .1f;
        @Config.RangeDouble(min = 0)
        public float posgainStamina = .1f;
        
        @Config.Comment("This multiplier controls the max stat loss, for example 0.95 would result in a max loss of 5% so 20 to 19.")
        @Config.RangeDouble(min = 0)
        public float poslossHealth = .95f;
        @Config.RangeDouble(min = 0)
        public float poslossSpeed = .95f;
        @Config.RangeDouble(min = 0)
        public float poslossStamina = .95f;
        
        @Config.Comment("This number controls the amount of ticks / time till an egg hatches. This value isnt super accurate.")
        @Config.RangeInt(min = 0)
        public int eggHatchTimeTicks = 10000;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Chococraft.MODID))
        {
            if(world.chocoboPackSizeMin > world.chocoboPackSizeMax)
            {
                int t = world.chocoboPackSizeMax;
                world.chocoboPackSizeMax = world.chocoboPackSizeMin;
                world.chocoboPackSizeMin = t;
            }
            ConfigManager.sync(Chococraft.MODID, Config.Type.INSTANCE);
        }
    }
}
