package net.slayer5934.chococraft.client.renderer.layers;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.entities.EntityChocobo;

public class LayerPlumage<T extends EntityChocobo> implements LayerRenderer<T>
{
	private RenderLivingBase renderer;
	
	private ResourceLocation PLUMAGE = new ResourceLocation(Chococraft.MODID,"textures/entities/chocobos/plumage.png");
	
	public LayerPlumage(RenderLivingBase renderer)
	{
		this.renderer = renderer;
	}
	
	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if(!entitylivingbaseIn.isInvisible() && !entitylivingbaseIn.isChild() && entitylivingbaseIn.isMale())
		{
			this.renderer.bindTexture(PLUMAGE);
			renderer.getMainModel().render(entitylivingbaseIn,limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
	
	@Override
	public boolean shouldCombineTextures()
	{
		return true;
	}
}
