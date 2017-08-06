package net.xalcon.chococraft.common;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.common.configuration.ChocoboAbilityConfiguration;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.EntityDataSerializers;
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

        // TODO: Make configurable
        EntityRegistry.addSpawn(EntityChocobo.class, 10, 1, 3, EnumCreatureType.CREATURE, BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS).toArray(new Biome[0]));
    }

    public void onPostInit(FMLPostInitializationEvent event)
    {

    }
}
