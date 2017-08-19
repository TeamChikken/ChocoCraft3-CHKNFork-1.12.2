package net.xalcon.chococraft.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;
import net.xalcon.chococraft.common.tileentities.TileEntityChocoboNest;

public class ContainerNest extends Container
{
    private TileEntityChocoboNest tile;
    private EntityPlayer player;

    public ContainerNest(TileEntityChocoboNest tile, EntityPlayer player)
    {
        this.tile = tile;
        this.player = player;

        this.bindPlayerInventory(player);

        this.addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 80, 35));
    }

    private void bindPlayerInventory(EntityPlayer player)
    {
        for (int row = 0; row < 3; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}
