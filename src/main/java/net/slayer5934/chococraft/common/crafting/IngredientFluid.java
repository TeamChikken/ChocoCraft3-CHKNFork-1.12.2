package net.slayer5934.chococraft.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class IngredientFluid extends Ingredient
{
    private final FluidStack fluid;
    private ItemStack[] cachedStacks;

    public IngredientFluid(FluidStack fluid)
    {
        super(0);
        this.fluid = fluid;
    }

    public IngredientFluid(Fluid fluid, int amount)
    {
        this(new FluidStack(fluid, amount));
    }

    public FluidStack getFluid()
    {
        return fluid;
    }

    @Override
    public ItemStack[] getMatchingStacks()
    {
        return cachedStacks != null ? cachedStacks : (cachedStacks = new ItemStack[]{FluidUtil.getFilledBucket(fluid)});
    }

    @Override
    public boolean apply(@Nullable ItemStack stack)
    {
        if (stack == null) return false;
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        return fluidStack != null && fluidStack.containsFluid(fluid);
    }
}
