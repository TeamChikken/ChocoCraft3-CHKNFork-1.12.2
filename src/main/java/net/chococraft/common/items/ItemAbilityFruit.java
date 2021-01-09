package net.chococraft.common.items;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.utils.registration.IItemModelProvider;

public class ItemAbilityFruit extends Item implements IItemModelProvider {
    public enum AbilityFruitType {
        SPRINT("spike_fruit", c -> !c.canSprint(), c -> c.setCanSprint(true)),
        GLIDE("aeroshroom", c -> !c.canGlide(), c -> c.setCanGlide(true)),
        DIVE("aqua_berry", c -> !c.canDive(), c -> c.setCanDive(true)),
        FLY("dead_pepper", c -> !c.canFly(), c -> c.setCanFly(true));

        private String name;
        private Consumer<EntityChocobo> abilityApplier;
        private Predicate<EntityChocobo> canLearnAbilityPredicate;

        AbilityFruitType(String name, Predicate<EntityChocobo> canLearnAbilityPredicate, Consumer<EntityChocobo> abilityApplier) {
            this.name = name;
            this.canLearnAbilityPredicate = canLearnAbilityPredicate;
            this.abilityApplier = abilityApplier;
        }

        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return this.ordinal();
        }

        public static AbilityFruitType getFromMeta(int meta) {
            if (meta < 0 || meta > values().length) return SPRINT;
            return values()[meta];
        }

        public boolean useFruitOn(EntityChocobo chocobo) {
            if (this.canLearnAbilityPredicate.test(chocobo)) {
                this.abilityApplier.accept(chocobo);
                return true;
            }
            return false;
        }
    }

    public ItemAbilityFruit() {
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!this.isInCreativeTab(tab)) return;

        for (AbilityFruitType type : AbilityFruitType.values()) {
            items.add(new ItemStack(this, 1, type.getMeta()));
        }
    }

    @Override
    public void registerItemModel(Item item) {
        ResourceLocation rl = this.getRegistryName();
        assert rl != null;

        // this registers a "blockstate" for our item, which allows different models depending on the variant
        ResourceLocation loc = new ResourceLocation(rl.getResourceDomain(), "items/" + rl.getResourcePath());
        for (AbilityFruitType type : AbilityFruitType.values()) {
            ModelLoader.setCustomModelResourceLocation(this, type.getMeta(), new ModelResourceLocation(loc, "type=" + type.getName()));
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityChocobo) {
            AbilityFruitType fruit = AbilityFruitType.getFromMeta(stack.getMetadata());
            if (fruit.useFruitOn((EntityChocobo) target)) {
                if (!playerIn.capabilities.isCreativeMode)
                    stack.shrink(1);
                return true;
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format(this.getUnlocalizedName(stack) + ".tooltip"));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + AbilityFruitType.getFromMeta(stack.getMetadata()).getName();
    }
}
