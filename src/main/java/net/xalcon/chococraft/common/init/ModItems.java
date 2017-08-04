package net.xalcon.chococraft.common.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.items.ItemGysahlGreen;
import net.xalcon.chococraft.utils.inject.ClassInjector;
import net.xalcon.chococraft.utils.registration.IItemModelRegistrationHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@GameRegistry.ObjectHolder(Chococraft.MODID)
@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class ModItems
{
	@GameRegistry.ObjectHolder("gysahl_green")
	public static ItemGysahlGreen gysahlGreen;

	@GameRegistry.ObjectHolder("chocobo_saddle")
    @ItemSetupParameters(stackSize = 4)
    public static Item chocoboSaddle;

	public static ItemStack spawneggChocobo;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		for(Field field : ModItems.class.getDeclaredFields())
		{
			if(!Item.class.isAssignableFrom(field.getType())) continue;

			GameRegistry.ObjectHolder objHolder = field.getAnnotation(GameRegistry.ObjectHolder.class);
			if(objHolder == null) continue;

			Item item = ClassInjector.createFromField(field);
			String internalName = objHolder.value();
			item.setRegistryName(internalName);
			item.setUnlocalizedName(Chococraft.MODID + "." + internalName);
			item.setCreativeTab(Chococraft.creativeTab);
            ItemSetupParameters parameters = field.getAnnotation(ItemSetupParameters.class);
            if(parameters != null)
                applyParameters(item, parameters);
			event.getRegistry().register(item);

			if(item instanceof IItemModelRegistrationHandler)
				((IItemModelRegistrationHandler) item).registerItemModel(item);
			else
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
	private @interface ItemSetupParameters
    {
        int stackSize() default 64;
    }

	private static void applyParameters(Item item, ItemSetupParameters parameters)
    {
        item.setMaxStackSize(parameters.stackSize());
    }
}
