package net.chococraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.chococraft.Chococraft;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.inventory.ContainerSaddleBag;

public class GuiChocoboInventory extends GuiContainer
{
    private static final ResourceLocation INV_TEXTURE_NULL = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_inventory_null.png");
    private static final ResourceLocation INV_TEXTURE_SMALL = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_inventory_small.png");
    private static final ResourceLocation INV_TEXTURE_LARGE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_inventory_large.png");

    private EntityChocobo chocobo;
    private EntityPlayer player;

    public GuiChocoboInventory(EntityChocobo chocobo, EntityPlayer player)
    {
        super(new ContainerSaddleBag(chocobo, player));
        this.xSize = 176;
        this.ySize = 204;
        this.chocobo = chocobo;
        this.player = player;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        switch(chocobo.getSaddleType())
        {
        	default:
        		this.mc.getTextureManager().bindTexture(INV_TEXTURE_NULL);
        		break;
            case SADDLE_BAGS:
                this.mc.getTextureManager().bindTexture(INV_TEXTURE_SMALL);
                break;
            case PACK:
            	this.mc.getTextureManager().bindTexture(INV_TEXTURE_LARGE);
                break;
        }
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i - 24, j + 10, 0, 204, 27, 33);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.chocobo.getDisplayName().getUnformattedText(), 8, 6, 0x888888);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x888888);
    }
}
