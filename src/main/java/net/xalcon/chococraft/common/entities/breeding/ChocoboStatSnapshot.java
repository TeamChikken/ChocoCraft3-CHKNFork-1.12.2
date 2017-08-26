package net.xalcon.chococraft.common.entities.breeding;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.properties.ChocoboAttributes;
import net.xalcon.chococraft.common.entities.properties.ChocoboColor;

public class ChocoboStatSnapshot
{
    public static final ChocoboStatSnapshot DEFAULT;
    public int level;
    public int generation;
    public float health;
    public float speed;
    public float stamina;
    public boolean canSprint;
    public boolean canGlide;
    public boolean canDive;
    public boolean canFly;

    static
    {
        DEFAULT = new ChocoboStatSnapshot();
        DEFAULT.generation = 1;
        DEFAULT.level = 1;
        DEFAULT.health = 20;
        DEFAULT.stamina = 10f;
        DEFAULT.speed = 0.7f;

        DEFAULT.canSprint = false;
        DEFAULT.canGlide = false;
        DEFAULT.canDive = false;
        DEFAULT.canFly = false;
    }

    private ChocoboStatSnapshot() { }

    public ChocoboStatSnapshot(EntityChocobo chocobo)
    {
        this.level = chocobo.getLevel();
        this.generation = chocobo.getGeneration();
        this.health = (float) chocobo.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
        this.speed = (float) chocobo.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
        this.stamina = (float) chocobo.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).getBaseValue();

        this.canSprint = chocobo.canSprint();
        this.canGlide = chocobo.canDive();
        this.canDive = chocobo.canDive();
        this.canFly = chocobo.canFly();
    }

    public ChocoboStatSnapshot(NBTTagCompound nbt)
    {
        this.level = nbt.getInteger("Level");
        this.generation = nbt.getInteger("Generation");
        this.health = nbt.getFloat("Health");
        this.speed = nbt.getFloat("Speed");
        this.stamina = nbt.getFloat("Stamina");

        this.canSprint = nbt.getBoolean("CanSprint");
        this.canGlide = nbt.getBoolean("CanGlide");
        this.canDive = nbt.getBoolean("CanDive");
        this.canFly = nbt.getBoolean("CanFly");
    }

    public NBTTagCompound serialize()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Level", this.level);
        nbt.setInteger("Generation", this.generation);
        nbt.setFloat("Health", this.health);
        nbt.setFloat("Speed", this.speed);
        nbt.setFloat("Stamina", this.stamina);

        nbt.setBoolean("CanFly", this.canFly);
        nbt.setBoolean("CanGlide", this.canGlide);
        nbt.setBoolean("CanSprint", this.canSprint);
        nbt.setBoolean("CanDive", this.canDive);
        return nbt;
    }
}
