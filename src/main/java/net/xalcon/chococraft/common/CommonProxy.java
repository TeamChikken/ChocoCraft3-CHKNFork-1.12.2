package net.xalcon.chococraft.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.xalcon.chococraft.common.configuration.ChocoboAbilityConfiguration;
import net.xalcon.chococraft.common.network.PacketManager;

public class CommonProxy
{
    public void onPreInit(FMLPreInitializationEvent event)
    {
        ChocoboAbilityConfiguration.loadAbilityInfo();
    }

    public void onInit(FMLInitializationEvent event)
    {
        PacketManager.init();
    }

    public void onPostInit(FMLPostInitializationEvent event)
    {

    }
}
