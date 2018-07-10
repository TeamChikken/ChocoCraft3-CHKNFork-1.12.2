package net.slayer5934.chococraft.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.slayer5934.chococraft.common.init.ModItems;

import javax.annotation.Nonnull;

public class SlotChocoboSaddle extends SlotItemHandler
{
    public SlotChocoboSaddle(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return stack.getItem() == ModItems.chocoboSaddle;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack)
    {
        return this.getItemHandler().getSlotLimit(0);
    }
}
