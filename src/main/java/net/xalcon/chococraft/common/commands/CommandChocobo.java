package net.xalcon.chococraft.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.properties.ChocoboAbilityInfo;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.Map;

public class CommandChocobo extends CommandBase
{
    private static final String MODID = Chococraft.MODID;

    private static final Map<String, BiConsumer<ChocoboAbilityInfo, String>> setMap;

    static
    {
        setMap = new HashMap<>();
        setMap.put("level", (info, arg) -> info.setLevel(Integer.parseInt(arg)));
        setMap.put("health", (info, arg) -> info.setHealth(Float.parseFloat(arg)));
        setMap.put("resistance", (info, arg) -> info.setResistance(Float.parseFloat(arg)));
        setMap.put("speed", (info, arg) -> info.setSpeed(Float.parseFloat(arg)));
        setMap.put("stamina", (info, arg) -> info.setStamina(Float.parseFloat(arg)));

        setMap.put("sprint", (info, arg) -> info.setCanSprint(Boolean.parseBoolean(arg)));
        setMap.put("dive", (info, arg) -> info.setCanDive(Boolean.parseBoolean(arg)));
        setMap.put("glide", (info, arg) -> info.setCanGlide(Boolean.parseBoolean(arg)));
        setMap.put("fly", (info, arg) -> info.setCanFly(Boolean.parseBoolean(arg)));
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
        ChocoboAbilityInfo abilityInfo = chocobo.getAbilityInfo();

        switch(args[0].toLowerCase())
        {
            case "list":
                //sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.get_level", abilityInfo.getLevel()));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.get_health", abilityInfo.getHealth()));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.get_resistance", abilityInfo.getResistance()));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.get_speed", abilityInfo.getSpeed()));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.get_stamina", abilityInfo.getStamina()));

                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.can" + ( abilityInfo.canSprint() ? "" : "_not" ) + "_sprint"));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.can" + ( abilityInfo.canDive() ? "" : "_not" ) + "_dive"));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.can" + ( abilityInfo.canGlide() ? "" : "_not" ) + "_glide"));
                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.can" + ( abilityInfo.canFly() ? "" : "_not" ) + "_fly"));
                break;
            case "set":
                if(args.length != 3)
                {
                    sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.invalid_set_parameters"));
                    return;
                }

                if(setMap.containsKey(args[1]))
                    setMap.get(args[1]).accept(abilityInfo, args[2]);

                sender.sendMessage(new TextComponentTranslation("cmd." + MODID + ".chocobo.successfuly_set_parameters", args[1], args[2]));

                break;
        }
    }
}
