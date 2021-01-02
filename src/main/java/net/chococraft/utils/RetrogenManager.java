package net.chococraft.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.chococraft.common.ChocoConfig;

@Mod.EventBusSubscriber(Side.SERVER)
public class RetrogenManager
{
	private static Map<World, Set<ChunkPos>> pendingWork = new HashMap<>();
	private static Map<World, Set<ChunkPos>> completedWork = new HashMap<>();

	@SubscribeEvent
	public static void onChunkLoad(ChunkDataEvent.Load event)
	{
		if(!(event.getWorld() instanceof WorldServer)) return;
		WorldServer world = (WorldServer) event.getWorld();

		// TODO: add thread sync (semaphore?)
		Set<ChunkPos> completedChunks = completedWork.computeIfAbsent(world, k -> new HashSet<>());

		// only add to the list if its not already there or if the retrongen version is older than the configured one
		if(completedChunks.contains(event.getChunk().getPos())) return;
		int version = event.getData().getInteger("CHOCOCRAFT_RG");
		if(version >= ChocoConfig.world.retrogenId) return;

		// we found a chunk that needs retrogen
		// TODO: add thread sync (semaphore?)
		pendingWork.computeIfAbsent(world, w -> new HashSet<>()).add(event.getChunk().getPos());
	}

	@SubscribeEvent
	public static void onChunkSave(ChunkDataEvent.Save event)
	{
		if(!(event.getWorld() instanceof WorldServer)) return;
		WorldServer world = (WorldServer) event.getWorld();

		// TODO: add thread sync (semaphore?)
		Set<ChunkPos> completedChunks = completedWork.get(world);
		if(completedChunks != null && completedChunks.contains(event.getChunk().getPos()))
		{
			NBTTagCompound data = event.getData();
			// TODO: Change this. We might have multiple generators in the future, like a flag per generator
			data.setInteger("CHOCOCRAFT_RG", 1);
			completedChunks.remove(event.getChunk().getPos());
		}
	}

	/*@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(!(event.world instanceof WorldServer)) return;

		if(event.phase == TickEvent.Phase.END)
		{

		}
	}*/
}
