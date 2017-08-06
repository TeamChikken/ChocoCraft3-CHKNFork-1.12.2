package net.xalcon.chococraft.common.world.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.xalcon.chococraft.common.init.ModBlocks;

import java.util.Random;

public class WorldGenGysahlGreen implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if(!world.provider.isSurfaceWorld() || !world.provider.hasSkyLight())
            return;

        if(random.nextInt(1000) > 100)
            return;

        // offset the generation by +8 on x and z to prevent cascading chunk generation
        int poxX = (chunkX * 16 + 8) + random.nextInt(16);//gets us the world position of where to spawn the gysahls
        int posZ = (chunkZ * 16 + 8) + random.nextInt(16);
        BlockPos finalPosition = world.getTopSolidOrLiquidBlock(new BlockPos(poxX, 0, posZ));//Gets the top block

        IBlockState blockState = ModBlocks.gysahlGreen.getFullyGrownState();

        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockPos = finalPosition.add(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockPos) && blockPos.getY() < world.provider.getHeight() && ModBlocks.gysahlGreen.canBlockStay(world, blockPos, blockState))
            {
                world.setBlockState(blockPos, blockState, 2);
            }
        }
    }
}