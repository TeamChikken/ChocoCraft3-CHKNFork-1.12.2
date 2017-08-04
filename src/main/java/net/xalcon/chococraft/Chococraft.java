package net.xalcon.chococraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.xalcon.chococraft.common.CommonProxy;
import net.xalcon.chococraft.common.configuration.ChocoboAbilityConfiguration;
import net.xalcon.chococraft.common.init.ModItems;

@Mod(modid = Chococraft.MODID, version = Chococraft.VERSION)
public class Chococraft
{
    public static final String MODID = "chococraft";
    public static final String VERSION = "@VERSION@";

    private final static Chococraft instance = new Chococraft();

    @SidedProxy(clientSide = "net.xalcon.chococraft.client.ClientProxy", serverSide = "net.xalcon.chococraft.common.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs creativeTab = new CreativeTabs(MODID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModItems.gysahlGreen);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items)
        {
            super.displayAllRelevantItems(items);
            items.add(ModItems.spawneggChocobo);
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
}
