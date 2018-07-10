package net.slayer5934.chococraft.common;

import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.entities.EntityChocobo;

@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class RidingEventHandler
{
    @SubscribeEvent
    public static void onMountEntity(EntityMountEvent event)
    {
        if(event.isMounting()) return;
        if(event.getEntityBeingMounted().isDead) return;
        if(!(event.getEntityBeingMounted() instanceof EntityChocobo)) return;

        if(!event.getEntityBeingMounted().onGround)
            event.setCanceled(true);
    }
}
