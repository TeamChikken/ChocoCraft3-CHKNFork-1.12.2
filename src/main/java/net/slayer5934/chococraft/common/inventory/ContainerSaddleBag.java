package net.slayer5934.chococraft.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.slayer5934.chococraft.common.entities.EntityChocobo;

public class ContainerSaddleBag extends Container
{
    public ContainerSaddleBag(EntityChocobo chocobo, EntityPlayer player)
    {
        this.refreshSlots(chocobo, player);
    }

    public void refreshSlots(EntityChocobo chocobo, EntityPlayer player)
    {
        this.inventorySlots.clear();
        bindPlayerInventory(player);

        switch(chocobo.getSaddleType())
        {
            case SADDLE_BAGS:
                bindInventorySmall(chocobo.chocoboInventory);
                break;
            case PACK:
                bindInventoryBig(chocobo.chocoboInventory);
                break;
        }

        this.addSlotToContainer(new SlotChocoboSaddle(chocobo.saddleItemStackHandler, 0, -16, 18));
    }

    private void bindInventorySmall(IItemHandler inventory)
    {
        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                this.addSlotToContainer(new SlotItemHandler(inventory, row * 5 + col, 44 + col * 18, 36 + row * 18));
            }
        }
    }

    private void bindInventoryBig(IItemHandler inventory)
    {
        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                this.addSlotToContainer(new SlotItemHandler(inventory, row * 9 + col, 8 + col * 18, 18 + row * 18));
            }
        }
    }

    private void bindPlayerInventory(EntityPlayer player)
    {
        for (int row = 0; row < 3; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 122 + row * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 180));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
    	return ItemStack.EMPTY;
    }
}
