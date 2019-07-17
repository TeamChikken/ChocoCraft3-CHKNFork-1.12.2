package net.slayer5934.chococraft.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.slayer5934.chococraft.common.tileentities.TileEntityChocoboNest;

import javax.annotation.Nonnull;

public class ContainerNest extends Container {
	private TileEntityChocoboNest tile;
	private EntityPlayer player;
	
	public ContainerNest(TileEntityChocoboNest tile, EntityPlayer player) {
		this.tile = tile;
		this.player = player;
		
		this.bindPlayerInventory(player);
		
		this.addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 80, 35));
	}
	
	private void bindPlayerInventory(EntityPlayer player) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index == 36) {
				if (!this.mergeItemStack(itemstack1, 0, 36, true)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 36, 37, false)) {
				return ItemStack.EMPTY;
			}
			
			this.tile.onInventoryChanged();
			
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
		}
		
		return itemstack;
	}
	
	private static class SlotEgg extends SlotItemHandler {
		public SlotEgg(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			return true;
		}
		
		@Override
		public boolean isItemValid(@Nonnull ItemStack stack) {
			return super.isItemValid(stack);
		}
	}
}
