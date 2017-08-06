package net.xalcon.chococraft.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import sun.invoke.empty.Empty;

import javax.annotation.Nonnull;

public class SaddleItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
    private ItemStack itemStack = ItemStack.EMPTY;

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        this.itemStack = stack;
    }

    @Override
    public int getSlots()
    {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return this.itemStack;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if(stack.isEmpty())
            return ItemStack.EMPTY;

        if(this.itemStack.isEmpty())
        {
            if(simulate)
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
            this.itemStack = stack.splitStack(1);
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if(amount <= 0)
            return ItemStack.EMPTY;

        if(simulate)
            return ItemHandlerHelper.copyStackWithSize(this.itemStack, amount);
        return this.itemStack.splitStack(amount);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 1;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return this.itemStack.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.itemStack = new ItemStack(nbt);
    }
}
