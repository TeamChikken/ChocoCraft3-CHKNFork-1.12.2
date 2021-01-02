package net.chococraft.utils;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldUtils
{
    public static int getDistanceToSurface(BlockPos startPos, World world)
    {
        BlockPos lastLiquidPos = null;

        for(BlockPos pos = startPos; pos.getY() < world.getHeight(); pos = pos.up())
        {
            IBlockState state = world.getBlockState(pos);
            if(!state.getMaterial().isLiquid())
                break;

            lastLiquidPos = pos;
        }

        return lastLiquidPos == null ? -1 : lastLiquidPos.getY() - startPos.getY();
    }

    @Nullable
    public static <T extends TileEntity> T getTileEntitySafe(IBlockAccess world, BlockPos pos, Class<T> tileClass)
    {
        TileEntity tile = world.getTileEntity(pos);
        if(tile == null || !tileClass.isAssignableFrom(tile.getClass())) return null;
        return tileClass.cast(tile);
    }
}
