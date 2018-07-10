package net.slayer5934.chococraft.common;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.slayer5934.chococraft.common.entities.EntityChocobo;
import net.slayer5934.chococraft.common.entities.properties.EntityDataSerializers;
import net.slayer5934.chococraft.common.network.PacketManager;
import net.slayer5934.chococraft.common.world.worldgen.WorldGenGysahlGreen;

public class CommonProxy
{
    public void openChocoboInfoGui(EntityChocobo chocobo) { /* NOP */ }
}
