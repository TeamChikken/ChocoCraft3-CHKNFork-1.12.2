package net.chococraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.chococraft.Chococraft;
import net.chococraft.common.inventory.ContainerNest;
import net.chococraft.common.tileentities.TileEntityChocoboNest;

public class GuiChocoboNest extends GuiContainer
{
    private final static ResourceLocation TEXTURE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_nest.png");
    private TileEntityChocoboNest tile;
    private EntityPlayer player;

    public GuiChocoboNest(TileEntityChocoboNest tile, EntityPlayer player)
    {
        super(new ContainerNest(tile, player));
        this.xSize = 176;
        this.ySize = 166;
        this.tile = tile;
        this.player = player;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String name = I18n.format("gui.choconest");
        int nameLength = this.fontRenderer.getStringWidth(name);
        this.fontRenderer.drawStringWithShadow(name, (this.xSize / 2) - (nameLength / 2), 4, -1);
    }
}
