package net.xalcon.chococraft.common.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.utils.registration.IItemModelRegistrationHandler;

public class ItemChocoboSaddle extends Item implements IItemModelRegistrationHandler
{
    public ItemChocoboSaddle()
    {
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(!this.isInCreativeTab(tab)) return;
        for(EntityChocobo.SaddleType type : EntityChocobo.SaddleType.ITEM_META)
        {
            items.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public void registerItemModel(Item item)
    {
        ResourceLocation rl = this.getRegistryName();
        assert rl != null;

        // this registers a "blockstate" for our item, which allows different models depending on the variant
        ResourceLocation loc = new ResourceLocation(rl.getResourceDomain(), "items/" + rl.getResourcePath());
        for(EntityChocobo.SaddleType type : EntityChocobo.SaddleType.ITEM_META)
        {
            ModelLoader.setCustomModelResourceLocation(this, type.getMeta(), new ModelResourceLocation(loc, "type=" + type.name().toLowerCase()));
        }
    }

    public EntityChocobo.SaddleType getSaddleType(ItemStack stack)
    {
        return EntityChocobo.SaddleType.getFromMeta(stack.getMetadata());
    }
}
