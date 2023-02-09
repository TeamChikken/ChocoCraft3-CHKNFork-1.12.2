package net.chococraft.common.inventory;

import net.chococraft.common.entities.EntityChocobo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.chococraft.common.ChocoConfig;

public class ContainerSaddleBag extends Container {
	private EntityChocobo chocobo;

	public ContainerSaddleBag(EntityChocobo chocobo, EntityPlayer player) {
		this.chocobo = chocobo;
		this.refreshSlots(chocobo, player);
	}

	public EntityChocobo getChocobo() {
		return chocobo;
	}

	public void refreshSlots(EntityChocobo chocobo, EntityPlayer player) {
		this.inventorySlots.clear();

		// Saddle slot
		this.addSlotToContainer(new SlotChocoboSaddle(chocobo.saddleItemStackHandler, 0, -16, 18));
		
		//Player inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 122 + row * 18));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 180));
		}
		
		//Chocobo inventory
		switch (chocobo.getSaddleType()) {
	    case SADDLE_BAGS:
	    bindInventorySmall(chocobo.chocoboInventory);
	    break;
	    case PACK:
	    bindInventoryBig(chocobo.chocoboInventory);
	    break;
	    }
	}
	
	//Chocobo inventory binding
	private void bindInventorySmall(IItemHandler inventory) {
    	if (ChocoConfig.chocobo.saddlesCosmeticOnly) return;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                this.addSlotToContainer(new SlotItemHandler(inventory, row * 5 + col, 44 + col * 18, 36 + row * 18));
            }
        }
    }

    private void bindInventoryBig(IItemHandler inventory) {
    	if (ChocoConfig.chocobo.saddlesCosmeticOnly) return;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlotToContainer(new SlotItemHandler(inventory, row * 9 + col, 8 + col * 18, 18 + row * 18));
            }
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < this.chocobo.chocoboInventory.getSlots()) {
				if (!this.mergeItemStack(itemstack1, this.chocobo.chocoboInventory.getSlots(), this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else 
				if (!this.mergeItemStack(itemstack1, 0, this.chocobo.chocoboInventory.getSlots(), false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}
