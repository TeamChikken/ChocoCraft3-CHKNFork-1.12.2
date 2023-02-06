package net.chococraft;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.chococraft.common.ChocoConfig;
import net.chococraft.common.ChococraftGuiHandler;
import net.chococraft.common.CommonProxy;
import net.chococraft.common.commands.CommandChocobo;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.entities.properties.EntityDataSerializers;
import net.chococraft.common.init.ModItems;
import net.chococraft.common.network.PacketManager;
import net.chococraft.common.world.worldgen.WorldGenGysahlGreen;
import net.chococraft.utils.Log4jFilter;

@Mod(modid = Chococraft.MODID, version = Chococraft.VERSION, acceptedMinecraftVersions = Chococraft.MC_VERSION)
public class Chococraft {
    public static final String MODID = "chococraft";
    public static final String VERSION = "@VERSION@";
    public static final String MC_VERSION = "@MC_VERSION@";

    private final static Chococraft instance = new Chococraft();

    public final static Logger log = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "net.chococraft.client.ClientProxy", serverSide = "net.chococraft.common.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs creativeTab = new CreativeTabs(MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.gysahlGreen);
        }
    };

    @Mod.InstanceFactory
    public static Chococraft getInstance() {
        return instance;
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public static void onPreInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ChococraftGuiHandler());
        EntityDataSerializers.init();
        PacketManager.init();
        Log4jFilter.init();

        GameRegistry.registerWorldGenerator(new WorldGenGysahlGreen(), ChocoConfig.world.gysahlGreenSpawnWeight);

        if (ChocoConfig.world.chocoboSpawnWeight == 0) return;

        Set<Biome> biomes = new HashSet<>();

        biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.HILLS));
        biomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER));

        EntityRegistry.addSpawn(EntityChocobo.class, ChocoConfig.world.chocoboSpawnWeight, ChocoConfig.world.chocoboPackSizeMin, ChocoConfig.world.chocoboPackSizeMax, EnumCreatureType.CREATURE, biomes.toArray(new Biome[biomes.size()]));
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public static void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandChocobo());
    }
}
