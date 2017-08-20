package net.xalcon.chococraft.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.xalcon.chococraft.common.entities.breeding.ChocoboBreedInfo;

public class TileEntityChocoboEgg extends TileEntity
{
    private ChocoboBreedInfo breedInfo;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.breedInfo = new ChocoboBreedInfo(nbt.getCompoundTag("BreedInfo"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("BreedInfo", this.breedInfo.serialize());
        return super.writeToNBT(nbt);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag("BreedInfo", this.breedInfo.serialize());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt)
    {
        super.handleUpdateTag(nbt);
        this.breedInfo = new ChocoboBreedInfo(nbt.getCompoundTag("BreedInfo"));
    }
}
