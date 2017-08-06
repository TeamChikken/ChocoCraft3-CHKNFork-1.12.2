package net.xalcon.chococraft.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.client.gui.GuiChocoboInventory;
import net.xalcon.chococraft.common.entities.EntityChocobo;

import javax.annotation.Nullable;

public class PacketOpenChocoboGui implements IMessage
{
    public int entityId;
    public int windowId;
    public NBTTagCompound saddle;
    @Nullable
    public NBTTagCompound inventory;

    public PacketOpenChocoboGui() { }

    public PacketOpenChocoboGui(EntityChocobo chocobo, int windowId)
    {
        this.entityId = chocobo.getEntityId();
        this.windowId = windowId;

        this.saddle = chocobo.saddleItemStackHandler.serializeNBT();
        if(chocobo.getSaddleType().getInventorySize() > 0)
            this.inventory = chocobo.chocoboInventory.serializeNBT();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.windowId = buf.readInt();
        this.saddle = ByteBufUtils.readTag(buf);
        if(buf.readBoolean())
            this.inventory = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeInt(this.windowId);
        ByteBufUtils.writeTag(buf, saddle);
        buf.writeBoolean(this.inventory != null);
        if(this.inventory != null)
            ByteBufUtils.writeTag(buf, inventory);
    }

    public static class Handler implements IMessageHandler<PacketOpenChocoboGui, IMessage>
    {
        @Override @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketOpenChocoboGui message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
            {
                Minecraft mc = Minecraft.getMinecraft();
                Entity entity = mc.world.getEntityByID(message.entityId);
                if(!(entity instanceof EntityChocobo))
                {
                    Chococraft.log.warn("Server send OpenGUI for chocobo with id {}, but this entity does not exist on my side", message.entityId);
                    return;
                }

                EntityChocobo chocobo = (EntityChocobo) entity;
                mc.displayGuiScreen(new GuiChocoboInventory(chocobo, mc.player));
                mc.player.openContainer.windowId = message.windowId;

                chocobo.saddleItemStackHandler.deserializeNBT(message.saddle);
                if(message.inventory != null)
                    chocobo.chocoboInventory.deserializeNBT(message.inventory);
            });
            return null;
        }
    }
}
