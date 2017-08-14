package net.xalcon.chococraft.utils;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
}
