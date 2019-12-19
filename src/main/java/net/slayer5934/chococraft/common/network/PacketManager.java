package net.slayer5934.chococraft.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.network.packets.PacketChocoboSprinting;
import net.slayer5934.chococraft.common.network.packets.PacketOpenChocoboGui;
import net.slayer5934.chococraft.common.network.packets.PacketUpgradeChocobo;

public class PacketManager {
	public final static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chococraft.MODID);
	private static int id = 0;
	
	public static void init() {
		INSTANCE.registerMessage(PacketOpenChocoboGui.Handler.class, PacketOpenChocoboGui.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketChocoboSprinting.Handler.class, PacketChocoboSprinting.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketUpgradeChocobo.Handler.class, PacketUpgradeChocobo.class, id++, Side.SERVER);
	}
}
