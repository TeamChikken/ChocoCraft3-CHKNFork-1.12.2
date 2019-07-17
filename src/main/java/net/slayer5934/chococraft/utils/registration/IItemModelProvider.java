package net.slayer5934.chococraft.utils.registration;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public interface IItemModelProvider {
	default void registerItemModel(Item item) {
		ResourceLocation rl = item.getRegistryName();
		assert rl != null;
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(rl, "inventory"));
	}
}
