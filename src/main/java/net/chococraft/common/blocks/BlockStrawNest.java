package net.chococraft.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.chococraft.Chococraft;
import net.chococraft.common.ChococraftGuiHandler;
import net.chococraft.common.tileentities.TileEntityChocoboNest;
import net.chococraft.utils.WorldUtils;
import net.chococraft.utils.inject.AttachedTileEntity;
import net.chococraft.utils.registration.IItemBlockProvider;

@AttachedTileEntity(name = "chocobo_nest", tile = TileEntityChocoboNest.class)
public class BlockStrawNest extends Block implements IItemBlockProvider {
    public final static AxisAlignedBB BOUNDS_EMPTY = new AxisAlignedBB(0, 0, 0, 1, .1875, 1);
    public final static PropertyBool HAS_EGG = PropertyBool.create("egg");

    @SuppressWarnings("unused") // used by class factory
    public BlockStrawNest() {
        super(Material.ROCK);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS_EMPTY;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HAS_EGG);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HAS_EGG, (meta & 0b0001) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HAS_EGG) ? 1 : 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityChocoboNest nest = WorldUtils.getTileEntitySafe(worldIn, pos, TileEntityChocoboNest.class);
        if (nest == null) return false;

        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (BlockChocoboEgg.isChocoboEgg(heldItem)) {
            if (!nest.getEggItemStack().isEmpty()) return false;
            if (worldIn.isRemote) return true;
            nest.setEggItemStack(playerIn.getHeldItem(hand).copy());
            playerIn.getHeldItem(hand).shrink(1);
            return true;
        } else {
            playerIn.openGui(Chococraft.getInstance(), ChococraftGuiHandler.GUI_CHOCOBO_NEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return false;
    }

    @Override
    public void registerItemModel(Item item) {
        ResourceLocation rl = item.getRegistryName();
        assert rl != null;
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(rl, "egg=false"));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityChocoboNest();
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
