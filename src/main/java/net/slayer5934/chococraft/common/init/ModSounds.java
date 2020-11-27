package net.slayer5934.chococraft.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.slayer5934.chococraft.Chococraft;

@Mod.EventBusSubscriber(modid = Chococraft.MODID)
public class ModSounds
{
    public final static SoundEvent AMBIENT_SOUND = createSoundEvent("entity.chocobo.kweh");
    public final static SoundEvent WHISTLE_SOUND_FOLLOW = createSoundEvent("entity.chocobo.kwehwhistlefollow");
    public final static SoundEvent WHISTLE_SOUND_STAY = createSoundEvent("entity.chocobo.kwehwhistlestay");
    public final static SoundEvent WHISTLE_SOUND_WANDER = createSoundEvent("entity.chocobo.kwehwhistlewander");

    @SubscribeEvent
    public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().register(AMBIENT_SOUND);
        event.getRegistry().register(WHISTLE_SOUND_FOLLOW);
        event.getRegistry().register(WHISTLE_SOUND_STAY);
        event.getRegistry().register(WHISTLE_SOUND_WANDER);
    }

    private static SoundEvent createSoundEvent(String id)
    {
        SoundEvent sound = new SoundEvent(new ResourceLocation(Chococraft.MODID, id));
        sound.setRegistryName(id);
        return sound;
    }
}
