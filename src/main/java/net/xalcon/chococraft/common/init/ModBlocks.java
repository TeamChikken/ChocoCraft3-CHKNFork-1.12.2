package net.xalcon.chococraft.common.init;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.blocks.BlockGysahlGreen;
import net.xalcon.chococraft.utils.inject.ClassInjector;

import java.lang.reflect.Field;

@GameRegistry.ObjectHolder(Chococraft.MODID)
@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class ModBlocks
{
	@GameRegistry.ObjectHolder("gysahl_green")
	public static BlockGysahlGreen gysahlGreen;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for(Field field : ModBlocks.class.getDeclaredFields())
		{
			if(!Block.class.isAssignableFrom(field.getType())) continue;

			GameRegistry.ObjectHolder objHolder = field.getAnnotation(GameRegistry.ObjectHolder.class);
			if(objHolder == null) continue;

			Block block = ClassInjector.createFromField(field);
			String internalName = objHolder.value();
			block.setRegistryName(internalName);
			block.setUnlocalizedName(Chococraft.MODID + "." + internalName);
			block.setCreativeTab(Chococraft.creativeTab);
			event.getRegistry().register(block);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerItems(RegistryEvent.Register<Block> event)
	{
		for(Field field : ModBlocks.class.getDeclaredFields())
		{
			if(Block.class.isAssignableFrom(field.getType())) continue;

			GameRegistry.ObjectHolder objHolder = field.getAnnotation(GameRegistry.ObjectHolder.class);
			if(objHolder == null) continue;

			Block block = ClassInjector.getOrNull(field);

		}
	}
}
