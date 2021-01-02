package net.chococraft.common.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.BiConsumer;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.chococraft.Chococraft;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.entities.properties.ChocoboAttributes;

public class CommandChocobo extends CommandBase
{
    private static final String MODID = Chococraft.MODID;

    private static final Map<String, BiConsumer<EntityChocobo, String>> setMap;

    static
    {
        setMap = new HashMap<>();
        setMap.put("level", (entity, arg) -> entity.setLevel(Integer.parseInt(arg)));
        setMap.put("health", (entity, arg) -> entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Float.parseFloat(arg)));
        setMap.put("resistance", (entity, arg) -> entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(Float.parseFloat(arg)));
        setMap.put("speed", (entity, arg) -> entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Float.parseFloat(arg)));
        setMap.put("stamina", (entity, arg) -> entity.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).setBaseValue(Float.parseFloat(arg)));

        setMap.put("sprint", (entity, arg) -> entity.setCanSprint(Boolean.parseBoolean(arg)));
        setMap.put("dive", (entity, arg) -> entity.setCanDive(Boolean.parseBoolean(arg)));
        setMap.put("glide", (entity, arg) -> entity.setCanGlide(Boolean.parseBoolean(arg)));
        setMap.put("fly", (entity, arg) -> entity.setCanFly(Boolean.parseBoolean(arg)));
    }

    @Override
    public String getName()
    {
        return "chocobo";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/chocobo list\n/chocobo set <trait> <value>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length == 0) return;
        if(!(sender instanceof EntityPlayer)) return;

        Entity mount = ((EntityPlayer) sender).getRidingEntity();
        if(!(mount instanceof EntityChocobo))
        {
            sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.not_riding_chocobo"));
            return;
        }

        EntityChocobo chocobo = (EntityChocobo) mount;

        switch(args[0].toLowerCase())
        {
            case "list":
                //sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.get_level", abilityInfo.getLevel()));
                sender.sendMessage(getText("get_health", chocobo, SharedMonsterAttributes.MAX_HEALTH));
                sender.sendMessage(getText("get_resistance", chocobo, SharedMonsterAttributes.ARMOR));
                sender.sendMessage(getText("get_speed", chocobo, SharedMonsterAttributes.MOVEMENT_SPEED));
                sender.sendMessage(getText("get_stamina", chocobo, ChocoboAttributes.MAX_STAMINA));

                sender.sendMessage(getText("sprint", chocobo.canSprint()));
                sender.sendMessage(getText("dive", chocobo.canDive()));
                sender.sendMessage(getText("glide", chocobo.canGlide()));
                sender.sendMessage(getText("fly", chocobo.canFly()));
                break;
            case "set":
                if(args.length != 3)
                {
                    sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.invalid_set_parameters"));
                    return;
                }

                if(setMap.containsKey(args[1]))
                    setMap.get(args[1]).accept(chocobo, args[2]);

                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.successfuly_set_parameters", args[1], args[2]));

                break;
        }
    }

    private static TextComponentTranslation getText(String key, EntityChocobo chocobo, IAttribute attribute)
    {
        return new TextComponentTranslation("cmd." + MODID + ".chocobo." + key, chocobo.getEntityAttribute(attribute).getBaseValue());
    }

    private static TextComponentTranslation getText(String key, boolean state)
    {
        return new TextComponentTranslation("cmd." + MODID + ".chocobo.can" + (state ? "_" : "_not_") + key);
    }
}
