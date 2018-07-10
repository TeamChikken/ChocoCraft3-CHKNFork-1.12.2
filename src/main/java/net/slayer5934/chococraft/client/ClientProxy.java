package net.slayer5934.chococraft.client;

import net.minecraft.client.Minecraft;
import net.slayer5934.chococraft.client.gui.GuiChocoboInfo;
import net.slayer5934.chococraft.common.CommonProxy;
import net.slayer5934.chococraft.common.entities.EntityChocobo;

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
