package net.xalcon.chococraft.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.xalcon.chococraft.Chococraft;

import javax.annotation.Nonnull;

public abstract class SaddleItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
    protected ItemStack itemStack = ItemStack.EMPTY;

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        ItemStack oldStack = this.itemStack;
        this.itemStack = stack;
        if(!(oldStack.isEmpty() && stack.isEmpty())) // dont update if we change from empty to empty
        {
            this.onStackChanged();
        }
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
        this.onStackChanged();
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
        ItemStack outStack = this.itemStack.splitStack(amount);
        this.onStackChanged();
        return outStack;
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
        this.onStackChanged();
    }

    protected abstract void onStackChanged();
}
