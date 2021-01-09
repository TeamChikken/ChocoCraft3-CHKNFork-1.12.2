package net.chococraft.client.renderer.entities;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.chococraft.Chococraft;
import net.chococraft.client.renderer.layers.LayerCollar;
import net.chococraft.client.renderer.layers.LayerPlumage;
import net.chococraft.client.renderer.layers.LayerSaddle;
import net.chococraft.common.entities.EntityChocobo;
import net.chococraft.client.models.entities.ModelAdultChocobo;

public class EntityRendererChocobo extends RenderLiving<EntityChocobo>
{
    public EntityRendererChocobo(RenderManager renderManager)
    {
        super(renderManager, new ModelAdultChocobo(), 1.0f);
        
        this.addLayer(new LayerCollar<>(this));
        this.addLayer(new LayerPlumage<>(this));
        this.addLayer(new LayerSaddle<>(this));
    }

    @Override
    public void renderLivingLabel(EntityChocobo entityChocobo, String str, double x, double y, double z, int maxDistance)
    {
        // move the label up by 0.2 units
        super.renderLivingLabel(entityChocobo, str, x, y + 0.2d, z, maxDistance);
    }

    @Override
    protected void preRenderCallback(EntityChocobo entityChocobo, float f)
    {
        //TODO big hack because the model is positioned wrong
        if(!entityChocobo.isChild())
            GlStateManager.translate(-0.075, 0, -0.45);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChocobo entityChocobo)
    {
        String type = entityChocobo.isChild() ? "chicobos" : "chocobos";
        String path = "textures/entities/" + type + "/";
        path += entityChocobo.getChocoboColor().name().toLowerCase() + "chocobo.png";
        return new ResourceLocation(Chococraft.MODID, path);
    }
}
