package net.slayer5934.chococraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.slayer5934.chococraft.Chococraft;
import net.slayer5934.chococraft.common.entities.EntityChocobo;

@Mod.EventBusSubscriber(modid = Chococraft.MODID, value = Side.CLIENT)
public class GuiChocoboIngameOverlay
{
	public static final ResourceLocation ICONS = new ResourceLocation(Chococraft.MODID, "textures/gui/icons.png");

	@SubscribeEvent
	public static void onGuiInagmeOverlayRender(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() != RenderGameOverlayEvent.ElementType.HEALTHMOUNT) return;

		Entity mountedEntity = Minecraft.getMinecraft().player.getRidingEntity();
		if(!(mountedEntity instanceof EntityChocobo)) return;
		EntityChocobo chocobo = (EntityChocobo) mountedEntity;

		Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);

		final int width = event.getResolution().getScaledWidth();
		final int height = event.getResolution().getScaledHeight();
		int left_align = width / 2 + 91;
		int top = height - GuiIngameForge.right_height;
		float staminaPercentage = chocobo.getStaminaPercentage() * 10;

		for (int i = 0; i < 10; ++i)
		{
			int x = left_align - i * 8 - 9;
			if(i >= staminaPercentage)
			{
				// render empty
				Gui.drawModalRectWithCustomSizedTexture(x, top, 0, 0, 9, 9, 32, 32);
			}
			else
			{
				if(i == ((int)staminaPercentage))
				{
					// draw partial
					Gui.drawModalRectWithCustomSizedTexture(x, top, 0, 0, 9, 9, 32, 32);
					int iconHeight = (int) (9 * (staminaPercentage - ((int) staminaPercentage)));
					Gui.drawModalRectWithCustomSizedTexture(x, top + (9 - iconHeight), 0, 18 + (9 - iconHeight), 9, iconHeight, 32, 32);
				}
				else
				{
					// draw full
					Gui.drawModalRectWithCustomSizedTexture(x, top, 0, 18, 9, 9, 32, 32);
				}
			}
		}
	}
}
