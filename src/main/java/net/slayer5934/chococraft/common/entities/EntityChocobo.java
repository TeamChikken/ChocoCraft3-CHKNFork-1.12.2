package net.slayer5934.chococraft.common.entities;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.ChocoConfig;
import net.slayer5934.chococraft.common.entities.breeding.EntityChocoboAIMate;
import net.slayer5934.chococraft.common.entities.properties.ChocoboAttributes;
import net.slayer5934.chococraft.common.entities.properties.ChocoboColor;
import net.slayer5934.chococraft.common.entities.properties.EntityDataSerializers;
import net.slayer5934.chococraft.common.entities.properties.MovementType;
import net.slayer5934.chococraft.common.entities.properties.SaddleType;
import net.slayer5934.chococraft.common.init.ModItems;
import net.slayer5934.chococraft.common.init.ModSounds;
import net.slayer5934.chococraft.common.inventory.ContainerSaddleBag;
import net.slayer5934.chococraft.common.inventory.SaddleItemStackHandler;
import net.slayer5934.chococraft.common.network.PacketManager;
import net.slayer5934.chococraft.common.network.packets.PacketOpenChocoboGui;
import net.slayer5934.chococraft.utils.WorldUtils;

@SuppressWarnings("WeakerAccess")
public class EntityChocobo extends EntityTameable
{
	private static final ResourceLocation CHOCOBO_LOOTABLE = new ResourceLocation(Chococraft.MODID, "entities/chocobo");
	public static final String NBTKEY_CHOCOBO_COLOR = "Color";
	public static final String NBTKEY_CHOCOBO_IS_MALE = "Male";
	public static final String NBTKEY_MOVEMENTTYPE = "MovementType";
	public static final String NBTKEY_SADDLE_ITEM = "Saddle";
	public static final String NBTKEY_INVENTORY = "Inventory";
	public static final String NBTKEY_NEST_POSITION = "NestPos";
	public static final String NBTKEY_CHOCOBO_LEVEL = "Level";
	public static final String NBTKEY_CHOCOBO_GENERATION = "Generation";
	public static final String NBTKEY_CHOCOBO_STAMINA = "Stamina";
	public static final String NBTKEY_CHOCOBO_CAN_FLY = "CanFly";
	public static final String NBTKEY_CHOCOBO_CAN_GLIDE = "CanGlide";
	public static final String NBTKEY_CHOCOBO_CAN_SPRINT = "CanSprint";
	public static final String NBTKEY_CHOCOBO_CAN_DIVE = "CanDive";

	private static final byte CAN_SPRINT_BIT = 0b0001;
	private static final byte CAN_DIVE_BIT = 0b0010;
	private static final byte CAN_GLIDE_BIT = 0b0100;
	private static final byte CAN_FLY_BIT = 0b1000;

