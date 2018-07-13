package net.slayer5934.chococraft.common.entities.properties;

import java.util.Optional;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public enum ChocoboColor
{
    YELLOW(new OreIngredient("dyeYellow")),
    GREEN(new OreIngredient("dyeLime")),
    BLUE(new OreIngredient("dyeBlue")),
    WHITE(new OreIngredient("dyeWhite")),
    BLACK(new OreIngredient("dyeBlack")),
    GOLD(new OreIngredient("ingotGold")),
    PINK(new OreIngredient("dyePink")),
    RED(new OreIngredient("dyeRed")),
    PURPLE(new OreIngredient("dyePurple")),
	FLAME(new OreIngredient(""));

    private static Random rand = new Random();
    private Ingredient colorIngredient;

    ChocoboColor(Ingredient colorIngredient)
    {
        this.colorIngredient = colorIngredient;
    }

    public static ChocoboColor getRandomColor()
    {
        return values()[rand.nextInt(values().length)];
    }

    public static Optional<ChocoboColor> getColorForItemstack(ItemStack itemStack)
    {
        for(ChocoboColor color : values())
        {
            if(color.colorIngredient.apply(itemStack))
                return Optional.of(color);
        }
        return Optional.empty();
    }
}
