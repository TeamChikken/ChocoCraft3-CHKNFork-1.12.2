package net.chococraft.common.tileentities;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.chococraft.Chococraft;
import net.chococraft.common.ChocoConfig;
import net.chococraft.common.blocks.BlockChocoboEgg;
import net.chococraft.common.blocks.BlockStrawNest;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.entities.breeding.BreedingHelper;
import net.chococraft.common.entities.breeding.ChocoboBreedInfo;
import net.chococraft.common.init.ModBlocks;

public class TileEntityChocoboNest extends TileEntity implements ITickable {
    private final static CheckOffset[] SHELTER_CHECK_OFFSETS = new CheckOffset[]
            {
                    new CheckOffset(new Vec3i(0, 1, 0), true),
                    new CheckOffset(new Vec3i(0, 2, 0), true),
                    new CheckOffset(new Vec3i(-1, 3, -1), false),
                    new CheckOffset(new Vec3i(-1, 3, 0), false),
                    new CheckOffset(new Vec3i(-1, 3, 1), false),
                    new CheckOffset(new Vec3i(0, 3, -1), false),
                    new CheckOffset(new Vec3i(0, 3, 0), false),
                    new CheckOffset(new Vec3i(0, 3, 1), false),
                    new CheckOffset(new Vec3i(1, 3, -1), false),
                    new CheckOffset(new Vec3i(1, 3, 0), false),
                    new CheckOffset(new Vec3i(1, 3, 1), false),
            };

    @SuppressWarnings("WeakerAccess")
    public static final String NBTKEY_IS_SHELTERED = "IsSheltered";
    @SuppressWarnings("WeakerAccess")
    public static final String NBTKEY_TICKS = "Ticks";
    @SuppressWarnings("WeakerAccess")
    public static final String NBTKEY_NEST_INVENTORY = "Inventory";

    private ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            TileEntityChocoboNest.this.onInventoryChanged();
        }
    };

    private boolean isSheltered;
    private int ticks = 0;

    @Override
    public void update() {
        if (this.world.isRemote)
            return;

        this.ticks++;
        if (ticks > 1_000_000)
            ticks = 0;

        boolean changed = false;

        if (this.ticks % 5 == 0 && !this.getEggItemStack().isEmpty()) {
            changed = this.updateEgg();
        }

        if (this.ticks % 200 == 100) {
            changed |= this.updateSheltered();
        }

        if (changed)
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos));
    }

    private boolean updateEgg() {
        ItemStack egg = this.getEggItemStack();

        if (!BlockChocoboEgg.isChocoboEgg(egg))
            return false;

        if (!egg.hasTagCompound())
            return false;

        NBTTagCompound nbt = egg.getOrCreateSubCompound(BlockChocoboEgg.NBTKEY_HATCHINGSTATE);
        int time = nbt.getInteger(BlockChocoboEgg.NBTKEY_HATCHINGSTATE_TIME);
        time += this.isSheltered ? 2 : 1;
        nbt.setInteger(BlockChocoboEgg.NBTKEY_HATCHINGSTATE_TIME, time);

        if (time < ChocoConfig.breeding.eggHatchTimeTicks)
            return false;

        // egg is ready to hatch
        ChocoboBreedInfo breedInfo = ChocoboBreedInfo.getFromNbtOrDefault(egg.getSubCompound(BlockChocoboEgg.NBTKEY_BREEDINFO));
        EntityChocobo baby = BreedingHelper.createChild(breedInfo, this.world);
        baby.setLocationAndAngles(this.pos.getX() + 0.5, this.pos.getY() + 0.2, this.pos.getZ() + 0.5, 0.0F, 0.0F);
        this.world.spawnEntity(baby);

        Random random = baby.getRNG();
        for (int i = 0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            double d3 = random.nextDouble() * baby.width * 2.0D - baby.width;
            double d4 = 0.5D + random.nextDouble() * baby.height;
            double d5 = random.nextDouble() * baby.width * 2.0D - baby.width;
            this.world.spawnParticle(EnumParticleTypes.HEART, baby.posX + d3, baby.posY + d4, baby.posZ + d5, d0, d1, d2);
        }

        this.setEggItemStack(ItemStack.EMPTY);
        return true;
    }

    private boolean updateSheltered() {
        // TODO: Make this better, use "can see sky" for shelter detection
        boolean sheltered = isSheltered();

        if (this.isSheltered != sheltered) {
            this.isSheltered = sheltered;
            return true;
        }

        return false;
    }

    public ItemStack getEggItemStack() {
        return this.inventory.getStackInSlot(0);
    }

    public void setEggItemStack(ItemStack itemStack) {
        if (itemStack.isEmpty())
            this.inventory.setStackInSlot(0, ItemStack.EMPTY);
        else if (BlockChocoboEgg.isChocoboEgg(itemStack)) {
            this.inventory.setStackInSlot(0, itemStack);
            if (itemStack.hasTagCompound()) {
                NBTTagCompound nbt = itemStack.getOrCreateSubCompound(BlockChocoboEgg.NBTKEY_HATCHINGSTATE);
                int time = nbt.getInteger(BlockChocoboEgg.NBTKEY_HATCHINGSTATE_TIME);
                nbt.setInteger(BlockChocoboEgg.NBTKEY_HATCHINGSTATE_TIME, time);
            }
        }
    }

    public IItemHandler getInventory() {
        return this.inventory;
    }

    //region Data Synchronization/Persistence
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isSheltered = nbt.getBoolean(NBTKEY_IS_SHELTERED);
        this.ticks = nbt.getInteger(NBTKEY_TICKS);
        this.inventory.deserializeNBT(nbt.getCompoundTag(NBTKEY_NEST_INVENTORY));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean(NBTKEY_IS_SHELTERED, this.isSheltered);
        nbt.setInteger(NBTKEY_TICKS, this.ticks);
        nbt.setTag(NBTKEY_NEST_INVENTORY, this.inventory.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag(NBTKEY_NEST_INVENTORY, this.inventory.serializeNBT());
        return new SPacketUpdateTileEntity(this.getPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.inventory.deserializeNBT(pkt.getNbtCompound().getCompoundTag(NBTKEY_NEST_INVENTORY));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag(NBTKEY_NEST_INVENTORY, this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        super.handleUpdateTag(nbt);
        this.inventory.deserializeNBT(nbt.getCompoundTag(NBTKEY_NEST_INVENTORY));
    }
    //endregion

    private static class CheckOffset {
        Vec3i offset;
        boolean shouldBeAir;

        CheckOffset(Vec3i offset, boolean shouldBeAir) {
            this.offset = offset;
            this.shouldBeAir = shouldBeAir;
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(Chococraft.MODID + ".container.nest");
    }

    public void onInventoryChanged() {
        this.markDirty();
        IBlockState newState = ModBlocks.strawNest.getDefaultState().withProperty(BlockStrawNest.HAS_EGG, !this.getEggItemStack().isEmpty());
        this.getWorld().setBlockState(this.getPos(), newState);
    }

    public boolean isSheltered() {
        boolean sheltered = true;
        for (CheckOffset checkOffset : SHELTER_CHECK_OFFSETS) {
            if (world.isAirBlock(this.getPos().add(checkOffset.offset)) != checkOffset.shouldBeAir) {
                sheltered = false;
                break;
            }
        }
        return sheltered;
    }
}
