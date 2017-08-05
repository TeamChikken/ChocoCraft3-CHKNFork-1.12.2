package net.xalcon.chococraft.common.blocks;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.xalcon.chococraft.common.init.ModItems;

public class BlockGysahlGreen extends BlockCrops
{
	public static final int MAX_AGE = 4;
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);

	private final IBlockState fullyGrownState;

	public BlockGysahlGreen()
	{
		this.fullyGrownState = this.getDefaultState().withProperty(AGE, this.getMaxAge());
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
		return MAX_AGE;
	}

    public IBlockState getFullyGrownState()
    {
        return this.fullyGrownState;
    }
}
