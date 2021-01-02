package net.chococraft.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.chococraft.common.ChocoConfig;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.handler.ExperienceHandler;

public class PacketUpgradeChocobo implements IMessage
{
	public int entityID;
	public int skillID;
	
	public PacketUpgradeChocobo() {}
	
	public PacketUpgradeChocobo(EntityChocobo chocobo, int skillID)
	{
		this.entityID = chocobo.getEntityId();
		this.skillID = skillID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.skillID = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.entityID);
		buf.writeInt(this.skillID);
	}
	
	public static class Handler implements IMessageHandler<PacketUpgradeChocobo, IMessage>
	{
		@Override
		public IMessage onMessage(PacketUpgradeChocobo message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
			{
			if(ctx.side == Side.SERVER)
			{
				World world = ctx.getServerHandler().player.world;
				EntityPlayer player = ctx.getServerHandler().player;
				Entity entity = world.getEntityByID(message.entityID);
				
				if(entity!=null && entity instanceof EntityChocobo)
				{
					if(message.skillID == 1 && ExperienceHandler.removeExperience(player, ChocoConfig.chocobo.ExpCostSprint))
					{
						((EntityChocobo)entity).setCanSprint(true);
					}
					else if(message.skillID == 2 && ExperienceHandler.removeExperience(player, ChocoConfig.chocobo.ExpCostGlide))
					{
						((EntityChocobo)entity).setCanGlide(true);
					}
					else if(message.skillID == 3 && ExperienceHandler.removeExperience(player, ChocoConfig.chocobo.ExpCostDive))
					{
						((EntityChocobo)entity).setCanDive(true);
					}
					else if(message.skillID == 4 && ExperienceHandler.removeExperience(player, ChocoConfig.chocobo.ExpCostFly))
					{
						((EntityChocobo)entity).setCanFly(true);
					}
					
				}
			}
			});
			return null;
		}
	}
}
