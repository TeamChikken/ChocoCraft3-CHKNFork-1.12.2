package net.xalcon.chococraft.common.entities;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityChocobo extends EntityTameable
{
    private static final DataParameter<ChocoboColor> PARAM_VARIANT = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.CHOCOBO_COLOR);
    private static final DataParameter<BagType> PARAM_BAG_TYPE = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.BAG_TYPE);
    private static final DataParameter<Boolean> PARAM_SADDLED = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PARAM_IS_MALE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private static final DataParameter<MovementType> PARAM_MOVEMENT_TYPE = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.MOVEMENT_TYPE);

    private final RiderState riderState;

    public float wingRotation;
    public float destPos;

    public BagType getBagType()
    {
        return this.dataManager.get(PARAM_BAG_TYPE);
    }

    public boolean isSaddled()
    {
        return this.dataManager.get(PARAM_SADDLED);
    }

    public boolean isMale()
    {
        return this.dataManager.get(PARAM_IS_MALE);
    }

    public enum ChocoboColor
    {
        YELLOW,
        GREEN,
        BLUE,
        WHITE,
        BLACK,
        GOLD,
        PINK,
        RED,
        PURPLE
    }

    public enum BagType
    {
        NONE, SADDLE, PACK
    }

    public enum MovementType
    {
        WANDER, FOLLOW_OWNER, STANDSTILL, FOLLOW_LURE
    }

    public EntityChocobo(World world)
    {
        super(world);
        this.setSize(1.3f, 2.3f);
        // TODO: this.setMale(world.rand.nextBoolean());
        // TODO: setCustomNameTag(DefaultNames.getRandomName(isMale()));
        // TODO: this.resetFeatherDropTime();
        this.riderState = new RiderState();
        // TODO: Does this still exist? ((PathNavigateGround) this.getNavigator()).set(true);
        this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 0.6D));
        // TODO: this.tasks.addTask(1, new ChocoboAIFollowOwner(this, 1.0D, 5.0F, 5.0F));// follow speed 1, min and max 5
        // TODO: this.tasks.addTask(1, new ChocoboAIFollowLure(this, 1.0D, 5.0F, 5.0F));
        // TODO: this.tasks.addTask(1, new ChocoboAIWatchPlayer(this, EntityPlayer.class, 5));
        // TODO: this.tasks.addTask(2, new ChocoboAIMate(this, 1.0D));
        this.tasks.addTask(1, new EntityAISwimming(this));


        // TODO: initChest();
        // TODO: Implement Hell Chocobo

        this.setColor(ChocoboColor.YELLOW);
        this.isImmuneToFire = getAbilityInfo().isImmuneToFire();
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(PARAM_VARIANT, ChocoboColor.YELLOW);
        this.dataManager.register(PARAM_BAG_TYPE, BagType.NONE);
        this.dataManager.register(PARAM_SADDLED, false);
        this.dataManager.register(PARAM_IS_MALE, false);
        this.dataManager.register(PARAM_MOVEMENT_TYPE, MovementType.WANDER);
    }

    // TODO: implement mounting

    public RiderState getRiderState()
    {
        return riderState;
    }

    public void setColor(ChocoboColor color)
    {
        this.dataManager.set(PARAM_VARIANT, color);
        // TODO: setStats();
    }

    public ChocoboColor getChocoboColor()
    {
        return this.dataManager.get(PARAM_VARIANT);
    }

    public ChocoboAbilityInfo getAbilityInfo()
    {
        return ChocoboAbilityInfo.getAbilityInfo(this.getChocoboColor());
    }

    @Override
    public double getMountedYOffset()
{
    return 1.65D;
}

    @Override
    public float getJumpUpwardsMotion()
    {
        return 0.5f;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        // TODO: implement breeding
        return false;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return null;
    }
}
