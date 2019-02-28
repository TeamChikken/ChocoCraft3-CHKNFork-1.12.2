package net.slayer5934.chococraft.common.handler;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public final class ExperienceHandler {
    // The following code was ripped from EPlus https://github.com/Epoxide-Software/Enchanting-Plus/ which adapted it from OpenModsLib https://github.com/OpenMods/OpenModsLib
    public static int getExperience (EntityPlayer player) {

        return (int) (getExperienceForLevels(player.experienceLevel) + player.experience * player.xpBarCap());
    }

    public static void removeExperience (EntityPlayer player, int amount) {

        addExperience(player, -amount);
    }

    public static void addExperience (EntityPlayer player, int amount) {

        final int experience = getExperience(player) + amount;
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        final int expForLevel = getExperienceForLevels(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
    }

    public static int getExperienceForLevels (int level) {

        if (level == 0) {

            return 0;
        }

        if (level > 0 && level < 17) {

            return level * level + 6 * level;
        }

        else if (level > 16 && level < 32) {

            return (int) (2.5 * level * level - 40.5 * level + 360);
        }

        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    public static int getLevelForExperience (int experience) {

        int level = 0;

        while (getExperienceForLevels(level) <= experience) {

            level++;
        }

        return level - 1;
    }
}