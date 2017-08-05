package net.xalcon.chococraft.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.xalcon.chococraft.common.entities.EntityChocobo;

public class ContainerSaddleBag extends Container
{
    public ContainerSaddleBag(EntityChocobo chocobo, EntityPlayer player)
    {
        
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}
