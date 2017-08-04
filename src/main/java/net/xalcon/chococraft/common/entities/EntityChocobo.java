package net.xalcon.chococraft.common.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.init.ModItems;
import net.xalcon.chococraft.common.network.PacketManager;
import net.xalcon.chococraft.common.network.packets.PacketChocoboJump;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.Collections;

public class EntityChocobo extends EntityTameable
{
    private static final DataParameter<ChocoboColor> PARAM_VARIANT = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.CHOCOBO_COLOR);
    private static final DataParameter<BagType> PARAM_BAG_TYPE = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.BAG_TYPE);
    private static final DataParameter<Boolean> PARAM_SADDLED = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PARAM_IS_MALE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private static final DataParameter<MovementType> PARAM_MOVEMENT_TYPE = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.MOVEMENT_TYPE);

    private final RiderState riderState;

    private float wingRotDelta;
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

    public void setSaddled(boolean isSaddled)
    {
        this.dataManager.set(PARAM_SADDLED, isSaddled);
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
        // TODO: implement follow owner or player with feather (if not tamed)
        // this.tasks.addTask(1, new ChocoboAIFollowOwner(this, 1.0D, 5.0F, 5.0F));// follow speed 1, min and max 5
        // TODO: this.tasks.addTask(2, new ChocoboAIMate(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, false, Collections.singleton(ModItems.gysahlGreen)));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(1, new EntityAISwimming(this));


        // TODO: initChest();
        // TODO: Implement Hell Chocobo

        this.setColor(ChocoboColor.YELLOW);
        this.isImmuneToFire = getAbilityInfo().isImmuneToFire();
        this.updateStats();
    }

    private void updateStats()
    {
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getAbilityInfo().getMaxHP());// set max health
        setHealth(getMaxHealth());// reset the hp to max
        onGroundSpeedFactor = this.getAbilityInfo().getLandSpeed() / 100f;
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(onGroundSpeedFactor);
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

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (this.getEntityWorld().isRemote) return true;

        if (this.isSaddled() && player.getHeldItem(hand).isEmpty() && !player.isSneaking())
            player.startRiding(this);
        else if (player.getHeldItem(hand).getItem() == ModItems.gysahlGreen)
            tryTame(player);
        else if (player.getHeldItem(hand).getItem() == ModItems.chocoboSaddle && this.isTamed() && !this.isSaddled())
            tryApplySaddle(player);
        return true;
    }

    private void tryApplySaddle(EntityPlayer player)
    {
        this.consumeItemFromStack(player, player.inventory.getCurrentItem());
        player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.saddle_applied"), true);
        this.setSaddled(true);
    }

    @Nullable
    public Entity getControllingPassenger()
    {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered()
    {
        return this.isTamed();
    }

    @Override
    public void travel(float strafe, float vertical, float forward)
    {
        // OLD TODO some point in future, move this to its own ai class
        EntityLivingBase rider = (EntityLivingBase) this.getControllingPassenger();
        if (rider != null)
        {
            this.prevRotationYaw = this.rotationYaw = rider.rotationYaw;
            this.rotationPitch = rider.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            strafe = rider.moveStrafing * 0.5F;
            forward = rider.moveForward;

            if (forward <= 0.0F)
            {
                forward *= 0.25F;
            }

            if(isInWater() && this.getAbilityInfo().canWalkOnWater()) {
                motionY = 0.4d;
                //moveFlying(strafe, forward, 100 / getAbilityInfo().getWaterSpeed());
                setJumping(true);
            }

            if (this.riderState.isJumping() && this.getAbilityInfo().getCanFly())
            {
                this.isJumping = true;
                this.jump();
                //moveFlying(strafe, forward, 100 / getAbilityInfo().getAirbornSpeed());
            }
            else if (this.riderState.isJumping() && !this.isJumping && this.onGround)
            {
                this.motionY += 0.75;
                this.riderState.setJumping(false);
                this.isJumping = true;
            }

            if (this.canPassengerSteer())
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafe, vertical, forward);
            }
            else if (rider instanceof EntityPlayer)
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            if (this.onGround)
            {
                this.isJumping = false;
                this.riderState.setJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f4 > 1.0F)
            {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.getAbilityInfo().canClimb() || this.isBeingRidden())
            this.stepHeight = 1.0F;

        this.fallDistance = 0f;

        Entity riddenByEntity = this.getControllingPassenger();

        // Wing rotations, control packet, client side
        if (this.getEntityWorld().isRemote)
        {
            // Client side
            if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer)
            {
                // TODO: THIS CRASHES ON A DEDICATED SERVER! MOVE THIS SOMEWHERE ELSE! Maybe in a keyboard event handler
                if (Minecraft.getMinecraft().player.getUniqueID().equals(riddenByEntity.getUniqueID()) && Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                    this.riderState.setJumping(true);

                if(this.riderState.hasChanged())
                {
                    PacketManager.INSTANCE.sendToServer(new PacketChocoboJump(this.riderState));
                    this.riderState.resetChanged();
                }
            }

            this.destPos += (double) (this.onGround ? -1 : 4) * 0.3D;
            this.destPos = MathHelper.clamp(destPos, 0f, 1f);

            if (!this.onGround)
                this.wingRotDelta = Math.min(wingRotation, 1f);

            this.wingRotDelta *= 0.9D;

            if (!this.onGround && this.motionY < 0.0D)
                this.motionY *= 0.8D;
            this.wingRotation += this.wingRotDelta * 2.0F;

            return;// Rest of code should be run on server only
        }
    }

    @Override
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked();
    }

    @Override
    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return true;
    }

    private void tryTame(EntityPlayer player)
    {
        if (!isTamed())
        {
            this.consumeItemFromStack(player, player.inventory.getCurrentItem());
            if (world.rand.nextFloat() < 0.1)
            {
                //Successfull tame
                this.setOwnerId(player.getUniqueID());
                this.setTamed(true);
                // TODO: implement chocopedia - InventoryHelper.giveIfMissing(new ItemStack(Additions.chocopediaItem), (EntityPlayerMP) player);
                player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.tame_success"), true);
            } else
            {
                player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.tame_fail"), true);
            }
        }
    }
}
