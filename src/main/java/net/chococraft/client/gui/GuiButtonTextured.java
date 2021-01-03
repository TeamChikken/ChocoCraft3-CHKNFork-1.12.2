package net.chococraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonTextured extends GuiButton {
    private ResourceLocation resourceLocation;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;

    public GuiButtonTextured(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation resourceLocation, int textureX, int textureY, int textureWidth, int textureHeight) {
        super(buttonId, x, y, widthIn, heightIn, "");
        setTexture(resourceLocation, textureX, textureY, textureWidth, textureHeight);
    }

    public void setTexture(ResourceLocation resourceLocation, int textureX, int textureY, int textureWidth, int textureHeight) {
        this.resourceLocation = resourceLocation;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int hoverState = this.getHoverState(this.hovered);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + hoverState * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + hoverState * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);


            mc.getTextureManager().bindTexture(this.resourceLocation);

            this.drawTexturedModalRect(this.x + (this.width - this.textureWidth) / 2, this.y + (this.height - this.textureHeight) / 2, this.textureX, this.textureY, this.textureWidth, this.textureHeight);
        }
    }
}