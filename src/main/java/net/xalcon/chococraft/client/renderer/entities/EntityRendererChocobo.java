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

public class EntityRendererChocobo extends RenderLiving
{
    public EntityRendererChocobo(RenderManager renderManager)
    {
        super(renderManager, new ModelChocobo(), 1.0f);
    }

    @Override
    public void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance)
    {
        super.renderLivingLabel(entityIn, str, x, y + 0.2d, z, maxDistance);
    }

    @Override
    public float handleRotationFloat(EntityLivingBase entityLiving, float f)
    {
        //Wing rotation
        EntityChocobo entityChocobo = (EntityChocobo) entityLiving;
        return (MathHelper.sin(entityChocobo.wingRotation) + 1F) * entityChocobo.destPos;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        //TODO big hack because the model is positioned wrong
        GlStateManager.translate(-0.075, 0, -0.45);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        EntityChocobo entityChocobo = (EntityChocobo) entity;
        String path = "textures/entities/chocobos/" + (entityChocobo.isTamed() ? "tamed" : "untamed") + "/";
        if (entityChocobo.getBagType() == EntityChocobo.BagType.PACK)
            path += "pack_bagged/";
        else if (entityChocobo.getBagType() == EntityChocobo.BagType.SADDLE)
            path += "saddle_bagged/";
        else if (entityChocobo.isSaddled())
            path += "saddled/";

        path += (entityChocobo.isMale() ? "male" : "female") + "/" + entityChocobo.getChocoboColor().name().toLowerCase() + "chocobo.png";
        return new ResourceLocation(Chococraft.MODID, path);
    }
}
