package net.xalcon.chococraft.common.entities.breeding;

import akka.actor.dungeon.FaultHandling;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.xalcon.chococraft.common.ChocoConfig;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.properties.ChocoboAttributes;
import net.xalcon.chococraft.common.entities.properties.ChocoboColor;

public class BreedingHelper
{
    public static ChocoboBreedInfo getBreedInfo(EntityChocobo mother, EntityChocobo father)
    {
        return new ChocoboBreedInfo(new ChocoboStatSnapshot(mother), new ChocoboStatSnapshot(father));
    }

    public static EntityChocobo createChild(ChocoboBreedInfo breedInfo, World world)
    {
        EntityChocobo chocobo = new EntityChocobo(world);
        float traitBaseMod = 0.75f; // TODO: enhance somehow (Items? Parent Happyness?)
        float traitRngLimit = 0.5f;

        ChocoboStatSnapshot mother = breedInfo.getMother();
        ChocoboStatSnapshot father = breedInfo.getFather();

        // Bred chocobos always start at level 1
        chocobo.setLevel(1);
        chocobo.setChocoboColor(ChocoboColor.getRandomColor());
        chocobo.setGeneration(((mother.generation + father.generation) / 2) + 1);

        float health = Math.round((mother.health + father.health) / 2) * (traitBaseMod + (world.rand.nextFloat() * traitRngLimit));
        chocobo.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.min(health, ChocoConfig.breeding.maxHealth));

        float avgSpeed = (mother.speed + father.speed) / 2f;
        float speed = Math.round(avgSpeed * (traitBaseMod + (world.rand.nextFloat() * traitRngLimit)));
        speed = MathHelper.clamp(speed, avgSpeed - ChocoConfig.breeding.maxSpeedGrowth, avgSpeed + ChocoConfig.breeding.maxSpeedGrowth);

        chocobo.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(speed, (ChocoConfig.breeding.maxSpeed / 100f)));

        float stamina = Math.round((mother.stamina + father.stamina) / 2) * (traitBaseMod + (world.rand.nextFloat() * traitRngLimit));
        chocobo.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).setBaseValue(Math.min(stamina, ChocoConfig.breeding.maxStamina));

        float canSprintChance = calculateChance(0.05f, 0.15f, 0.4f, mother.canSprint, father.canSprint);
        chocobo.setCanSprint(canSprintChance > world.rand.nextFloat());

        float canGlideChance = calculateChance(0.02f, 0.1f, 0.4f, mother.canGlide, father.canGlide);
        chocobo.setCanGlide(canGlideChance > world.rand.nextFloat());

        float canDiveChance = calculateChance(0.02f, 0.1f, 0.3f, mother.canDive, father.canDive);
        chocobo.setCanSprint(canDiveChance > world.rand.nextFloat());

        float canFlyBaseChance = chocobo.canGlide() ? 0.001f : 0;
        float canFlyChance = calculateChance(canFlyBaseChance, 0.1f, 0.3f, mother.canFly, father.canFly);
        chocobo.setCanSprint(canFlyChance > world.rand.nextFloat());

        chocobo.setGrowingAge(-24000);

        return chocobo;
    }

    private static float calculateChance(float baseChance, float perParentChance, float bothParentsChance, boolean motherHasAbility, boolean fatherHasAbility)
    {
        return baseChance + (motherHasAbility || fatherHasAbility ? perParentChance : 0) + (motherHasAbility && fatherHasAbility ? bothParentsChance : 0);
    }
}
