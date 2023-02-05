package net.chococraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.chococraft.Chococraft;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.common.entities.properties.ChocoboAttributes;
import net.chococraft.common.handler.ExperienceHandler;
import net.chococraft.common.ChocoConfig;
import net.chococraft.common.network.PacketManager;
import net.chococraft.common.network.packets.PacketUpgradeChocobo;

import java.io.IOException;

public class GuiChocoboInfo extends GuiScreen {
    public final static ResourceLocation TEXTURE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_stats.png");

    private final EntityChocobo chocobo;
    private final EntityPlayer player;

    private int xSize = 176;
    private int ySize = 89;
    private int guiLeft;
    private int guiTop;

    public GuiChocoboInfo(EntityChocobo chocobo, EntityPlayer player) {
        this.chocobo = chocobo;
        this.player = player;
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButtonTextured(0, 24, 52, 20, 20, TEXTURE, 0, 105, 16, 16));
        this.buttonList.add(new GuiButtonTextured(1, 60, 52, 20, 20, TEXTURE, 16, 105, 16, 16));
        this.buttonList.add(new GuiButtonTextured(2, 96, 52, 20, 20, TEXTURE, 32, 105, 16, 16));
        this.buttonList.add(new GuiButtonTextured(3, 132, 52, 20, 20, TEXTURE, 48, 105, 16, 16));
    }

    /*
        SKILL ID Numbers
        1	-	SPRINT
        2	-	GLIDE
        3	-	DIVE
        4	-	FLY
     */
    @Override
    protected void actionPerformed(GuiButton b) {
        PacketUpgradeChocobo packet = new PacketUpgradeChocobo(chocobo, b.id + 1);
        PacketManager.INSTANCE.sendToServer(packet);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;

        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.guiLeft, this.guiTop, 0);

        this.drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);

        String name = this.chocobo.getDisplayName().getUnformattedText();
        int nameLength = this.fontRenderer.getStringWidth(name);
        this.fontRenderer.drawStringWithShadow(name, (this.xSize / 2) - (nameLength / 2), 4, -1);

        String ownerText = I18n.format("gui.chocoinfo.text.not_tamed");
        if (chocobo.isTamed()) {
            EntityLivingBase owner = chocobo.getOwner();
            if (owner == null)
                ownerText = I18n.format("gui.chocoinfo.text.unknown_owner");
            else
                ownerText = I18n.format("gui.chocoinfo.text.owner_format", owner.getDisplayName().getUnformattedText());
        }
        int ownerTextLength = this.fontRenderer.getStringWidth(ownerText);
        this.fontRenderer.drawStringWithShadow(ownerText, (this.xSize / 2) - (ownerTextLength / 2), 74, -1);

        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawGenderInfo();
        this.drawHealthInfo();
        this.drawSpeedInfo();
        this.drawStaminaInfo();

        this.updateButtonTextures();

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.interceptButtons();

        this.drawHover(mouseX, mouseY);

        GlStateManager.popMatrix();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;

        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    private void updateButtonTextures() {
        for (GuiButton btn : buttonList) {
            GuiButtonTextured btnt = (GuiButtonTextured) btn;
            btnt.setTexture(TEXTURE, btnt.id * 16, canUseAbility(btn.id) ? 89 : 105, 16, 16);
        }
    }

    private void interceptButtons() {
        for (GuiButton btn : buttonList) {
            btn.enabled = (getAbilityXPCost(btn.id) <= ExperienceHandler.getExperience(player)) && !canUseAbility(btn.id);

            if (btn.isMouseOver()) {
                if (canUseAbility(btn.id)) {
                    String tooltip = I18n.format("gui.chocoinfo.button.already_unlocked_ability", I18n.format(getAbilityFromButton(btn.id)));
                    int width = this.fontRenderer.getStringWidth(tooltip);
                    this.fontRenderer.drawString(tooltip, 88 - (width / 2), 93, -1, true);
                } else {
                    String tooltip = I18n.format("gui.chocoinfo.button.ability", ExperienceHandler.getExperience(player), getAbilityXPCost(btn.id), I18n.format(getAbilityFromButton(btn.id)));
                    int width = this.fontRenderer.getStringWidth(tooltip);
                    this.fontRenderer.drawString(tooltip, 88 - (width / 2), 93, -1, true);
                }
            }
        }
    }

    private void drawGenderInfo() {
        this.drawTexturedModalRect(26, 18, 176, this.chocobo.isMale() ? 16 : 0, 16, 16);

        String value = I18n.format(this.chocobo.isMale() ? "gui.chocoinfo.texture.male" : "gui.chocoinfo.texture.female");
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 35 - (width / 2), 36, -1);
    }

    private void drawHealthInfo() {
        String value = String.valueOf((int) this.chocobo.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 70 - (width / 2), 36, -1);
    }

    private void drawSpeedInfo() {
        String value = String.valueOf((int) Math.round(this.chocobo.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 100));
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 106 - (width / 2), 36, -1);
    }

    private void drawStaminaInfo() {
        String value = String.valueOf((int) this.chocobo.getEntityAttribute(ChocoboAttributes.MAX_STAMINA).getBaseValue());
        int width = this.fontRenderer.getStringWidth(value);
        this.fontRenderer.drawStringWithShadow(value, 142 - (width / 2), 36, -1);
    }

    private void drawHover(int mouseX, int mouseY) {
        if (mouseX >= 25 && mouseY >= 17 && mouseX < 25 + 18 && mouseY < 17 + 18)
            this.drawHoveringText(I18n.format("gui.chocoinfo.texture.gender"), mouseX, mouseY);

        if (mouseX >= 61 && mouseY >= 17 && mouseX < 61 + 18 && mouseY < 17 + 18)
            this.drawHoveringText(I18n.format("gui.chocoinfo.texture.health"), mouseX, mouseY);

        if (mouseX >= 97 && mouseY >= 17 && mouseX < 97 + 18 && mouseY < 17 + 18)
            this.drawHoveringText(I18n.format("gui.chocoinfo.texture.speed"), mouseX, mouseY);

        if (mouseX >= 133 && mouseY >= 17 && mouseX < 133 + 18 && mouseY < 17 + 18)
            this.drawHoveringText(I18n.format("gui.chocoinfo.texture.stamina"), mouseX, mouseY);

        for (GuiButton btn : buttonList) {
            if (btn.isMouseOver()) {
                this.drawHoveringText(I18n.format("gui.chocoinfo.button.button_format", I18n.format(getAbilityFromButton(btn.id))), mouseX, mouseY);
            }
        }
    }

    private boolean canUseAbility(int i) {
        switch (i) {
            case 0:
                return chocobo.canSprint();
            case 1:
                return chocobo.canGlide();
            case 2:
                return chocobo.canDive();
            case 3:
                return chocobo.canFly();
        }

        return false;
    }

    private String getAbilityFromButton(int i) {
        String key = "gui.chocoinfo.button.";
        switch (i) {
            case 0:
                return key + "sprint";
            case 1:
                return key + "glide";
            case 2:
                return key + "dive";
            case 3:
                return key + "fly";
            default:
                return key + "";
        }
    }

    private int getAbilityXPCost(int i) {
        switch (i) {
            case 0:
                return ChocoConfig.chocobo.ExpCostSprint;
            case 1:
                return ChocoConfig.chocobo.ExpCostGlide;
            case 2:
                return ChocoConfig.chocobo.ExpCostDive;
            case 3:
                return ChocoConfig.chocobo.ExpCostFly;
            default:
                return 0;
        }
    }
}
