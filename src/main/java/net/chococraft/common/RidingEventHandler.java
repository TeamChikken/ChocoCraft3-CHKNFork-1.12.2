package net.chococraft.common;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.chococraft.Chococraft;
import net.chococraft.common.entities.EntityChocobo;

@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class RidingEventHandler {
    @SubscribeEvent
    public static void onMountEntity(EntityMountEvent event) {
        if (event.isMounting()) return;
        if (event.getEntityBeingMounted().isDead) return;
        if (!(event.getEntityBeingMounted() instanceof EntityChocobo)) return;

        if (!event.getEntityBeingMounted().onGround && !event.getEntityBeingMounted().isRiding())
            event.setCanceled(true);
    }

    /* This Foricbly dismounts players that log out
     * when riding a chocobo to prevent them from
     * maintaining control over it upon logging back in
     */

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerLoggedOutEvent event) {
        if (event.player.isRiding()) {
            Entity entityRide = event.player.getRidingEntity();
            if (entityRide instanceof EntityChocobo) {
                event.player.dismountRidingEntity();
            }
        }
    }
}
