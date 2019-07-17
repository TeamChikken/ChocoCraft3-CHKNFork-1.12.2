package net.slayer5934.chococraft.common.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.slayer5934.chococraft.common.init.ModBlocks;
import net.slayer5934.chococraft.common.init.ModItems;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

/**
 * Created by Joseph on 7/17/2019.
 */
public class ChococraftThaumcraft {
	@SubscribeEvent
	public void aspectRegistrationEvent(AspectRegistryEvent event) {
		event.register.registerObjectTag(new ItemStack(ModItems.chocoboFeather), new AspectList().add(Aspect.AIR, 10).add(Aspect.FLIGHT, 10));
		event.register.registerObjectTag(new ItemStack(ModItems.chocoboDrumStickRaw), new AspectList().add(Aspect.AIR, 10).add(Aspect.BEAST, 10).add(Aspect.LIFE, 10));
		event.register.registerObjectTag(new ItemStack(ModItems.chocoboDrumStickCooked), new AspectList().add(Aspect.AIR, 10).add(Aspect.BEAST, 10).add(Aspect.CRAFT, 10));
		
		event.register.registerObjectTag(new ItemStack(ModItems.gysahlGreen), new AspectList().add(Aspect.PLANT, 3));
		event.register.registerObjectTag(new ItemStack(ModItems.gysahlGreenSeeds), new AspectList().add(Aspect.PLANT, 2));
		event.register.registerObjectTag(new ItemStack(ModItems.lovelyGysahlGreen), new AspectList().add(Aspect.PLANT, 6).add(Aspect.DESIRE, 6));
		event.register.registerObjectTag(new ItemStack(ModItems.pickledGysahlRaw), new AspectList().add(Aspect.PLANT, 3).add(Aspect.CRAFT, 3));
		event.register.registerObjectTag(new ItemStack(ModItems.pickledGysahlCooked), new AspectList().add(Aspect.PLANT, 5).add(Aspect.CRAFT, 5));
		
		event.register.registerObjectTag(new ItemStack(ModItems.abilityFruit), new AspectList().add(Aspect.PLANT, 15).add(Aspect.MAGIC, 15).add(Aspect.DESIRE, 15));
		
		event.register.registerObjectTag(new ItemStack(ModBlocks.chocoboEgg), new AspectList().add(Aspect.LIFE, 10).add(Aspect.PROTECT, 10));
		
		ThaumcraftApi.registerEntityTag("chocobo", new AspectList().add(Aspect.BEAST, 35).add(Aspect.FLIGHT, 25).add(Aspect.SENSES, 8));
	}
}
