package net.xalcon.chococraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.chococraft.common.init.ModItems;

@Mod(modid = Chococraft.MODID, version = Chococraft.VERSION)
public class Chococraft
{
    public static final String MODID = "chococraft";
    public static final String VERSION = "@VERSION@";

    public static final CreativeTabs creativeTab = new CreativeTabs(MODID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModItems.gysahlGreen);
        }
    };
}
