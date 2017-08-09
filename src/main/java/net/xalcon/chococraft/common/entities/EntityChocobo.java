package net.xalcon.chococraft.common.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.ChocoConfig;
import net.xalcon.chococraft.common.entities.breeding.Breeding;
import net.xalcon.chococraft.common.init.ModItems;
import net.xalcon.chococraft.common.inventory.ContainerSaddleBag;
import net.xalcon.chococraft.common.inventory.SaddleItemStackHandler;
import net.xalcon.chococraft.common.network.PacketManager;
import net.xalcon.chococraft.common.network.packets.PacketChocoboJump;
import net.xalcon.chococraft.common.network.packets.PacketOpenChocoboGui;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.Collections;

public class EntityChocobo extends EntityTameable
{
    private static final DataParameter<ChocoboColor> PARAM_VARIANT = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.CHOCOBO_COLOR);
    private static final DataParameter<Boolean> PARAM_IS_MALE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
    private static final DataParameter<MovementType> PARAM_MOVEMENT_TYPE = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.MOVEMENT_TYPE);
    private static final DataParameter<ItemStack> PARAM_SADDLE_ITEM = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.ITEM_STACK);

    private final RiderState riderState;
    public final ItemStackHandler chocoboInventory = new ItemStackHandler();
    public final SaddleItemStackHandler saddleItemStackHandler = new SaddleItemStackHandler()
    {
        @Override
        protected void onStackChanged()
        {
            EntityChocobo.this.setSaddleType(this.itemStack);
        }
    };

    private float wingRotDelta;
    public float wingRotation;
    public float destPos;
    public boolean fedGoldenGyshal;

    public SaddleType getSaddleType()
    {
        return ModItems.chocoboSaddle.getSaddleType(this.dataManager.get(PARAM_SADDLE_ITEM));
    }

    private void setSaddleType(ItemStack saddleStack)
    {
        SaddleType newType = ModItems.chocoboSaddle.getSaddleType(saddleStack);
        SaddleType oldType = this.getSaddleType();
        if (oldType != newType)
        {
            this.dataManager.set(PARAM_SADDLE_ITEM, saddleStack.copy());
            this.reconfigureInventory(oldType, newType);
        }
    }

    private void reconfigureInventory(SaddleType oldType, SaddleType newType)
    {
        if(!this.getEntityWorld().isRemote)
        {
            /*if(this.chocoboInventory.getSlots() > newType.getInventorySize())
            {
                // inventory is getting smaller, drop overflow
                for(int i = newType.getInventorySize(); i < this.chocoboInventory.getSlots(); i++)
                {
                    ItemStack stack = this.chocoboInventory.extractItem(i, Integer.MAX_VALUE, false);
                    InventoryHelper.spawnItemStack(this.getEntityWorld(), this.posX, this.posY + .5, this.posZ, stack);
                }
            }*/
            // TODO: Handle resizing. ItemStackHandler#setSize() clears the internal inventory!
            for(int i = 0; i < this.chocoboInventory.getSlots(); i++)
            {
                ItemStack stack = this.chocoboInventory.extractItem(i, Integer.MAX_VALUE, false);
                InventoryHelper.spawnItemStack(this.getEntityWorld(), this.posX, this.posY + .5, this.posZ, stack);
            }
        }
        this.chocoboInventory.setSize(newType.getInventorySize());

        for(EntityPlayer player : world.playerEntities)
        {
            if(player.openContainer instanceof ContainerSaddleBag)
                ((ContainerSaddleBag) player.openContainer).refreshSlots(this, player);
        }
    }

    public boolean isSaddled()
    {
        return this.getSaddleType().isRidingSaddle();
    }

    public boolean isMale()
    {
        return this.dataManager.get(PARAM_IS_MALE);
    }

    public void setMale(boolean isMale)
    {
        this.dataManager.set(PARAM_IS_MALE, isMale);
    }

    public MovementType getMovementType()
    {
        return this.dataManager.get(PARAM_MOVEMENT_TYPE);
    }

    public void setMovementType(MovementType type)
    {
        this.dataManager.set(PARAM_MOVEMENT_TYPE, type);
    }

    public EntityChocobo(World world)
    {
        super(world);
        this.setSize(1.3f, 2.3f);
        // TODO: setCustomNameTag(DefaultNames.getRandomName(isMale()));
        // TODO: this.resetFeatherDropTime();
        this.riderState = new RiderState();
        updateStats();
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        this.setMale(this.world.rand.nextBoolean());
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void initEntityAI()
    {
        // TODO: Does this still exist? ((PathNavigateGround) this.getNavigator()).set(true);
        // TODO: implement follow owner or player with feather (if not tamed)
        // this.tasks.addTask(1, new ChocoboAIFollowOwner(this, 1.0D, 5.0F, 5.0F));// follow speed 1, min and max 5
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, false, Collections.singleton(ModItems.gysahlGreen)));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
    }

    private void updateStats()
    {
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getAbilityInfo().getMaxHP());// set max health
        setHealth(getMaxHealth());// reset the hp to max
        onGroundSpeedFactor = this.getAbilityInfo().getLandSpeed() / 100f;
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(onGroundSpeedFactor);
        this.isImmuneToFire = getAbilityInfo().isImmuneToFire();
        this.stepHeight = this.getAbilityInfo().getStepHeight(true);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(PARAM_VARIANT, ChocoboColor.YELLOW);
        this.dataManager.register(PARAM_IS_MALE, false);
        this.dataManager.register(PARAM_MOVEMENT_TYPE, MovementType.WANDER);
        this.dataManager.register(PARAM_SADDLE_ITEM, ItemStack.EMPTY);
    }

    // TODO: implement mounting

    public RiderState getRiderState()
    {
        return riderState;
    }

    public void setColor(ChocoboColor color)
    {
        this.dataManager.set(PARAM_VARIANT, color);
        this.updateStats();
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
        return stack.getItem() == ModItems.lovelyGysahlGreen;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        EntityChocobo baby = new EntityChocobo(this.getEntityWorld());
        baby.setColor(Breeding.getColour(this, (EntityChocobo) entity));
        this.fedGoldenGyshal = false;
        ((EntityChocobo) entity).fedGoldenGyshal = false;
        return baby;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (this.getEntityWorld().isRemote) return true;

        ItemStack heldItemStack = player.getHeldItem(hand);

        if(this.isTamed() && player.isSneaking())
        {
            if(player instanceof EntityPlayerMP)
                this.displayChocoboInventory((EntityPlayerMP) player);
            return true;
        }

        if (this.isSaddled() && heldItemStack.isEmpty() && !player.isSneaking())
        {
            player.startRiding(this);
            return true;
        }

        if (heldItemStack.getItem() == ModItems.gysahlGreen)
        {
            this.consumeItemFromStack(player, player.inventory.getCurrentItem());
            if (world.rand.nextFloat() < ChocoConfig.chocobo.tameChance)
            {
                this.setOwnerId(player.getUniqueID());
                this.setTamed(true);
                player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.tame_success"), true);
            }
            else
            {
                player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.tame_fail"), true);
            }
            return true;
        }

        if (heldItemStack.getItem() == ModItems.chocoboSaddle && this.isTamed() && !this.isSaddled())
        {
            player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.saddle_applied"), true);
            this.saddleItemStackHandler.setStackInSlot(0, heldItemStack.copy().splitStack(1));
            this.consumeItemFromStack(player, heldItemStack);
            this.setSaddleType(heldItemStack);
            return true;
        }

        return super.processInteract(player, hand);
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

            if (this.riderState.isJumping() && !this.isJumping && this.onGround)
            {
                this.motionY += 0.75;
                this.riderState.setJumping(false);
                this.isJumping = true;
            }

            if (this.canPassengerSteer())
            {
                this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
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
                // :thonk: no idea why this is not crashing on a dedicated server, but we might want to move this somewhere else
                if (Minecraft.getMinecraft().player.getUniqueID().equals(riddenByEntity.getUniqueID()) && Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                    this.riderState.setJumping(true);

                if (this.riderState.hasChanged())
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
    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("Color", (byte) this.getChocoboColor().ordinal());
        nbt.setBoolean("Male", this.isMale());
        nbt.setByte("MovementType", (byte) this.getMovementType().ordinal());
        nbt.setTag("Saddle", this.saddleItemStackHandler.serializeNBT());

        if (getSaddleType() != SaddleType.NONE)
            nbt.setTag("Inventory", this.chocoboInventory.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.setColor(ChocoboColor.values()[nbt.getByte("Color")]);
        this.setMale(nbt.getBoolean("Male"));
        this.setMovementType(MovementType.values()[nbt.getByte("MovementType")]);
        this.saddleItemStackHandler.deserializeNBT(nbt.getCompoundTag("Saddle"));

        if (getSaddleType() != SaddleType.NONE)
            this.chocoboInventory.deserializeNBT(nbt.getCompoundTag("Inventory"));
    }

    private void displayChocoboInventory(EntityPlayerMP player)
    {
        player.getNextWindowId();
        player.openContainer = new ContainerSaddleBag(this, player);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addListener(player);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.openContainer));
        PacketManager.INSTANCE.sendTo(new PacketOpenChocoboGui(this, player.currentWindowId), player);
    }
}
