package net.chococraft.common.items;

import net.chococraft.common.ChocoConfig;
import net.chococraft.common.blocks.BlockChocoboEgg;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBlockChocoboEgg extends ItemBlock {
    public ItemBlockChocoboEgg(Block block) {
        super(block);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (!BlockChocoboEgg.isChocoboEgg(stack))
            return super.showDurabilityBar(stack);

        if (!stack.hasTagCompound())
            return false;

        NBTTagCompound nbtHatchIngstate = stack.getSubCompound(BlockChocoboEgg.NBTKEY_HATCHINGSTATE);
        if (nbtHatchIngstate == null)
            return false;

        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (!BlockChocoboEgg.isChocoboEgg(stack))
            return super.getDurabilityForDisplay(stack);

        if (!stack.hasTagCompound())
            return 0.0;

        int time = 0;
        NBTTagCompound nbtHatchIngstate = stack.getSubCompound(BlockChocoboEgg.NBTKEY_HATCHINGSTATE);
        if (nbtHatchIngstate != null)
            time = nbtHatchIngstate.getInteger(BlockChocoboEgg.NBTKEY_HATCHINGSTATE_TIME);

        double percent = (double) time / (double) ChocoConfig.breeding.eggHatchTimeTicks;

        return 1.0 - percent;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        if (!BlockChocoboEgg.isChocoboEgg(stack))
            return super.getRGBDurabilityForDisplay(stack);

        return 0x0000FF00;
    }
}
