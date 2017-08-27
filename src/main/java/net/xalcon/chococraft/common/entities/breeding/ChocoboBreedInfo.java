package net.xalcon.chococraft.common.entities.breeding;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class ChocoboBreedInfo
{
    private ChocoboStatSnapshot mother = ChocoboStatSnapshot.DEFAULT;
    private ChocoboStatSnapshot father = ChocoboStatSnapshot.DEFAULT;

    public ChocoboStatSnapshot getMother()
    {
        return this.mother;
    }

    public ChocoboStatSnapshot getFather()
    {
        return this.father;
    }

    public ChocoboBreedInfo(ChocoboStatSnapshot mother, ChocoboStatSnapshot father)
    {
        this.mother = mother;
        this.father = father;
    }

    public ChocoboBreedInfo(NBTTagCompound nbt)
    {
        if(nbt.hasKey("mother"))
            this.mother = new ChocoboStatSnapshot(nbt.getCompoundTag("mother"));
        if(nbt.hasKey("father"))
            this.father = new ChocoboStatSnapshot(nbt.getCompoundTag("father"));
    }

    public NBTTagCompound serialize()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("mother", this.mother.serialize());
        nbt.setTag("father", this.father.serialize());
        return nbt;
    }

    public static ChocoboBreedInfo getFromNbtOrDefault(@Nullable NBTTagCompound nbt)
    {
        return nbt != null ? new ChocoboBreedInfo(nbt) : new ChocoboBreedInfo(ChocoboStatSnapshot.DEFAULT, ChocoboStatSnapshot.DEFAULT);
    }
}
