package net.xalcon.chococraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.network.PacketManager;
import net.xalcon.chococraft.common.network.packets.PacketChocoboSprinting;

@Mod.EventBusSubscriber(modid = Chococraft.MODID, value = Side.CLIENT)
public class ChocoboSprintingEventHandler
{
    private static boolean isSprinting = false;

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event)
    {
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isRiding())
        {
            KeyBinding keyBinding = Minecraft.getMinecraft().gameSettings.keyBindSprint;
            if(keyBinding.isPressed())
            {
                if(!isSprinting)
                {
                    isSprinting = true;
                    PacketManager.INSTANCE.sendToServer(new PacketChocoboSprinting(true));
                }
            }
            else
            {
                if(isSprinting)
                {
                    isSprinting = false;
                    PacketManager.INSTANCE.sendToServer(new PacketChocoboSprinting(false));
                }
            }
        }
        else
        {
            isSprinting = false;
        }
    }
}
