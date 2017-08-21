package net.xalcon.chococraft.common.entities.breeding;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import net.xalcon.chococraft.common.entities.EntityChocobo;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChocoboAIMate extends EntityAIBase
{
    private final EntityChocobo chocobo;
    private final World world;
    private final double moveSpeed;
    private EntityChocobo targetMate;
    private int spawnBabyDelay;

    public EntityChocoboAIMate(EntityChocobo chocobo, double moveSpeed)
    {
        this.chocobo = chocobo;
        this.world = chocobo.world;
        this.moveSpeed = moveSpeed;
        this.setMutexBits(3); // TODO: What is this?
    }

    @Override
    public boolean shouldExecute()
    {
        return this.chocobo.isInLove() && !this.chocobo.isPregnant() && (this.targetMate = this.getNearbyMate()) != null;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60 && !this.chocobo.isPregnant();
    }

    @Override
    public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void updateTask()
    {
        this.chocobo.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.chocobo.getVerticalFaceSpeed());
        this.chocobo.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.chocobo.getDistanceSqToEntity(this.targetMate) < 9.0D)
        {
            this.setPregnant();
        }
    }

    @Nullable
    private EntityChocobo getNearbyMate()
    {
        List<EntityChocobo> list = this.world.getEntitiesWithinAABB(EntityChocobo.class, this.chocobo.getEntityBoundingBox().grow(8.0D));
        double dist = Double.MAX_VALUE;
        EntityChocobo closestMate = null;

        for (EntityChocobo entry : list)
        {
            if (this.chocobo.canMateWith(entry) && this.chocobo.getDistanceSqToEntity(entry) < dist)
            {
                closestMate = entry;
                dist = this.chocobo.getDistanceSqToEntity(entry);
            }
        }

        return closestMate;
    }

    private void setPregnant()
    {
        if(this.chocobo.isMale()) return;

        this.chocobo.setPregnant(true);
    }
}
