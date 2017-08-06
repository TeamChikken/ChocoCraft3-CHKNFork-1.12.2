package net.xalcon.chococraft.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.network.packets.PacketChocoboJump;
import net.xalcon.chococraft.common.network.packets.PacketOpenChocoboGui;

public class PacketManager
{
    public final static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chococraft.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(PacketChocoboJump.Handler, PacketChocoboJump.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketOpenChocoboGui.Handler.class, PacketOpenChocoboGui.class, 1, Side.CLIENT);
    }
}
