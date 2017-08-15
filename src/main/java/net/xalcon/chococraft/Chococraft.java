package net.xalcon.chococraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.xalcon.chococraft.common.CommonProxy;
import net.xalcon.chococraft.common.commands.CommandChocobo;
import net.xalcon.chococraft.common.init.ModItems;
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

    @Mod.EventHandler
    public static void onPreInit(FMLPreInitializationEvent event)
    {
        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public static void onInit(FMLInitializationEvent event)
    {
        proxy.onInit(event);
    }

    @Mod.EventHandler
    public static void onPostInit(FMLPostInitializationEvent event)
    {
        proxy.onPostInit(event);
    }

    @Mod.EventHandler
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandChocobo());
    }
}
