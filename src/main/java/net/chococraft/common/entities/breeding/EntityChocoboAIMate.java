package net.chococraft.common.entities.breeding;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.chococraft.Chococraft;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.init.ModBlocks;
import net.chococraft.common.tileentities.TileEntityChocoboEgg;
import net.chococraft.utils.WorldUtils;

public class EntityChocoboAIMate extends EntityAIBase {
    private final static Vec3i[] LAY_EGG_CHECK_OFFSETS =
            {
                    new Vec3i(0, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(-1, 0, -1),
                    new Vec3i(0, 0, -1), new Vec3i(+1, 0, -1), new Vec3i(+1, 0, 0),
                    new Vec3i(+1, 0, +1), new Vec3i(0, 0, +1), new Vec3i(-1, 0, +1),

                    new Vec3i(0, 1, 0), new Vec3i(-1, 1, 0), new Vec3i(-1, 1, -1),
                    new Vec3i(0, 1, -1), new Vec3i(+1, 1, -1), new Vec3i(+1, 1, 0),
                    new Vec3i(+1, 1, +1), new Vec3i(0, 1, +1), new Vec3i(-1, 1, +1),
            };

    private final EntityChocobo chocobo;
    private final World world;
    private final double moveSpeed;
    private EntityChocobo targetMate;
    private int spawnBabyDelay;

    public EntityChocoboAIMate(EntityChocobo chocobo, double moveSpeed) {
        this.chocobo = chocobo;
        this.world = chocobo.world;
        this.moveSpeed = moveSpeed;
        this.setMutexBits(3); // TODO: What is this?
    }

    @Override
    public boolean shouldExecute() {
        return this.chocobo.isInLove() && (this.targetMate = this.getNearbyMate()) != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void updateTask() {
        this.chocobo.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.chocobo.getVerticalFaceSpeed());
        this.chocobo.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.chocobo.getDistanceSq(this.targetMate) < 9.0D) {
            this.spawnEgg();
        }
    }

    @Nullable
    private EntityChocobo getNearbyMate() {
        List<EntityChocobo> list = this.world.getEntitiesWithinAABB(EntityChocobo.class, this.chocobo.getEntityBoundingBox().grow(8.0D));
        double dist = Double.MAX_VALUE;
        EntityChocobo closestMate = null;

        for (EntityChocobo entry : list) {
            if (this.chocobo.canMateWith(entry) && this.chocobo.getDistanceSq(entry) < dist) {
                closestMate = entry;
                dist = this.chocobo.getDistanceSq(entry);
            }
        }

        return closestMate;
    }

    private void spawnEgg() {
        if (this.chocobo.isMale()) return;

        this.chocobo.setGrowingAge(6000);
        this.targetMate.setGrowingAge(6000);
        this.chocobo.resetInLove();
        this.targetMate.resetInLove();

        BlockPos pos = this.chocobo.getPosition();
        for (Vec3i offset : LAY_EGG_CHECK_OFFSETS) {
            BlockPos offsetPos = pos.add(offset);
            IBlockState state = this.world.getBlockState(offsetPos);
            if (state.getMaterial().isReplaceable() && !state.getMaterial().isLiquid() && ModBlocks.chocoboEgg.canPlaceBlockAt(this.world, offsetPos)) {
                if (!this.world.setBlockState(offsetPos, ModBlocks.chocoboEgg.getDefaultState())) {
                    Chococraft.log.error("Unable to place egg @ {}, setBlockState() returned false!", offsetPos);
                    return;
                }

                TileEntityChocoboEgg eggTile = WorldUtils.getTileEntitySafe(this.world, offsetPos, TileEntityChocoboEgg.class);
                if (eggTile == null) {
                    Chococraft.log.error("Unable to place egg @ {}, no tile entity was found at the given position!", offsetPos);
                    return;
                }

                eggTile.setBreedInfo(new ChocoboBreedInfo(new ChocoboStatSnapshot(this.chocobo), new ChocoboStatSnapshot(this.targetMate)));
                return;
            }
        }

    }
}