	private static final DataParameter<ChocoboColor> PARAM_COLOR = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.CHOCOBO_COLOR);
	private static final DataParameter<Boolean> PARAM_IS_MALE = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<MovementType> PARAM_MOVEMENT_TYPE = EntityDataManager.createKey(EntityChocobo.class, EntityDataSerializers.MOVEMENT_TYPE);
	private static final DataParameter<ItemStack> PARAM_SADDLE_ITEM = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.ITEM_STACK);

	private final static DataParameter<Integer> PARAM_LEVEL = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.VARINT);
	private final static DataParameter<Integer> PARAM_GENERATION = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.VARINT);
	private final static DataParameter<Float> PARAM_STAMINA = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.FLOAT);
	private final static DataParameter<Byte> PARAM_ABILITY_MASK = EntityDataManager.createKey(EntityChocobo.class, DataSerializers.BYTE);

    private static final UUID CHOCOBO_SPRINTING_BOOST_ID = UUID.fromString("03ba3167-393e-4362-92b8-909841047640");
    private static final AttributeModifier CHOCOBO_SPRINTING_SPEED_BOOST = (new AttributeModifier(CHOCOBO_SPRINTING_BOOST_ID, "Chocobo sprinting speed boost", 1, 1)).setSaved(false);


    public final ItemStackHandler chocoboInventory = new ItemStackHandler();
	public final SaddleItemStackHandler saddleItemStackHandler = new SaddleItemStackHandler()
	{
		@Override
		protected void onStackChanged()
		{
			EntityChocobo.this.setSaddleType(this.itemStack);
		}
	};

	public float wingRotation;
	public float destPos;
	private boolean isChocoboJumping;
	private float wingRotDelta;
	private BlockPos nestPos;

    public EntityChocobo(World world)
	{
		super(world);
		this.setSize(1.2f, 2.8f);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(2, new EntityChocoboAIMate(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
		this.tasks.addTask(4, new EntityAITempt(this, 1.2D, false, Collections.singleton(ModItems.gysahlGreen)));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
	}
	
	private final EntityAIFollowOwner follow = new EntityAIFollowOwner(this, 2.0D, 3.0F, 10.0F);
	public float followingmrhuman = 2;

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(ChocoboAttributes.MAX_STAMINA).setBaseValue(ChocoConfig.chocobo.defaultStamina);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ChocoConfig.chocobo.defaultSpeed / 100f);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ChocoConfig.chocobo.defaultHealth);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(PARAM_COLOR, ChocoboColor.YELLOW);
		this.dataManager.register(PARAM_IS_MALE, false);
		this.dataManager.register(PARAM_MOVEMENT_TYPE, MovementType.WANDER);
		this.dataManager.register(PARAM_SADDLE_ITEM, ItemStack.EMPTY);

		this.dataManager.register(PARAM_LEVEL, 1);
		this.dataManager.register(PARAM_STAMINA, (float)ChocoConfig.chocobo.defaultStamina);
		this.dataManager.register(PARAM_GENERATION, 0);
		this.dataManager.register(PARAM_ABILITY_MASK, (byte)0);
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable()
	{
		return CHOCOBO_LOOTABLE;
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		this.setMale(this.world.rand.nextBoolean());
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public boolean canBeSteered()
	{
		return this.isTamed();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.setChocoboColor(ChocoboColor.values()[nbt.getByte(NBTKEY_CHOCOBO_COLOR)]);
		this.setMale(nbt.getBoolean(NBTKEY_CHOCOBO_IS_MALE));
		this.setMovementType(MovementType.values()[nbt.getByte(NBTKEY_MOVEMENTTYPE)]);
		this.saddleItemStackHandler.deserializeNBT(nbt.getCompoundTag(NBTKEY_SADDLE_ITEM));

		if (getSaddleType() != SaddleType.NONE)
			this.chocoboInventory.deserializeNBT(nbt.getCompoundTag(NBTKEY_INVENTORY));

		if(nbt.hasKey(NBTKEY_NEST_POSITION))
		    this.nestPos = NBTUtil.getPosFromTag(nbt.getCompoundTag(NBTKEY_NEST_POSITION));

		this.setLevel(nbt.getInteger(NBTKEY_CHOCOBO_LEVEL));
		this.setGeneration(nbt.getInteger(NBTKEY_CHOCOBO_GENERATION));
		this.setStamina(nbt.getFloat(NBTKEY_CHOCOBO_STAMINA));

		this.setCanFly(nbt.getBoolean(NBTKEY_CHOCOBO_CAN_FLY));
		this.setCanGlide(nbt.getBoolean(NBTKEY_CHOCOBO_CAN_GLIDE));
		this.setCanSprint(nbt.getBoolean(NBTKEY_CHOCOBO_CAN_SPRINT));
		this.setCanDive(nbt.getBoolean(NBTKEY_CHOCOBO_CAN_DIVE));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte(NBTKEY_CHOCOBO_COLOR, (byte) this.getChocoboColor().ordinal());
		nbt.setBoolean(NBTKEY_CHOCOBO_IS_MALE, this.isMale());
		nbt.setByte(NBTKEY_MOVEMENTTYPE, (byte) this.getMovementType().ordinal());
		nbt.setTag(NBTKEY_SADDLE_ITEM, this.saddleItemStackHandler.serializeNBT());

		if (getSaddleType() != SaddleType.NONE)
			nbt.setTag(NBTKEY_INVENTORY, this.chocoboInventory.serializeNBT());

		if(this.nestPos != null)
		    nbt.setTag(NBTKEY_NEST_POSITION, NBTUtil.createPosTag(this.nestPos));

		nbt.setInteger(NBTKEY_CHOCOBO_LEVEL, this.getLevel());
		nbt.setInteger(NBTKEY_CHOCOBO_GENERATION, this.getGeneration());
		nbt.setFloat(NBTKEY_CHOCOBO_STAMINA, this.getStamina());

		nbt.setBoolean(NBTKEY_CHOCOBO_CAN_FLY, this.canFly());
		nbt.setBoolean(NBTKEY_CHOCOBO_CAN_GLIDE, this.canGlide());
		nbt.setBoolean(NBTKEY_CHOCOBO_CAN_SPRINT, this.canSprint());
		nbt.setBoolean(NBTKEY_CHOCOBO_CAN_DIVE, this.canDive());
	}

	public ChocoboColor getChocoboColor()
	{
		return this.dataManager.get(PARAM_COLOR);
	}

	public void setChocoboColor(ChocoboColor color)
	{
		this.dataManager.set(PARAM_COLOR, color);
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

	public boolean isSaddled()
	{
		return this.getSaddleType().isRidingSaddle();
	}

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

	@Nullable
	public BlockPos getNestPosition()
	{
		return this.nestPos;
	}

	public void setNestPosition(@Nullable BlockPos nestPos)
	{
		this.nestPos = nestPos;
	}

	//region Chocobo statistics getter/setter
	public int getLevel() { return this.dataManager.get(PARAM_LEVEL); }
	public void setLevel(int value) { this.dataManager.set(PARAM_LEVEL, value); }
	public float getStamina() { return this.dataManager.get(PARAM_STAMINA); }
	public void setStamina(float value) { this.dataManager.set(PARAM_STAMINA, value); }

    public float getStaminaPercentage()
    {
        return (float) (this.getStamina() / this.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).getAttributeValue());
    }

	public int getGeneration() { return this.dataManager.get(PARAM_GENERATION); }
	public void setGeneration(int value) { this.dataManager.set(PARAM_GENERATION, value); }

	private boolean useStamina(float value)
	{
		if(value == 0) return true;
		float curStamina = this.dataManager.get(PARAM_STAMINA);
		if(curStamina < value) return false;

		float maxStamina = (float) this.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).getAttributeValue();
		float newStamina = MathHelper.clamp(curStamina - value, 0, maxStamina);
		this.dataManager.set(PARAM_STAMINA, newStamina);
		return true;
	}

	public boolean canFly() { return (this.dataManager.get(PARAM_ABILITY_MASK) & CAN_FLY_BIT) > 0; }
	public void setCanFly(boolean state) { this.setAbilityMaskBit(CAN_FLY_BIT, state); }
	public boolean canGlide() { return (this.dataManager.get(PARAM_ABILITY_MASK) & CAN_GLIDE_BIT) > 0; }
	public void setCanGlide(boolean state) { this.setAbilityMaskBit(CAN_GLIDE_BIT, state); }
	public boolean canSprint() { return (this.dataManager.get(PARAM_ABILITY_MASK) & CAN_SPRINT_BIT) > 0; }
	public void setCanSprint(boolean state) { this.setAbilityMaskBit(CAN_SPRINT_BIT, state); }
	public boolean canDive() { return (this.dataManager.get(PARAM_ABILITY_MASK) & CAN_DIVE_BIT) > 0; }
	public void setCanDive(boolean state) { this.setAbilityMaskBit(CAN_DIVE_BIT, state); }

	private void setAbilityMaskBit(int bit, boolean state)
	{
		int value = this.dataManager.get(PARAM_ABILITY_MASK);
		this.dataManager.set(PARAM_ABILITY_MASK, (byte)(state ? value | bit : value & ~bit));
	}
	//endregion

	@Override
	public double getMountedYOffset()
	{
		return 1.65D;
	}

	@Override
	public boolean shouldDismountInWater(Entity rider)
	{
		return false;
	}

	@Nullable
	public Entity getControllingPassenger()
	{
		return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
	}

	@Override
	public void travel(float strafe, float vertical, float forward)
	{
		if (this.getControllingPassenger() instanceof EntityPlayer)
		{
			EntityPlayer rider = (EntityPlayer) this.getControllingPassenger();
			
			this.prevRotationYaw = rider.rotationYaw;
			this.prevRotationPitch = rider.rotationPitch;
			this.rotationYaw = rider.rotationYaw;
			this.rotationPitch = rider.rotationPitch;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = this.rotationYaw;
			this.renderYawOffset = this.rotationYaw;

			strafe = rider.moveStrafing * 0.5F;
			forward = rider.moveForward;

			// reduce movement speed by 75% if moving backwards
			if (forward <= 0.0F)
				forward *= 0.25F;

			if (this.onGround)
				this.isChocoboJumping = false;

			this.jumpMovementFactor = .1f;

			if (this.canPassengerSteer())
			{
				if (rider.isJumping)
				{
					if (this.canFly() && !rider.isInWater() && this.useStamina(ChocoConfig.chocobo.flyStaminaCost))
					{
						// flight logic
						this.motionY += this.onGround ? .5f : .1f;
						if (motionY > 0.5f)
							this.motionY = 0.5f;
					}
					else
					{
						// jump logic
						if (!this.isChocoboJumping && this.onGround && this.useStamina(ChocoConfig.chocobo.jumpStaminaCost))
						{
							this.motionY = .6f;
							this.isChocoboJumping = true;
						}
					}
				}

				if (this.isInWater())
				{
					if (this.canDive())
					{
						if (rider.isSneaking())
						{
							this.motionY -= 0.05f;
							if (this.motionY < -0.7f)
								this.motionY = -0.7f;
						}

						if (rider.isJumping)
						{
							this.motionY = .5f;
						}
						
						this.inWater = false;
						this.setSprinting(false);
					}
					else
					{
						if (rider.isJumping)
						{
							this.motionY = .2f;
						}
						else if (this.motionY < 0)
						{
							int distance = WorldUtils.getDistanceToSurface(this.getPosition(), this.getEntityWorld());
							if (distance > 0)
								this.motionY = .01f + Math.min(0.05f * distance, 0.7);
						}
					}
				}

				if (!this.onGround && !this.isInWater() && !rider.isSneaking() && this.motionY < 0 && this.canGlide() && this.useStamina(ChocoConfig.chocobo.glideStaminaCost))
				{
					this.motionY *= 0.8f;
				}

				if(this.isSprinting() && !this.useStamina(ChocoConfig.chocobo.sprintStaminaCost))
                {
                    this.setSprinting(false);
                }
				
				if(this.isSprinting() && !this.canSprint() && this.useStamina(ChocoConfig.chocobo.sprintStaminaCost))
                {
                    this.setSprinting(false);
                }

				this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(strafe, vertical, forward);
			}
		}
		else
		{
			super.travel(strafe, vertical, forward);
		}
	}
	
	@Override
	public void updatePassenger(Entity passenger) {
	    super.updatePassenger(passenger);
	    if (passenger instanceof EntityLiving && this.getControllingPassenger() == passenger) {
	        this.renderYawOffset = ((EntityLivingBase) passenger).renderYawOffset;
	    }
	}

	@Nullable
	@Override
	public EntityAgeable createChild(EntityAgeable entity)
	{
		return null;
	}

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if(otherAnimal == this || !(otherAnimal instanceof EntityChocobo)) return false;
        if(!this.isInLove() || !otherAnimal.isInLove()) return false;
        EntityChocobo otherChocobo = (EntityChocobo) otherAnimal;
        return otherChocobo.isMale() != this.isMale();
    }

    @Override
    public void setSprinting(boolean sprinting)
    {
        this.setFlag(3, sprinting);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (iattributeinstance.getModifier(CHOCOBO_SPRINTING_BOOST_ID) != null)
        {
            iattributeinstance.removeModifier(CHOCOBO_SPRINTING_SPEED_BOOST);
        }

        if (sprinting && this.canSprint())
        {
            iattributeinstance.applyModifier(CHOCOBO_SPRINTING_SPEED_BOOST);
        }
    }
    
    public void dropFeather()
	{
    	if (!this.isChild())
    	{
    		if(!this.getEntityWorld().isRemote)
    		{
    		this.entityDropItem(new ItemStack(ModItems.chocoboFeather, 1), 0.0F);
    		}
    	}
	}
    
    public int TimeSinceFeatherChance = 0;

    @Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		this.setRotation(this.rotationYaw, this.rotationPitch);

		this.regenerateStamina();

		this.stepHeight = 1f;
		this.fallDistance = 0f;
		
	    if(this.TimeSinceFeatherChance == 3000)
		{
	    	this.TimeSinceFeatherChance = 0;
	    	
	    	if ((float)Math.random() < .25)
			{
				this.dropFeather();
			}
		}
	    
	    this.TimeSinceFeatherChance++;

		if(!this.getEntityWorld().isRemote)
        {
            if(this.canDive() && this.ticksExisted % 60 == 0)
            {
                this.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 100, 0, true, false));
                if(this.isBeingRidden())
                {
                    Entity controller = this.getControllingPassenger();
                    if(controller instanceof EntityPlayer)
                    {
                        ((EntityPlayer) controller).addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 100, 0, true, false));
                    }
                }
            }
        }
        else
		{
            // Wing rotations, control packet, client side
			// Client side
			this.destPos += (double) (this.onGround ? -1 : 4) * 0.3D;
			this.destPos = MathHelper.clamp(destPos, 0f, 1f);

			if (!this.onGround)
				this.wingRotDelta = Math.min(wingRotation, 1f);
			this.wingRotDelta *= 0.9D;
			this.wingRotation += this.wingRotDelta * 2.0F;

			if (this.onGround)
			{
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
			} else
			{
				this.limbSwing = 0;
				this.limbSwingAmount = 0;
				this.prevLimbSwingAmount = 0;
			}
		}
	}

	private void regenerateStamina()
	{
		// ... yes, we also allow regeneration while in lava :P
		// this effectivly limits regeneration to only work while on the ground
		if(!this.onGround && !this.isInWater() && !this.isInLava()  && !this.isSprinting())
			return;

		float regen = ChocoConfig.chocobo.staminaRegenRate;

		// half the amount of regeneration while moving
		if(this.motionX != 0 || this.motionZ != 0)
			regen *= 0.85;

		// TODO: implement regen bonus (another IAttribute?)
		this.useStamina(-regen);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack heldItemStack = player.getHeldItem(hand);

		if(heldItemStack.getItem() == ModItems.chocopedia)
		{
			Chococraft.proxy.openChocoboInfoGui(this, player);
			return true;
		}

		if (this.isTamed() && player.isSneaking() && !this.isChild())
		{
			if (player instanceof EntityPlayerMP)
				this.displayChocoboInventory((EntityPlayerMP) player);
			return true;
		}

		if (this.getEntityWorld().isRemote) return true;

		if (this.isSaddled() && heldItemStack.isEmpty() && !player.isSneaking() && !this.isChild())
		{
			player.startRiding(this);
			return true;
		}

		if (!this.isTamed() && heldItemStack.getItem() == ModItems.gysahlGreen)
		{
			this.consumeItemFromStack(player, player.inventory.getCurrentItem());
			if ((float)Math.random() < ChocoConfig.chocobo.tameChance)
			{
				this.setOwnerId(player.getUniqueID());
				this.setTamed(true);
				player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.tame_success"), true);
			} else
			{
				player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.tame_fail"), true);
			}
			return true;
		}
		
		if(this.isTamed() && heldItemStack.getItem() == ModItems.gysahlGreen)
		{
			{
				if(getHealth() != getMaxHealth()) {
					this.consumeItemFromStack(player, player.inventory.getCurrentItem());
					heal(5);
				} else {
					player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.heal_fail"), true);
				}
			}
		}
		
		if(this.isTamed() && heldItemStack.getItem() == ModItems.chocoboWhistle && !this.isChild())
		{
			{
				if(this.followingmrhuman == 3) {
					this.playSound(ModSounds.WHISTLE_SOUND_FOLLOW, 1.0F, 1.0F);
					this.setNoAI(false);
					this.tasks.addTask(0, this.follow);
					followingmrhuman = 1;
					player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.chocobo_followcmd"), true);
				} else if(this.followingmrhuman == 1){
					this.playSound(ModSounds.WHISTLE_SOUND_WANDER, 1.0F, 1.0F);
					this.tasks.removeTask(this.follow);
					followingmrhuman = 2;
					player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.chocobo_wandercmd"), true);
				} else if(this.followingmrhuman == 2){
					this.playSound(ModSounds.WHISTLE_SOUND_STAY, 1.0F, 1.0F);
					this.setNoAI(true);
					followingmrhuman = 3;
					player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.chocobo_staycmd"), true);
				}
			}
		}

		if(this.isTamed() && !this.isInLove() && heldItemStack.getItem() == ModItems.lovelyGysahlGreen && !this.isChild())
		{
			this.consumeItemFromStack(player, player.inventory.getCurrentItem());
			this.setInLove(player);
			return true;
		}

		if (heldItemStack.getItem() == ModItems.chocoboSaddle && this.isTamed() && !this.isSaddled() && !this.isChild())
		{
			player.sendStatusMessage(new TextComponentTranslation(Chococraft.MODID + ".entity_chocobo.saddle_applied"), true);
			this.saddleItemStackHandler.setStackInSlot(0, heldItemStack.copy().splitStack(1));
			this.setSaddleType(heldItemStack);
			this.consumeItemFromStack(player, heldItemStack);
			return true;
		}

		if(this.isTamed() && !heldItemStack.isEmpty())
		{
			Optional<ChocoboColor> color = ChocoboColor.getColorForItemstack(heldItemStack);
			if(color.isPresent())
			{
				this.consumeItemFromStack(player, heldItemStack);
				this.setChocoboColor(color.get());
			}
		}

		return super.processInteract(player, hand);
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

	@SuppressWarnings("unused")
	private void reconfigureInventory(SaddleType oldType, SaddleType newType)
	{
		if (!this.getEntityWorld().isRemote)
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
			for (int i = 0; i < this.chocoboInventory.getSlots(); i++)
			{
				if(!this.isDead) 
				{
				ItemStack stack = this.chocoboInventory.extractItem(i, Integer.MAX_VALUE, false);
				InventoryHelper.spawnItemStack(this.getEntityWorld(), this.posX, this.posY + .5, this.posZ, stack);
				}
			}
		}
		this.chocoboInventory.setSize(newType.getInventorySize());

		for (EntityPlayer player : world.playerEntities)
		{
			if (player.openContainer instanceof ContainerSaddleBag)
				((ContainerSaddleBag) player.openContainer).refreshSlots(this, player);
		}
	}
	
    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        
        if(this.chocoboInventory!=null && this.isSaddled())
        {
            for(int i = this.chocoboInventory.getSlots()-1;i >=0; i--)
            {
                if(!this.chocoboInventory.getStackInSlot(i).isEmpty())
                    this.entityDropItem(this.chocoboInventory.getStackInSlot(i),0.0f);
            }
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return ModSounds.AMBIENT_SOUND;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return ModSounds.AMBIENT_SOUND;
    }

    protected SoundEvent getDeathSound()
    {
        return ModSounds.AMBIENT_SOUND;
    }

    @Override
    protected float getSoundVolume()
    {
        return .6f;
    }
    
    @Override
    public int getTalkInterval()
    {
        return (24 * (int)(Math.random()*100));
    }
}
