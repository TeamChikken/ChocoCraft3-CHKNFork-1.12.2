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
        float traitBaseMod = 0.8f;
        float traitRngLimit = 0.5f;
        float randomZeroToOne = (float)Math.random();

        ChocoboStatSnapshot mother = breedInfo.getMother();
        ChocoboStatSnapshot father = breedInfo.getFather();

        // Bred chocobos always start at level 1
        chocobo.setLevel(1);
        chocobo.setChocoboColor(ChocoboColor.getRandomColor());
        chocobo.setGeneration(((mother.generation + father.generation) / 2) + 1);
//Health
        float health = Math.round((mother.health + father.health) / 2) * (traitBaseMod + ((float)Math.random() * traitRngLimit));
        chocobo.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.min(health, ChocoConfig.breeding.maxHealth));
//Speed
        float speed = ((mother.speed + father.speed) / 2f) * (traitBaseMod + ((float)Math.random() * traitRngLimit));
        chocobo.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(speed, (ChocoConfig.breeding.maxSpeed / 100f)));
//Stamina
        float stamina = Math.round((mother.stamina + father.stamina) / 2) * (traitBaseMod + ((float)Math.random() * traitRngLimit));
        chocobo.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).setBaseValue(Math.min(stamina, ChocoConfig.breeding.maxStamina));
//Traits
        float canSprintChance = calculateChance(0.03f, 0.25f, 0.5f, mother.canSprint, father.canSprint);
        chocobo.setCanSprint(canSprintChance > (float)Math.random());

        float canGlideChance = calculateChance(0.01f, 0.2f, 0.45f, mother.canGlide, father.canGlide);
        chocobo.setCanGlide(canGlideChance > (float)Math.random());

        float canDiveChance = calculateChance(0.01f, 0.2f, 0.45f, mother.canDive, father.canDive);
        chocobo.setCanSprint(canDiveChance > (float)Math.random());

        float canFlyChance = calculateChance(0.005f, 0.15f, 0.4f, mother.canFly, father.canFly);
        chocobo.setCanFly(canFlyChance > (float)Math.random());

        chocobo.setGrowingAge(-24000);

        return chocobo;
    }

    private static float calculateChance(float baseChance, float perParentChance, float bothParentsChance, boolean motherHasAbility, boolean fatherHasAbility)
    {
        return baseChance + (motherHasAbility || fatherHasAbility ? perParentChance : 0) + (motherHasAbility && fatherHasAbility ? bothParentsChance : 0);
    }
}
