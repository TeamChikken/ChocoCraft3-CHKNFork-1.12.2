package net.xalcon.chococraft.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.network.packets.PacketChocoboJump;

public class PacketManager
{
    public final static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chococraft.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(PacketChocoboJump.Handler, PacketChocoboJump.class, 0, Side.SERVER);
    }
}
