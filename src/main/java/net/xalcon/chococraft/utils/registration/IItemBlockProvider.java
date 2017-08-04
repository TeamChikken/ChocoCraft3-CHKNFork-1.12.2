package net.xalcon.chococraft.utils.registration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public interface IItemBlockProvider extends IItemModelRegistrationHandler
{
	default Item getItemBlock() { return new ItemBlock((Block)this); }
}
