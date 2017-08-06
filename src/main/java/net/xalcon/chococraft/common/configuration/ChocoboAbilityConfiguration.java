package net.xalcon.chococraft.common.configuration;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.xalcon.chococraft.common.entities.ChocoboAbilityInfo;

import net.xalcon.chococraft.common.entities.ChocoboColor;

public class ChocoboAbilityConfiguration
{
    public static void loadAbilityInfo()
    {
        //CommentedConfigurationNode abilityNode = mainNode.getNode("abilitys");
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.YELLOW).setSpeeds(20, 10, 0).setMaxHP(30).setStepHeight(1, 0.5f));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.GREEN).setSpeeds(27, 10, 0).setMaxHP(30).setStepHeight(2, 0.5f));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.BLUE).setSpeeds(27, 55, 0).setMaxHP(30).setStepHeight(1, 0.5f).setCanWalkOnWater(true));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.WHITE).setSpeeds(35, 45, 0).setMaxHP(40).setStepHeight(2, 0.5f).setCanClimb(true).setCanWalkOnWater(true));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.BLACK).setSpeeds(40, 20, 0).setMaxHP(40).setStepHeight(2, 0.5f).setCanWalkOnWater(true).setCanClimb(true));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.GOLD).setSpeeds(50, 20, 55).setMaxHP(50).setStepHeight(2, 0.5f).setCanWalkOnWater(true).setCanClimb(true).setCanFly(true).setImmuneToFire(true));// TODO needs particles
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.PINK).setSpeeds(55, 25, 60).setMaxHP(50).setStepHeight(2, 0.5f).setCanClimb(true).setCanWalkOnWater(true).setCanFly(true));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.RED).setSpeeds(55, 25, 60).setMaxHP(50).setStepHeight(2, 0.5f).setCanClimb(true).setCanWalkOnWater(true).setCanFly(true));
        loadAbilityForColout(new ChocoboAbilityInfo(ChocoboColor.PURPLE).setSpeeds(40, 10, 55).setMaxHP(50).setStepHeight(1, 0.5f).setCanClimb(true).setCanFly(true).setImmuneToFire(true).addRiderAbility(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, -1, true, false)));
    }

    private static void loadAbilityForColout(ChocoboAbilityInfo abilityInfo)
    {
        /*float landSpeed = node.getNode("landSpeed").getFloat(abilityInfo.getLandSpeed());
        float waterSpeed = node.getNode("waterSpeed").getFloat(abilityInfo.getWaterSpeed());
        float airSpeed = node.getNode("airSpeed").getFloat(abilityInfo.getAirbornSpeed());
        int maxHP = node.getNode("maxHP").getInt(abilityInfo.getMaxHP());
        float mountedStepHeight = node.getNode("mountedStepHeight").getFloat(abilityInfo.getStepHeight(true));
        float normalStepHeight = node.getNode("normalStepHeight").getFloat(abilityInfo.getStepHeight(false));
        boolean canWalkOnWater = node.getNode("canWalkOnWater").getBoolean(abilityInfo.canWalkOnWater());
        boolean canClimb = node.getNode("canClimb").getBoolean(abilityInfo.canClimb());
        boolean canFly = node.getNode("canFly").getBoolean(abilityInfo.canFly());
        boolean immuneToFire = node.getNode("immuneToFire").getBoolean(abilityInfo.isImmuneToFire());
        abilityInfo.setSpeeds(landSpeed, waterSpeed, airSpeed).setMaxHP(maxHP).setStepHeight(mountedStepHeight, normalStepHeight).setCanWalkOnWater(canWalkOnWater)
                .setCanClimb(canClimb).setCanFly(canFly).setImmuneToFire(immuneToFire).save();*/
        // TODO: load from config
        abilityInfo.save();
    }
}
