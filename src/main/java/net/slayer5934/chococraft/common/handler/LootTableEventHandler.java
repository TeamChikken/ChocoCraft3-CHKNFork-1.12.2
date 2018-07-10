package net.slayer5934.chococraft.common.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.ChocoConfig;
import net.slayer5934.chococraft.common.init.ModItems;
import net.slayer5934.chococraft.common.items.ItemAbilityFruit;

@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class LootTableEventHandler
{
    private final static LootCondition[] NO_CONDITION = new LootCondition[0];

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event)
    {
        if(!ChocoConfig.world.addAbilityFruitsToDungeonLoot) return;

        ResourceLocation lootTable = event.getName();
        if(!lootTable.getResourcePath().startsWith("chests/")) return;

        LootPool pool = event.getTable().getPool("main");
        //noinspection ConstantConditions : getPool() can return null, stfu intellij
        if(pool == null)
        {
            pool = new LootPool(new LootEntry[0], NO_CONDITION, new RandomValueRange(5, 10), new RandomValueRange(0), "main");
            event.getTable().addPool(pool);
        }

        LootFunction damage = new SetMetadata(NO_CONDITION, new RandomValueRange(0, ItemAbilityFruit.AbilityFruitType.values().length - 1));
        LootFunction amount = new SetCount(NO_CONDITION, new RandomValueRange(1, 1));
        LootFunction[] functions = new LootFunction[] { damage, amount };
        pool.addEntry(new LootEntryItem(ModItems.abilityFruit, ChocoConfig.world.abilityFruitDungeonLootWeight, 1, functions, NO_CONDITION, Chococraft.MODID+":ability_fruits"));
    }
}
