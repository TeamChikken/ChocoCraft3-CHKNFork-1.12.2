package net.slayer5934.chococraft.client.renderer.layers;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.entities.EntityChocobo;

public class LayerSaddle<T extends EntityChocobo> implements LayerRenderer<T> {
	private RenderLivingBase renderer;
	
	private ResourceLocation SADDLE = new ResourceLocation(Chococraft.MODID, "textures/entities/chocobos/saddle.png");
	private ResourceLocation SADDLE_BAG = new ResourceLocation(Chococraft.MODID, "textures/entities/chocobos/saddle_bag.png");
	private ResourceLocation PACK_BAG = new ResourceLocation(Chococraft.MODID, "textures/entities/chocobos/pack_bag.png");
	
	public LayerSaddle(RenderLivingBase renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isSaddled()) {
			ResourceLocation temp = null;
			
			switch (entitylivingbaseIn.getSaddleType()) {
				case SADDLE:
					temp = SADDLE;
					break;
				case SADDLE_BAGS:
					temp = SADDLE_BAG;
					break;
				case PACK:
					temp = PACK_BAG;
					break;
			}
			
			this.renderer.bindTexture(temp);
			this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			
		}
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}
