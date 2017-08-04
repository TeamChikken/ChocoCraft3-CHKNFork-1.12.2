package net.xalcon.chococraft.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.RiderState;

public class PacketChocoboJump implements IMessage
{
    public RiderState riderState;

    public PacketChocoboJump() {}

    public PacketChocoboJump(RiderState riderState) {//TODO make interface for controllable entities for haxylib, edit this to take that interface etc
        this.riderState = riderState;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        riderState = new RiderState();
        riderState.setJumping(buf.readBoolean());
        riderState.setSneaking(buf.readBoolean());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(riderState.isJumping());
        buf.writeBoolean(riderState.isSneaking());
    }

    public final static IMessageHandler<PacketChocoboJump, IMessage> Handler = (message, ctx) ->
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
        {
            Entity ridingEntity = ctx.getServerHandler().player.getRidingEntity();
            if(ridingEntity instanceof EntityChocobo)
                ((EntityChocobo)ridingEntity).getRiderState().updateState(message.riderState);
        });
        return null;
    };
}
