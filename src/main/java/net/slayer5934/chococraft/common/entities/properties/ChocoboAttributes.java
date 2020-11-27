package net.slayer5934.chococraft.common.entities.properties;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class ChocoboAttributes
{
    public static IAttribute MAX_STAMINA =
            new RangedAttribute(null, "chocobo.maxStamina", 10.0D, 10D, 1024.0D)
            .setDescription("Max Stamina").setShouldWatch(true);
}
