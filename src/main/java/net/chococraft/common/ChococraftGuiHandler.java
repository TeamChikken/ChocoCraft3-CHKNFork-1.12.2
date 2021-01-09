package net.chococraft.common;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.chococraft.client.gui.GuiChocoboNest;
import net.chococraft.common.inventory.ContainerNest;
import net.chococraft.common.tileentities.TileEntityChocoboNest;
import net.chococraft.utils.WorldUtils;

public class ChococraftGuiHandler implements IGuiHandler {
    public final static int GUI_CHOCOBO_NEST = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
        switch (guiId) {
            case GUI_CHOCOBO_NEST:
                TileEntityChocoboNest nest = WorldUtils.getTileEntitySafe(world, new BlockPos(x, y, z), TileEntityChocoboNest.class);
                if (nest != null)
                    return new ContainerNest(nest, player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
        switch (guiId) {
            case GUI_CHOCOBO_NEST:
                TileEntityChocoboNest nest = WorldUtils.getTileEntitySafe(world, new BlockPos(x, y, z), TileEntityChocoboNest.class);
                if (nest != null)
                    return new GuiChocoboNest(nest, player);
        }
        return null;
    }
}
