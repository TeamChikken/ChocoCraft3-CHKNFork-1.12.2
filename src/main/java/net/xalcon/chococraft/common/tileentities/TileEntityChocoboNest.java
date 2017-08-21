package net.xalcon.chococraft.common.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.blocks.BlockChocoboEgg;
import net.xalcon.chococraft.common.blocks.BlockStrawNest;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.init.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TileEntityChocoboNest extends TileEntity implements ITickable
{
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

    private ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityChocoboNest.this.onInventoryChanged();
        }
    };

    private UUID ownerChocobo;
    private boolean isSheltered;
    private int ticks = 0;

    @Override
    public void update()
    {
        if(this.world.isRemote) return;
        this.ticks++;
        if(ticks > 1_000_000)
            ticks = 0;

        if(this.ticks % 200 == 100)
        {
            boolean changed = this.updateSheltered();
            changed |= this.updateOwner();
        }
    }

    private boolean updateOwner()
    {
        if(this.ownerChocobo != null) return false;
        List<EntityChocobo> chocobos = this.world.getEntitiesWithinAABB(EntityChocobo.class, new AxisAlignedBB(this.pos).expand(16, 8, 16));
        double dist = Double.MAX_VALUE;
        EntityChocobo closestChocobo = null;
        for(EntityChocobo chocobo : chocobos)
        {
            double d = chocobo.getPosition().distanceSq(this.pos);
            if(d < dist)
            {
                dist = d;
                closestChocobo = chocobo;
            }
        }

        if(closestChocobo != null)
        {
            this.ownerChocobo = closestChocobo.getUniqueID();
            closestChocobo.setNestPosition(this.pos);
        }
        return this.ownerChocobo != null;
    }

    private boolean updateSheltered()
    {
        boolean changed = false;
        // TODO: Make this better, use "can see sky" for shelter detection
        for(CheckOffset checkOffset : SHELTER_CHECK_OFFSETS)
        {
            if(world.isAirBlock(this.getPos().add(checkOffset.offset)) != checkOffset.shouldBeAir)
            {
                if(this.isSheltered)
                    changed = true;
                this.isSheltered = false;
                return changed;
            }
        }
        if(!this.isSheltered)
            changed = true;
        this.isSheltered = true;
        return changed;
    }

    public ItemStack getEggItemStack()
    {
        return this.inventory.getStackInSlot(0);
    }

    public void setEggItemStack(ItemStack itemStack)
    {
        if(itemStack.isEmpty())
            this.inventory.setStackInSlot(0, ItemStack.EMPTY);
        else if(BlockChocoboEgg.isChocoboEgg(itemStack))
            this.inventory.setStackInSlot(0, itemStack);
    }

    public IItemHandler getInventory()
    {
        return this.inventory;
    }

    //region Data Synchronization/Persistence
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.isSheltered = nbt.getBoolean("IsSheltered");
        this.ticks = nbt.getInteger("Ticks");
        if(nbt.hasKey("Chocobo"))
            this.ownerChocobo = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("Chocobo"));
        this.inventory.deserializeNBT(nbt.getCompoundTag("Inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setBoolean("IsSheltered", this.isSheltered);
        nbt.setInteger("Ticks", this.ticks);
        if(this.ownerChocobo != null)
            nbt.setTag("Chocobo", NBTUtil.createUUIDTag(this.ownerChocobo));
        nbt.setTag("Inventory", this.inventory.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Inventory", this.inventory.serializeNBT());
        return new SPacketUpdateTileEntity(this.getPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.inventory.deserializeNBT(pkt.getNbtCompound().getCompoundTag("Inventory"));
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag("Inventory", this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt)
    {
        super.handleUpdateTag(nbt);
        this.inventory.deserializeNBT(nbt.getCompoundTag("Inventory"));
    }
    //endregion

    private static class CheckOffset
    {
        Vec3i offset;
        boolean shouldBeAir;

        CheckOffset(Vec3i offset, boolean shouldBeAir)
        {
            this.offset = offset;
            this.shouldBeAir = shouldBeAir;
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation(Chococraft.MODID + ".container.nest");
    }

    public void onInventoryChanged()
    {
        this.markDirty();
        IBlockState newState = ModBlocks.strawNest.getDefaultState().withProperty(BlockStrawNest.HAS_EGG, !this.getEggItemStack().isEmpty());
        this.getWorld().setBlockState(this.getPos(), newState);
    }
}
