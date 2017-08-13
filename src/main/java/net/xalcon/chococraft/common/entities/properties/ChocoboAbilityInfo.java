package net.xalcon.chococraft.common.entities.properties;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.xalcon.chococraft.common.entities.EntityChocobo;

public class ChocoboAbilityInfo
{
    private final static DataParameter<Integer> LEVEL = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.VARINT);
    private final static DataParameter<Float> HEALTH = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.FLOAT);
    private final static DataParameter<Float> RESISTANCE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.FLOAT);
    private final static DataParameter<Float> SPEED = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.FLOAT);
    private final static DataParameter<Float> STAMINA = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.FLOAT);

    private final static DataParameter<Boolean> CAN_FLY = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private final static DataParameter<Boolean> CAN_GLIDE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private final static DataParameter<Boolean> CAN_SPRINT = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private final static DataParameter<Boolean> CAN_DIVE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);

    private EntityChocobo chocobo;

    public int getLevel() { return this.chocobo.getDataManager().get(LEVEL); }
    public void setLevel(int value) { this.chocobo.getDataManager().set(LEVEL, value); }
    public float getHealth() { return this.chocobo.getDataManager().get(HEALTH); }
    public void setHealth(float value) { this.chocobo.getDataManager().set(HEALTH, value); }
    public float getResistance() { return this.chocobo.getDataManager().get(RESISTANCE); }
    public void setResistance(float value) { this.chocobo.getDataManager().set(RESISTANCE, value); }
    public float getSpeed() { return this.chocobo.getDataManager().get(SPEED); }
    public void setSpeed(float value) { this.chocobo.getDataManager().set(SPEED, value); }
    public float getStamina() { return this.chocobo.getDataManager().get(STAMINA); }
    public void setStamina(float value) { this.chocobo.getDataManager().set(STAMINA, value); }

    public boolean canFly() { return this.chocobo.getDataManager().get(CAN_FLY); }
    public void setCanFly(boolean state) { this.chocobo.getDataManager().set(CAN_FLY, state); }
    public boolean canGlide() { return this.chocobo.getDataManager().get(CAN_GLIDE); }
    public void setCanGlide(boolean state) { this.chocobo.getDataManager().set(CAN_GLIDE, state); }
    public boolean canSprint() { return this.chocobo.getDataManager().get(CAN_SPRINT); }
    public void setCanSprint(boolean state) { this.chocobo.getDataManager().set(CAN_SPRINT, state); }
    public boolean canDive() { return this.chocobo.getDataManager().get(CAN_DIVE); }
    public void setCanDive(boolean state) { this.chocobo.getDataManager().set(CAN_DIVE, state); }

    public ChocoboAbilityInfo(EntityChocobo chocobo)
    {
        this.chocobo = chocobo;
    }

    public void registerDataParameters()
    {
        this.chocobo.getDataManager().register(LEVEL, 1);
        this.chocobo.getDataManager().register(HEALTH, 20f);
        this.chocobo.getDataManager().register(RESISTANCE, 0f);
        this.chocobo.getDataManager().register(SPEED, 1f);
        this.chocobo.getDataManager().register(STAMINA, 10f);

        this.chocobo.getDataManager().register(CAN_FLY, false);
        this.chocobo.getDataManager().register(CAN_GLIDE, false);
        this.chocobo.getDataManager().register(CAN_SPRINT, false);
        this.chocobo.getDataManager().register(CAN_DIVE, false);
    }

    public void deserializeNbt(NBTTagCompound nbt)
    {
        this.setLevel(nbt.getInteger("level"));
        this.setHealth(nbt.getFloat("health"));
        this.setResistance(nbt.getFloat("resistance"));
        this.setSpeed(nbt.getFloat("speed"));
        this.setStamina(nbt.getFloat("stamina"));

        this.setCanFly(nbt.getBoolean("canFly"));
        this.setCanGlide(nbt.getBoolean("canGlide"));
        this.setCanSprint(nbt.getBoolean("canSprint"));
        this.setCanDive(nbt.getBoolean("canDive"));
    }

    public NBTTagCompound serializeNbt()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setInteger("level", this.getLevel());
        nbt.setFloat("health", this.getHealth());
        nbt.setFloat("resistance", this.getResistance());
        nbt.setFloat("speed", this.getSpeed());
        nbt.setFloat("stamina", this.getStamina());

        nbt.setBoolean("canFly", this.canFly());
        nbt.setBoolean("canGlide", this.canGlide());
        nbt.setBoolean("canSprint", this.canSprint());
        nbt.setBoolean("canDive", this.canDive());

        return nbt;
    }
}
