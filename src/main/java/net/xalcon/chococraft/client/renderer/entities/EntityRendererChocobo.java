package net.xalcon.chococraft.client.renderer.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
        super.renderLivingLabel(entityChocobo, str, x, y + 0.2d, z, maxDistance);
    }

    @Override
    public float handleRotationFloat(EntityChocobo entityChocobo, float f)
    {
        //Wing rotation
        return (MathHelper.sin(entityChocobo.wingRotation) + 1F) * entityChocobo.destPos;
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
        if (entityChocobo.getBagType() == EntityChocobo.BagType.PACK)
            path += "pack_bagged/";
        else if (entityChocobo.getBagType() == EntityChocobo.BagType.SADDLE)
            path += "saddle_bagged/";
        else if (entityChocobo.isSaddled())
            path += "saddled/";

        if(!entityChocobo.isChild())
            path += (entityChocobo.isMale() ? "male" : "female") + "/";
        path += entityChocobo.getChocoboColor().name().toLowerCase() + "chocobo.png";
        return new ResourceLocation(Chococraft.MODID, path);
    }
}
