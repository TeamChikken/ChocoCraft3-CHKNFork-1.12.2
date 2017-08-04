package net.xalcon.chococraft.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.xalcon.chococraft.common.init.ModBlocks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGysahlGreen extends ItemSeedFood
{
	public ItemGysahlGreen()
	{
		super(1, 0, ModBlocks.gysahlGreen, Blocks.GRASS);
		this.setPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 1), 0.6f);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
	{
		super.onFoodEaten(stack, worldIn, player);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(this.getUnlocalizedName() + ".tooltip");
	}
}
