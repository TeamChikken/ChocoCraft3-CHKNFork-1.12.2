package net.xalcon.chococraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.inventory.ContainerSaddleBag;

public class GuiChocobo extends GuiContainer
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_inventory.png");

    public GuiChocobo(EntityChocobo chocobo, EntityPlayer player)
    {
        super(new ContainerSaddleBag(chocobo, player));
        this.xSize = 176;
        this.ySize = 204;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i - 24, j + 40, 0, 204, 27, 33);
    }
}
