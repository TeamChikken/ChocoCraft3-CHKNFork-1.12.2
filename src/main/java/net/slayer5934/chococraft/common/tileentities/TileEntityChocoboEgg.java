package net.slayer5934.chococraft.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.slayer5934.chococraft.common.blocks.BlockChocoboEgg;
import net.slayer5934.chococraft.common.entities.breeding.ChocoboBreedInfo;

import javax.annotation.Nullable;

public class TileEntityChocoboEgg extends TileEntity
{
    public final static String NBTKEY_BREEDINFO = "BreedInfo";
    private ChocoboBreedInfo breedInfo;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.breedInfo = new ChocoboBreedInfo(nbt.getCompoundTag(NBTKEY_BREEDINFO));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag(NBTKEY_BREEDINFO, this.breedInfo.serialize());
        return super.writeToNBT(nbt);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag(NBTKEY_BREEDINFO, this.breedInfo.serialize());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt)
    {
        super.handleUpdateTag(nbt);
        this.breedInfo = new ChocoboBreedInfo(nbt.getCompoundTag(NBTKEY_BREEDINFO));
    }

    @Nullable
    public ChocoboBreedInfo getBreedInfo()
    {
        return this.breedInfo;
    }

    public void setBreedInfo(ChocoboBreedInfo breedInfo)
    {
        this.breedInfo = breedInfo;
    }
}
