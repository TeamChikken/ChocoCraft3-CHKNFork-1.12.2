package net.xalcon.chococraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.xalcon.chococraft.utils.registration.IItemBlockProvider;

public class BlockChocoboEgg extends Block implements IItemBlockProvider
{
    public final static AxisAlignedBB BOUNDS = new AxisAlignedBB(.25, 0, .25, .75, .75, .75);

    public BlockChocoboEgg()
    {
        super(Material.ROCK);
    }

    @Override @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
