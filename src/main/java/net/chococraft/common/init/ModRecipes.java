package net.chococraft.common.init;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ModRecipes {
    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        GameRegistry.addSmelting(ModItems.chocoboDrumStickRaw, new ItemStack(ModItems.chocoboDrumStickCooked), 2);
        GameRegistry.addSmelting(ModItems.pickledGysahlRaw, new ItemStack(ModItems.pickledGysahlCooked), 2);
    }
}
