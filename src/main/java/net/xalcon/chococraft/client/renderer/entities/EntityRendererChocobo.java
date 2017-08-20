package net.xalcon.chococraft.client.renderer.entities;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.xalcon.chococraft.Chococraft;
import net.xalcon.chococraft.client.models.entities.ModelChocobo;
import net.xalcon.chococraft.common.entities.EntityChocobo;

public class EntityRendererChocobo extends RenderLiving<EntityChocobo>
{
    public EntityRendererChocobo(RenderManager renderManager)
    {
        super(renderManager, new ModelChocobo(), 1.0f);
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
        String path = "textures/entities/" + type + "/" + (entityChocobo.isTamed() ? "tamed" : "untamed") + "/";
        switch (entityChocobo.getSaddleType())
        {
            case SADDLE:
                path += "saddled/";
                break;
            case SADDLE_BAGS:
                path += "saddle_bagged/";
                break;
            case PACK:
                path += "pack_bagged/";
                break;
        }
        if(!entityChocobo.isChild())
            path += (entityChocobo.isMale() ? "male" : "female") + "/";
        path += entityChocobo.getChocoboColor().name().toLowerCase() + "chocobo.png";
        return new ResourceLocation(Chococraft.MODID, path);
    }
}
