package net.xalcon.chococraft.common.entities.breeding;

import net.minecraft.entity.ai.EntityAIBase;
import net.xalcon.chococraft.common.entities.EntityChocobo;

public class EntityChocoboAIPregnant extends EntityAIBase
{
    private EntityChocobo chocobo;

    public EntityChocoboAIPregnant(EntityChocobo chocobo)
    {
        this.chocobo = chocobo;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        return this.chocobo.isPregnant();
    }
}
