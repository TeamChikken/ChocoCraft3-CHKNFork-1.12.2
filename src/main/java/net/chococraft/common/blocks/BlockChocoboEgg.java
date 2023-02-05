package net.chococraft.common.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.chococraft.common.entities.properties.ChocoboColor;
import net.chococraft.common.items.ItemBlockChocoboEgg;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import net.chococraft.Chococraft;
import net.chococraft.common.entities.breeding.ChocoboBreedInfo;
import net.chococraft.common.entities.breeding.ChocoboStatSnapshot;
import net.chococraft.common.init.ModBlocks;
import net.chococraft.common.tileentities.TileEntityChocoboEgg;
import net.chococraft.utils.inject.AttachedTileEntity;
import net.chococraft.utils.registration.IItemBlockProvider;

@AttachedTileEntity(name = "chocobo_egg", tile = TileEntityChocoboEgg.class)
public class BlockChocoboEgg extends Block implements IItemBlockProvider {
    public final static String NBTKEY_HATCHINGSTATE_TIME = "Time";
    public final static String NBTKEY_HATCHINGSTATE = "HatchingState";
    public final static String NBTKEY_BREEDINFO = "BreedInfo";

    public final static AxisAlignedBB BOUNDS = new AxisAlignedBB(.25, 0, .25, .75, .75, .75);

    @SuppressWarnings("unused")
    public BlockChocoboEgg() {
        super(Material.GROUND);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public static boolean isChocoboEgg(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlock &&
                ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockChocoboEgg;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityChocoboEgg();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (!(tile instanceof TileEntityChocoboEgg)) return;

            ChocoboBreedInfo breedInfo = ChocoboBreedInfo.getFromNbtOrDefault(stack.getSubCompound(NBTKEY_BREEDINFO));

            ((TileEntityChocoboEgg) tile).setBreedInfo(breedInfo);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileEntityChocoboEgg) {
            if (worldIn.isRemote) return;
            //noinspection ConstantConditions | this will never be null when we are getting called - otherwise, its a MC bug
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005F);

            ItemStack itemStack = new ItemStack(ModBlocks.chocoboEgg, 1, 0);
            ChocoboBreedInfo breedInfo = ((TileEntityChocoboEgg) te).getBreedInfo();
            if (breedInfo == null) {
                Chococraft.log.error("Unable to create ItemStack for egg @ {}, the eggy has no breeding info attached");
                return;
            }
            if (breedInfo != null) {
                itemStack.setTagInfo(NBTKEY_BREEDINFO, breedInfo.serialize());
            }
            spawnAsEntity(worldIn, pos, itemStack);
            return;
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    public String getColorText(ChocoboColor color) {
        switch (color) {
            case YELLOW: return I18n.format("item.chococraft.chocobo_egg.tooltip.yellow");
            case GREEN: return I18n.format("item.chococraft.chocobo_egg.tooltip.green");
            case BLUE: return I18n.format("item.chococraft.chocobo_egg.tooltip.blue");
            case WHITE: return I18n.format("item.chococraft.chocobo_egg.tooltip.white");
            case BLACK: return I18n.format("item.chococraft.chocobo_egg.tooltip.black");
            case GOLD: return I18n.format("item.chococraft.chocobo_egg.tooltip.gold");
            case PINK: return I18n.format("item.chococraft.chocobo_egg.tooltip.pink");
            case RED: return I18n.format("item.chococraft.chocobo_egg.tooltip.red");
            case PURPLE: return I18n.format("item.chococraft.chocobo_egg.tooltip.purple");
            case FLAME: return I18n.format("item.chococraft.chocobo_egg.tooltip.flame");
        }

        return "";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        NBTTagCompound nbtBreedInfo = stack.getSubCompound(NBTKEY_BREEDINFO);
        if (nbtBreedInfo != null) {
            ChocoboBreedInfo info = new ChocoboBreedInfo(nbtBreedInfo);
            ChocoboStatSnapshot mother = info.getMother();
            ChocoboStatSnapshot father = info.getFather();

            tooltip.add(I18n.format("item." + Chococraft.MODID + ".chocobo_egg.tooltip.mother_info", (int) mother.health, (int) (mother.speed * 100), (int) mother.stamina, getColorText(mother.color)));
            tooltip.add(I18n.format("item." + Chococraft.MODID + ".chocobo_egg.tooltip.father_info", (int) father.health, (int) (father.speed * 100), (int) father.stamina, getColorText(mother.color)));
        } else {
            tooltip.add(I18n.format("item." + Chococraft.MODID + ".chocobo_egg.tooltip.invalid_egg"));
        }
    }

    @Override
    public Item createItemBlock() {
        return new ItemBlockChocoboEgg(this);
    }
}
