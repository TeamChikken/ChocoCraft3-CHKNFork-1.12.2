package net.xalcon.chococraft.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.network.packets.PacketChocoboSprinting;
import net.xalcon.chococraft.common.network.packets.PacketOpenChocoboGui;

public class PacketManager
{
    public final static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chococraft.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(PacketOpenChocoboGui.Handler.class, PacketOpenChocoboGui.class, 0, Side.CLIENT);

        INSTANCE.registerMessage(PacketChocoboSprinting.Handler.class, PacketChocoboSprinting.class, 1, Side.SERVER);
    }
}
