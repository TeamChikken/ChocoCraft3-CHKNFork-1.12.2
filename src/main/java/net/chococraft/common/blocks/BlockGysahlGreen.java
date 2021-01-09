package net.chococraft.common.blocks;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.chococraft.common.init.ModItems;

public class BlockGysahlGreen extends BlockCrops {
    public static final int MAX_AGE = 4;
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);

    private final IBlockState fullyGrownState;

    @SuppressWarnings("unused") // used by class factory
    public BlockGysahlGreen() {
        this.fullyGrownState = this.getDefaultState().withProperty(AGE, this.getMaxAge());
    }

    @Override
    protected Item getSeed() {
        return ModItems.gysahlGreenSeeds;
    }

    /// Gysahl farm drop chance
    @Override
    protected Item getCrop() {
        float gysahlchance = (float) Math.random();
        if (0.15f > gysahlchance)
            return ModItems.lovelyGysahlGreen;
        else
            return ModItems.gysahlGreen;
    }
///

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    protected PropertyInteger getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    public IBlockState getFullyGrownState() {
        return this.fullyGrownState;
    }
}
