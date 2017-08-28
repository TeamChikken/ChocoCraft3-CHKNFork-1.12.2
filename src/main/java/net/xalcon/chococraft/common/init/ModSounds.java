package net.xalcon.chococraft.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.chococraft.Chococraft;

@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class ModSounds
{
    public final static SoundEvent AMBIENT_SOUND = createSoundEvent("entity.chocobo.kweh");

    @SubscribeEvent
    public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().register(AMBIENT_SOUND);
    }

    private static SoundEvent createSoundEvent(String id)
    {
        SoundEvent sound = new SoundEvent(new ResourceLocation(Chococraft.MODID, id));
        sound.setRegistryName(id);
        return sound;
    }
}
