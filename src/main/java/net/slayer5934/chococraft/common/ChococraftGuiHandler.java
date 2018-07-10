package net.slayer5934.chococraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.slayer5934.chococraft.client.gui.GuiChocoboNest;
import net.slayer5934.chococraft.common.inventory.ContainerNest;
import net.slayer5934.chococraft.common.tileentities.TileEntityChocoboNest;
import net.slayer5934.chococraft.utils.WorldUtils;

import javax.annotation.Nullable;

public class ChococraftGuiHandler implements IGuiHandler
{
    public final static int GUI_CHOCOBO_NEST = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (guiId)
        {
            case GUI_CHOCOBO_NEST:
                TileEntityChocoboNest nest = WorldUtils.getTileEntitySafe(world, new BlockPos(x, y, z), TileEntityChocoboNest.class);
                if(nest != null)
                    return new ContainerNest(nest, player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (guiId)
        {
            case GUI_CHOCOBO_NEST:
                TileEntityChocoboNest nest = WorldUtils.getTileEntitySafe(world, new BlockPos(x, y, z), TileEntityChocoboNest.class);
                if(nest != null)
                    return new GuiChocoboNest(nest, player);
        }
        return null;
    }
}
