package net.slayer5934.chococraft.client.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.slayer5934.chococraft.Chococraft;


public class GuiChocoboBook extends GuiScreen
{
    public final static ResourceLocation TEXTURE = new ResourceLocation(Chococraft.MODID, "textures/gui/chocobo_book.png");
    
    private final EntityPlayer player;

    private int xSize = 130;
    private int ySize = 185;
    private int currentpage = 1;
    private int guiLeft;
    private int guiTop;

    public GuiChocoboBook(EntityPlayer player)
    {
        this.player = player;
    }
    
    @Override
    public void initGui()
    {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
        this.buttonList.add(new GuiButton( 1, this.guiLeft, this.guiTop + 165, 20, 20, I18n.format("<")));
        this.buttonList.add(new GuiButton( 2, (this.guiLeft+xSize) - 20, this.guiTop + 165, 20, 20, I18n.format(">")));
        // without this button a weird highlight effect happens to the main gui... dont ask me why, i dont understand it :[
       this.buttonList.add(new GuiButton( 0, this.guiLeft+180, this.guiTop+80, 0, 0, ""));
    }
    
    @Override
    protected void actionPerformed(GuiButton b)
    {
		switch(b.id)
		{
			case 1: this.currentpage = (this.currentpage <= 1 ? 6 : this.currentpage-1);
			break;	
			case 2: this.currentpage = (this.currentpage >= 6 ? 1 : this.currentpage+1);
            break;	
		}
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

        String name = I18n.format("gui.chocobook.title") + currentpage;
        int nameLength = this.fontRenderer.getStringWidth(name);
        this.fontRenderer.drawStringWithShadow(name, (this.xSize / 2) - (nameLength / 2), 4, -1);
        
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.renderpage();
        
        GlStateManager.popMatrix();
    }

    private void renderpage()
    {
        this.fontRenderer.drawSplitString(I18n.format("gui.chocobook.page" + (currentpage)), 5, 20, 120, 0);
    }
    
}
