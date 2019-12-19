package net.slayer5934.chococraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.ChocoConfig;
import net.slayer5934.chococraft.common.entities.EntityChocobo;
import net.slayer5934.chococraft.common.entities.properties.ChocoboAttributes;
import net.slayer5934.chococraft.common.handler.ExperienceHandler;
import net.slayer5934.chococraft.common.network.PacketManager;
import net.slayer5934.chococraft.common.network.packets.PacketUpgradeChocobo;

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
		
		this.buttonList.add(new GuiButton(1, this.guiLeft + 180, this.guiTop + 1, 80, 20, I18n.format("gui.chocoinfo.button.sprint")));
		this.buttonList.add(new GuiButton(2, this.guiLeft + 180, this.guiTop + 23, 80, 20, I18n.format("gui.chocoinfo.button.glide")));
		this.buttonList.add(new GuiButton(3, this.guiLeft + 180, this.guiTop + 45, 80, 20, I18n.format("gui.chocoinfo.button.dive")));
		this.buttonList.add(new GuiButton(4, this.guiLeft + 180, this.guiTop + 67, 80, 20, I18n.format("gui.chocoinfo.button.fly")));
		// without this button a weird highlight effect happens to the main gui... dont ask me why, i dont understand it :[
		this.buttonList.add(new GuiButton(0, this.guiLeft + 180, this.guiTop + 80, 0, 0, ""));
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
		switch (b.id) {
			case 1:
			case 2:
			case 3:
			case 4: {
				PacketUpgradeChocobo packet = new PacketUpgradeChocobo(chocobo, b.id);
				PacketManager.INSTANCE.sendToServer(packet);
			}
			default:
				break;
			
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
		//interact with the buttons after generated and under everything else, otherwise it screws up..
		this.interceptButtons();
		
		GlStateManager.popMatrix();
	}
	
	private void interceptButtons() {
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i) != null) {
				
				GuiButton btn = buttonList.get(i);
				
				if (i == 0 && (ChocoConfig.chocobo.ExpCostSprint > ExperienceHandler.getExperience(this.player) || this.chocobo.canSprint())) btn.enabled = false;
				
				if (i == 1 && (ChocoConfig.chocobo.ExpCostGlide > ExperienceHandler.getExperience(this.player) || this.chocobo.canGlide())) btn.enabled = false;
				
				if (i == 2 && (ChocoConfig.chocobo.ExpCostDive > ExperienceHandler.getExperience(this.player) || this.chocobo.canDive())) btn.enabled = false;
				
				if (i == 3 && (ChocoConfig.chocobo.ExpCostFly > ExperienceHandler.getExperience(this.player) || this.chocobo.canFly())) btn.enabled = false;
				
				if (btn.isMouseOver() && !canUseAbility(i)) {
					String tooltip = I18n.format("gui.chocoinfo.button.ability", ExperienceHandler.getExperience(player), getAbilityXPCost(i), I18n.format(getAbilityFromButton(i)));
					int width = this.fontRenderer.getStringWidth(tooltip);
					this.fontRenderer.drawString(tooltip, 88 - (width / 2), 93, -1, true);
				}
			}
		}
	}
	
	private void drawGenderInfo() {
		this.drawTexturedModalRect(26, 18, 176, this.chocobo.isMale() ? 16 : 0, 16, 16);
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
	
	private void drawAbilityInfo() {
		this.mc.getTextureManager().bindTexture(TEXTURE);
		if (!this.chocobo.canSprint()) this.drawTexturedModalRect(26, 54, 176, 32, 16, 16);
		
		if (!this.chocobo.canGlide()) this.drawTexturedModalRect(62, 54, 176, 32, 16, 16);
		
		if (!this.chocobo.canDive()) this.drawTexturedModalRect(98, 54, 176, 32, 16, 16);
		
		if (!this.chocobo.canFly()) this.drawTexturedModalRect(134, 54, 176, 32, 16, 16);
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
