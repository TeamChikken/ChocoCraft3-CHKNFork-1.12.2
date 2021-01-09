package net.chococraft.common.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.chococraft.common.init.ModItems;

public class SlotChocoboSaddle extends SlotItemHandler {
    public SlotChocoboSaddle(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return stack.getItem() == ModItems.chocoboSaddle;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return this.getItemHandler().getSlotLimit(0);
    }
}
