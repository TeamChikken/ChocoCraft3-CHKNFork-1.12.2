package net.xalcon.chococraft.common.blocks;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;
import net.xalcon.chococraft.common.init.ModItems;

public class BlockGysahlGreen extends BlockCrops
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 4);

	public BlockGysahlGreen()
	{

	}

	@Override
	protected Item getSeed()
	{
		return ModItems.gysahlGreenSeeds;
	}

	@Override
	protected Item getCrop()
	{
		return ModItems.gysahlGreen;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AGE);
	}

	@Override
	protected PropertyInteger getAgeProperty()
	{
		return AGE;
	}

	@Override
	public int getMaxAge()
	{
		return 4;
	}
}
