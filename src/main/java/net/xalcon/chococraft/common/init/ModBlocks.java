package net.xalcon.chococraft.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.blocks.BlockChocoboEgg;
import net.xalcon.chococraft.common.blocks.BlockGysahlGreen;
import net.xalcon.chococraft.common.blocks.BlockStrawNest;
import net.xalcon.chococraft.utils.inject.AttachedTileEntity;
import net.xalcon.chococraft.utils.inject.ClassInjector;
import net.xalcon.chococraft.utils.registration.IItemBlockProvider;
import net.xalcon.chococraft.utils.registration.IItemModelProvider;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(Chococraft.MODID)
@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class ModBlocks
{
	@GameRegistry.ObjectHolder("gysahl_green")
	public static BlockGysahlGreen gysahlGreen;

	@GameRegistry.ObjectHolder("straw_nest")
    public static BlockStrawNest strawNest;

    @GameRegistry.ObjectHolder("chocobo_egg")
    public static BlockChocoboEgg chocoboEgg;

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

			AttachedTileEntity attachedTileEntity = field.getType().getAnnotation(AttachedTileEntity.class);
			if(attachedTileEntity != null)
				GameRegistry.registerTileEntity(attachedTileEntity.tile(), Chococraft.MODID + ":" + attachedTileEntity.name());
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		for(Field field : ModBlocks.class.getDeclaredFields())
		{
			if(!Block.class.isAssignableFrom(field.getType())) continue;

			GameRegistry.ObjectHolder objHolder = field.getAnnotation(GameRegistry.ObjectHolder.class);
			if(objHolder == null) continue;

			Block block = ClassInjector.getOrNull(field);
            if(block == null || !(block instanceof IItemBlockProvider)) continue;

            String internalName = objHolder.value();
            Item item = ((IItemBlockProvider)block).createItemBlock();
            item.setRegistryName(internalName);
            block.setUnlocalizedName(Chococraft.MODID + "." + internalName);
            block.setCreativeTab(Chococraft.creativeTab);

            event.getRegistry().register(item);
		}
	}

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event)
    {
        for(Field field : ModBlocks.class.getDeclaredFields())
        {
            if(!Block.class.isAssignableFrom(field.getType())) continue;
            Block block = ClassInjector.getOrNull(field);

            if(block instanceof IItemBlockProvider)
            {
                ((IItemModelProvider) block).registerItemModel(Item.getItemFromBlock(block));
            }
        }
    }
}
