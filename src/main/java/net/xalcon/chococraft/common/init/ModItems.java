package net.xalcon.chococraft.common.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.items.ItemChocoboSaddle;
import net.xalcon.chococraft.common.items.ItemChocoboSpawnEgg;
import net.xalcon.chococraft.common.items.ItemGysahlGreenSeeds;
import net.xalcon.chococraft.utils.inject.ClassInjector;
import net.xalcon.chococraft.utils.inject.InstanceFactoryMethod;
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
	@GameRegistry.ObjectHolder("chocobo_saddle")
    @ItemSetupParameters(stackSize = 4)
    public static ItemChocoboSaddle chocoboSaddle;

    @GameRegistry.ObjectHolder("chocobo_feather")
    public static Item chocoboFeather;

    @GameRegistry.ObjectHolder("chocobo_whistle")
    @ItemSetupParameters(stackSize = 1)
    public static Item chocoboWhistle;

	@GameRegistry.ObjectHolder("chocobo_spawn_egg")
	public static ItemChocoboSpawnEgg chocoboSpawnEgg;

    @GameRegistry.ObjectHolder("gysahl_green_seeds")
    public static ItemGysahlGreenSeeds gysahlGreenSeeds;

    @GameRegistry.ObjectHolder("gysahl_green")
    @ItemFoodParameters(amount = 1, saturation = 1)
    public static ItemFood gysahlGreen;

	@GameRegistry.ObjectHolder("chocobo_drumstick_raw")
    @ItemFoodParameters(amount = 2, saturation = 2, isWolfFood = true)
	public static ItemFood chocoboDrumStickRaw;

	@GameRegistry.ObjectHolder("chocobo_drumstick_cooked")
    @ItemFoodParameters(amount = 6, saturation = 8, isWolfFood = true)
	public static ItemFood chocoboDrumStickCooked;

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
		}
	}

    @SubscribeEvent
	public static void onRegisterModels(ModelRegistryEvent event)
    {
        for(Field field : ModItems.class.getDeclaredFields())
        {
            if(!Item.class.isAssignableFrom(field.getType())) continue;
            Item item = ClassInjector.getOrNull(field);
            if(item instanceof IItemModelRegistrationHandler)
                ((IItemModelRegistrationHandler) item).registerItemModel(item);
            else
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface ItemFoodParameters
    {
        int amount();
        int saturation();
        boolean isWolfFood() default false;
    }

    @InstanceFactoryMethod
    public static ItemFood createItemFood(ItemFoodParameters parameters)
    {
        return new ItemFood(parameters.amount(), parameters.saturation(), parameters.isWolfFood());
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
