package net.xalcon.chococraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.inventory.ContainerNest;
import net.xalcon.chococraft.common.tileentities.TileEntityChocoboNest;

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
        this.fontRenderer.drawString(this.tile.getDisplayName().getUnformattedText(), 8, 6, 0x404040);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }
}
