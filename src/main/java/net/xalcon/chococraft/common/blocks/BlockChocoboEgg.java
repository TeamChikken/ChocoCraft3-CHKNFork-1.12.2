package net.xalcon.chococraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.xalcon.chococraft.common.tileentities.TileEntityChocoboEgg;
import net.xalcon.chococraft.utils.inject.AttachedTileEntity;
import net.xalcon.chococraft.utils.registration.IItemBlockProvider;

import javax.annotation.Nullable;

@AttachedTileEntity(name = "chocobo_egg", tile = TileEntityChocoboEgg.class)
public class BlockChocoboEgg extends Block implements IItemBlockProvider
{
    public final static AxisAlignedBB BOUNDS = new AxisAlignedBB(.25, 0, .25, .75, .75, .75);

    @SuppressWarnings("unused")
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

    @Override @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public static boolean isChocoboEgg(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ItemBlock &&
                ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockChocoboEgg;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityChocoboEgg();
    }
}
