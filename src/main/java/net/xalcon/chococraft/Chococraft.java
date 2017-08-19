package net.xalcon.chococraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.common.ChocoConfig;
import net.xalcon.chococraft.common.ChococraftGuiHandler;
import net.xalcon.chococraft.common.CommonProxy;
import net.xalcon.chococraft.common.commands.CommandChocobo;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.properties.EntityDataSerializers;
import net.xalcon.chococraft.common.init.ModItems;
import net.xalcon.chococraft.common.network.PacketManager;
import net.xalcon.chococraft.common.world.worldgen.WorldGenGysahlGreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Chococraft.MODID, version = Chococraft.VERSION, acceptedMinecraftVersions = Chococraft.MC_VERSION)
public class Chococraft
{
    public static final String MODID = "chococraft";
    public static final String VERSION = "@VERSION@";
    public static final String MC_VERSION = "@MC_VERSION@";

    private final static Chococraft instance = new Chococraft();

    public final static Logger log = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "net.xalcon.chococraft.client.ClientProxy", serverSide = "net.xalcon.chococraft.common.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs creativeTab = new CreativeTabs(MODID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModItems.gysahlGreen);
        }
    };

    @Mod.InstanceFactory
    public static Chococraft getInstance()
    {
        return instance;
    }

    @Mod.EventHandler @SuppressWarnings("unused")
    public static void onPreInit(FMLPreInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ChococraftGuiHandler());
        EntityDataSerializers.init();
        PacketManager.init();

        GameRegistry.registerWorldGenerator(new WorldGenGysahlGreen(), ChocoConfig.world.gysahlGreenSpawnWeight);

        EntityRegistry.addSpawn(EntityChocobo.class,
                ChocoConfig.world.chocoboSpawnWeight,
                ChocoConfig.world.chocoboPackSizeMin,
                ChocoConfig.world.chocoboPackSizeMax,
                EnumCreatureType.CREATURE,
                BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS).toArray(new Biome[0]));
    }

    @Mod.EventHandler @SuppressWarnings("unused")
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandChocobo());
    }
}
