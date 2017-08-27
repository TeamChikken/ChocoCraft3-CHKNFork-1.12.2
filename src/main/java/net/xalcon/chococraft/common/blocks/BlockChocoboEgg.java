package net.xalcon.chococraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.entities.breeding.ChocoboBreedInfo;
import net.xalcon.chococraft.common.entities.breeding.ChocoboStatSnapshot;
import net.xalcon.chococraft.common.init.ModBlocks;
import net.xalcon.chococraft.common.tileentities.TileEntityChocoboEgg;
import net.xalcon.chococraft.utils.inject.AttachedTileEntity;
import net.xalcon.chococraft.utils.registration.IItemBlockProvider;

import javax.annotation.Nullable;
import java.util.List;

@AttachedTileEntity(name = "chocobo_egg", tile = TileEntityChocoboEgg.class)
public class BlockChocoboEgg extends Block implements IItemBlockProvider
{
    public final static AxisAlignedBB BOUNDS = new AxisAlignedBB(.25, 0, .25, .75, .75, .75);

    @SuppressWarnings("unused")
    public BlockChocoboEgg()
    {
        super(Material.GROUND);
        this.setHarvestLevel("pickaxe", 0);
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(!worldIn.isRemote)
        {
            TileEntity tile = worldIn.getTileEntity(pos);
            if(!(tile instanceof TileEntityChocoboEgg)) return;

            ChocoboBreedInfo breedInfo = ChocoboBreedInfo.getFromNbtOrDefault(stack.getSubCompound("BreedInfo"));

            ((TileEntityChocoboEgg) tile).setBreedInfo(breedInfo);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (te instanceof TileEntityChocoboEgg)
        {
            if (worldIn.isRemote) return;
            //noinspection ConstantConditions | this will never be null when we are getting called - otherwise, its a MC bug
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005F);

            ItemStack itemStack = new ItemStack(ModBlocks.chocoboEgg, 1, 0);
            ChocoboBreedInfo breedInfo = ((TileEntityChocoboEgg) te).getBreedInfo();
            if(breedInfo == null)
            {
                Chococraft.log.error("Unable to create ItemStack for egg @ {}, the eggy has no breeding info attached");
                return;
            }
            itemStack.setTagInfo("BreedInfo", breedInfo.serialize());
            spawnAsEntity(worldIn, pos, itemStack);
            return;
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        NBTTagCompound nbt = stack.getSubCompound("BreedInfo");
        if(nbt != null)
        {
            ChocoboBreedInfo info = new ChocoboBreedInfo(nbt);
            ChocoboStatSnapshot mother = info.getMother();
            ChocoboStatSnapshot father = info.getFather();

            tooltip.add(I18n.format("item." + Chococraft.MODID + ".chocobo_egg.tooltip.mother_info", (int)mother.health, (int)(mother.speed * 100), (int)mother.stamina));
            tooltip.add(I18n.format("item." + Chococraft.MODID + ".chocobo_egg.tooltip.father_info", (int)father.health, (int)(father.speed * 100), (int)father.stamina));
        }
        else
        {
            tooltip.add(I18n.format("item." + Chococraft.MODID + ".chocobo_egg.tooltip.invalid_egg"));
        }
    }
}
