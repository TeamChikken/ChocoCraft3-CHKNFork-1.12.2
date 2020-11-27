package net.slayer5934.chococraft.common.items;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.entities.EntityChocobo;
import net.slayer5934.chococraft.common.entities.properties.ChocoboColor;
import net.slayer5934.chococraft.utils.registration.IItemModelProvider;

public class ItemChocoboSpawnEgg extends Item implements IItemModelProvider
{
    public ItemChocoboSpawnEgg()
    {
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(this.isInCreativeTab(tab))
        {
            for(ChocoboColor color : ChocoboColor.values())
                items.add(new ItemStack(this, 1, color.ordinal()));
        }
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return EnumActionResult.SUCCESS;

        Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(Chococraft.MODID, "chocobo"), worldIn);

        int meta = player.getHeldItem(hand).getMetadata();
        if(meta < 0 || meta >= ChocoboColor.values().length) return EnumActionResult.FAIL;

        if (entity instanceof EntityChocobo)
        {
            EntityChocobo entityliving = (EntityChocobo)entity;
            if(player.isSneaking())
                entityliving.setGrowingAge(-24000);
            entity.setLocationAndAngles(pos.getX() + .5, pos.getY() + getYOffset(worldIn, pos), pos.getZ() + .5, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
            entityliving.setChocoboColor(ChocoboColor.values()[meta]);
            worldIn.spawnEntity(entity);
            entityliving.playLivingSound();
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void registerItemModel(Item item)
    {
        ResourceLocation rl = this.getRegistryName();
        assert rl != null;

        // this registers a "blockstate" for our item, which allows different models depending on the variant
        ResourceLocation loc = new ResourceLocation(rl.getResourceDomain(), "items/" + rl.getResourcePath());
        for(ChocoboColor color : ChocoboColor.values())
            ModelLoader.setCustomModelResourceLocation(this, color.ordinal(), new ModelResourceLocation(loc, "type=" + color.name().toLowerCase()));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int meta = stack.getMetadata();
        if(meta >= 0 && meta < ChocoboColor.values().length)
            return super.getUnlocalizedName(stack) + "." + ChocoboColor.values()[meta].name().toLowerCase();
        return super.getUnlocalizedName(stack);
    }

    private double getYOffset(World world, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = world.getCollisionBoxes(null, axisalignedbb);

        if (list.isEmpty())
        {
            return 0.0D;
        }
        else
        {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double)pos.getY();
        }
    }
}
