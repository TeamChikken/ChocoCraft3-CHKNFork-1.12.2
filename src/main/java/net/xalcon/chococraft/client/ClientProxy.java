package net.xalcon.chococraft.client;

import net.minecraft.client.Minecraft;
import net.xalcon.chococraft.client.gui.GuiChocoboInfo;
import net.xalcon.chococraft.common.CommonProxy;
import net.xalcon.chococraft.common.entities.EntityChocobo;

@SuppressWarnings("ALL")
public class ClientProxy extends CommonProxy
{
    @Override
    public void openChocoboInfoGui(EntityChocobo chocobo)
    {
        super.openChocoboInfoGui(chocobo);
        Minecraft.getMinecraft().displayGuiScreen(new GuiChocoboInfo(chocobo));
    }
}
