package net.xalcon.chococraft.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.common.entities.EntityChocobo;
import net.xalcon.chococraft.common.entities.properties.ChocoboAttributes;

public class GuiChocoboInfo extends GuiScreen
{
    public final static ResourceLocation TEXTURE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_stats.png");
    private final EntityChocobo chocobo;

    private int xSize = 176;
    private int ySize = 89;
    private int guiLeft;
    private int guiTop;

    public GuiChocoboInfo(EntityChocobo chocobo)
    {
        this.chocobo = chocobo;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.guiLeft, this.guiTop, 0);
        this.drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);

        String name = this.chocobo.getDisplayName().getUnformattedText();
        int nameLength = this.fontRenderer.getStringWidth(name);
        this.fontRenderer.drawStringWithShadow(name, (this.xSize / 2) - (nameLength / 2), 4, -1);

        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawGenderInfo();
        this.drawHealthInfo();
        this.drawSpeedInfo();
        this.drawStaminaInfo();
        this.drawAbilityInfo();

        GlStateManager.popMatrix();
    }

    private void drawGenderInfo()
    {
        this.drawTexturedModalRect(26, 18, 176, this.chocobo.isMale() ? 16 : 0, 16, 16);
    }

    private void drawHealthInfo()
    {
        String value = String.valueOf((int) this.chocobo.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 70 - (width / 2), 30, -1);
    }

    private void drawSpeedInfo()
    {
        String value = String.valueOf((int) Math.round(this.chocobo.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 100));
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 106 - (width / 2), 30, -1);
    }

    private void drawStaminaInfo()
    {
        String value = String.valueOf((int) this.chocobo.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).getBaseValue());
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 143 - (width / 2), 30, -1);
    }

    private void drawAbilityInfo()
    {
        this.mc.getTextureManager().bindTexture(TEXTURE);
        if(!this.chocobo.canSprint())
            this.drawTexturedModalRect(26, 54, 176, 32, 16, 16);

        if(!this.chocobo.canGlide())
            this.drawTexturedModalRect(62, 54, 176, 32, 16, 16);

        if(this.chocobo.canDive())
            this.drawTexturedModalRect(98, 54, 176, 32, 16, 16);

        if(this.chocobo.canFly())
            this.drawTexturedModalRect(134, 54, 176, 32, 16, 16);
    }
}
