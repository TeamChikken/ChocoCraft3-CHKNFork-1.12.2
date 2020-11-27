package net.slayer5934.chococraft.common.entities.breeding;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

public class ChocoboBreedInfo
{
    public final static String NBTKEY_MOTHER_STATSNAPSHOT = "mother";
    public final static String NBTKEY_FATHER_STATSNAPSHOT = "father";

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
        if(nbt.hasKey(NBTKEY_MOTHER_STATSNAPSHOT))
            this.mother = new ChocoboStatSnapshot(nbt.getCompoundTag(NBTKEY_MOTHER_STATSNAPSHOT));
        if(nbt.hasKey(NBTKEY_FATHER_STATSNAPSHOT))
            this.father = new ChocoboStatSnapshot(nbt.getCompoundTag(NBTKEY_FATHER_STATSNAPSHOT));
    }

    public NBTTagCompound serialize()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag(NBTKEY_MOTHER_STATSNAPSHOT, this.mother.serialize());
        nbt.setTag(NBTKEY_FATHER_STATSNAPSHOT, this.father.serialize());
        return nbt;
    }

    public static ChocoboBreedInfo getFromNbtOrDefault(@Nullable NBTTagCompound nbt)
    {
        return nbt != null ? new ChocoboBreedInfo(nbt) : new ChocoboBreedInfo(ChocoboStatSnapshot.DEFAULT, ChocoboStatSnapshot.DEFAULT);
    }
}
