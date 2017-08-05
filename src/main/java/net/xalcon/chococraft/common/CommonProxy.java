package net.xalcon.chococraft.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.common.configuration.ChocoboAbilityConfiguration;
import net.xalcon.chococraft.common.entities.EntityDataSerializers;
import net.xalcon.chococraft.common.init.ModEntities;
import net.xalcon.chococraft.common.network.PacketManager;
import net.xalcon.chococraft.common.world.worldgen.WorldGenGysahlGreen;

public class CommonProxy
{
    public void onPreInit(FMLPreInitializationEvent event)
    {
        ChocoboAbilityConfiguration.loadAbilityInfo();
    }

    public void onInit(FMLInitializationEvent event)
    {
        EntityDataSerializers.init();
        PacketManager.init();

        GameRegistry.registerWorldGenerator(new WorldGenGysahlGreen(), 3); // TODO: Make weight configurable
    }

    public void onPostInit(FMLPostInitializationEvent event)
    {

    }
}
