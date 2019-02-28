package net.slayer5934.chococraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.entities.EntityChocobo;
import net.slayer5934.chococraft.common.entities.properties.ChocoboAttributes;
import net.slayer5934.chococraft.common.handler.ExperienceHandler;
import net.slayer5934.chococraft.common.ChocoConfig;

public class GuiChocoboInfo extends GuiScreen
{
    public final static ResourceLocation TEXTURE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_stats.png");
    
    private final EntityChocobo chocobo;
    private final EntityPlayer player;
    
    private int sprint = ChocoConfig.chocobo.ExpCostSprint;
    private int glide = ChocoConfig.chocobo.ExpCostGlide;
    private int dive = ChocoConfig.chocobo.ExpCostDive;
    private int fly = ChocoConfig.chocobo.ExpCostFly;

    private int xSize = 176;
    private int ySize = 89;
    private int guiLeft;
    private int guiTop;

    public GuiChocoboInfo(EntityChocobo chocobo, EntityPlayer player)
    {
        this.chocobo = chocobo;
        this.player = player;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
        this.buttonList.add(new GuiButton( 1, this.guiLeft+180, this.guiTop+01, 80, 20, "Unlock Sprint"));
        this.buttonList.add(new GuiButton( 2, this.guiLeft+180, this.guiTop+23, 80, 20, "Unlock Glide"));
        this.buttonList.add(new GuiButton( 3, this.guiLeft+180, this.guiTop+45, 80, 20, "Unlock Dive"));
        this.buttonList.add(new GuiButton( 4, this.guiLeft+180, this.guiTop+67, 80, 20, "Unlock Fly"));
        // without this button a weird highlight effect happens to the main gui... dont ask me why, i dont understand it :[
        this.buttonList.add(new GuiButton( 0, this.guiLeft+180, this.guiTop+80, 0, 0, ""));
        
    }
   	// if the user clicks the buttonzzz
    @Override
    protected void actionPerformed(GuiButton b)
    {
    	 if(b.id == 1)
    	 {
    		 if (sprint <= ExperienceHandler.getExperience(this.player))
    		 {
    		 ExperienceHandler.removeExperience(this.player, sprint);
    		 chocobo.setCanSprint(true);
    		 }
    	 }
    	 
    	 if(b.id == 2)
    	 {
    		 if (glide <= ExperienceHandler.getExperience(this.player))
    		 {
    		 ExperienceHandler.removeExperience(this.player, glide);
    		 chocobo.setCanGlide(true);
    		 }
    	 }
    	 
    	 if(b.id == 3)
    	 {
    		 if (dive <= ExperienceHandler.getExperience(this.player))
    		 {
    		 ExperienceHandler.removeExperience(this.player, dive);
    		 chocobo.setCanDive(true);
    		 }
    	 }
    	 
    	 if(b.id == 4)
    	 {
    		 if (fly <= ExperienceHandler.getExperience(this.player))
    		 {
    		 ExperienceHandler.removeExperience(this.player, fly);
    		 chocobo.setCanFly(true);
    		 }
    	 }
    }
    //
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
        //interact with the buttons after generated and under everything else, otherwise it screws up..
        this.interceptButtons();
        //
        GlStateManager.popMatrix();
    }
    
    private void interceptButtons()
    {
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i) instanceof GuiButton) {
            	
                    GuiButton btn = (GuiButton) buttonList.get(i);
                    
                    // button initialization checks
                    if (i==0) {
                        if (sprint > ExperienceHandler.getExperience(this.player) || this.chocobo.canSprint())
                        	btn.enabled = false;
                        }
                    if (i==1) {
                        if (glide > ExperienceHandler.getExperience(this.player) || this.chocobo.canGlide())
                        	btn.enabled = false;
                        }
                    if (i==2) {
                        if (dive > ExperienceHandler.getExperience(this.player) || this.chocobo.canDive())
                        	btn.enabled = false;
                        }
                    if (i==3) {
                        if (fly > ExperienceHandler.getExperience(this.player) || this.chocobo.canFly())
                        	btn.enabled = false;
                        }
                    //
                    // button mouse over tooltips
                    if (btn.isMouseOver() && i==0 && !chocobo.canSprint()) {
                        String tooltip = ("You currently have " + ExperienceHandler.getExperience(this.player) + " out of " + sprint + " experience needed to unlock Sprint.");
                        int width = this.fontRenderer.getStringWidth(tooltip);
                        this.fontRenderer.drawStringWithShadow(tooltip, 88 - (width / 2), 93, -1);
                        }
                    if (btn.isMouseOver() && i==1 && !chocobo.canGlide()) {
                        String tooltip = ("You currently have " + ExperienceHandler.getExperience(this.player) + " out of " + glide + " experience needed to unlock Glide.");
                        int width = this.fontRenderer.getStringWidth(tooltip);
                        this.fontRenderer.drawStringWithShadow(tooltip, 88 - (width / 2), 93, -1);
                        }
                    if (btn.isMouseOver() && i==2 && !chocobo.canDive()) {
                        String tooltip = ("You currently have " + ExperienceHandler.getExperience(this.player) + " out of " + dive + " experience needed to unlock Dive.");
                        int width = this.fontRenderer.getStringWidth(tooltip);
                        this.fontRenderer.drawStringWithShadow(tooltip, 88 - (width / 2), 93, -1);
                        }
                    if (btn.isMouseOver() && i==3 && !chocobo.canFly()) {
                        String tooltip = ("You currently have " + ExperienceHandler.getExperience(this.player) + " out of " + fly + " experience needed to unlock Fly.");
                        int width = this.fontRenderer.getStringWidth(tooltip);
                        this.fontRenderer.drawStringWithShadow(tooltip, 88 - (width / 2), 93, -1);
                        }
                    //
            }
        }
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

        if(!this.chocobo.canDive())
            this.drawTexturedModalRect(98, 54, 176, 32, 16, 16);

        if(!this.chocobo.canFly())
            this.drawTexturedModalRect(134, 54, 176, 32, 16, 16);
    }
}
