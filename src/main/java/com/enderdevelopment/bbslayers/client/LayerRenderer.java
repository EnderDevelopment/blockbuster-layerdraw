package com.enderdevelopment.bbslayers.client;

import com.enderdevelopment.bbslayers.layer.LayerData;
import com.enderdevelopment.bbslayers.layer.TextureLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class LayerRenderer {
    @SubscribeEvent
    public void renderLayers(RenderLivingEvent.Post<EntityLivingBase> event) {
        EntityLivingBase entity = event.getEntity();
        if (!isBlockbusterActor(entity)) return;
        for (TextureLayer layer : LayerData.get(entity.world).getLayers(entity.getUniqueID().toString())) {
            renderLayer(event, layer);
        }
    }

    private void renderLayer(RenderLivingEvent.Post<EntityLivingBase> event, TextureLayer layer) {
        RenderLivingBase<EntityLivingBase> renderer = event.getRenderer();
        ModelBase model = renderer.getMainModel();
        EntityLivingBase entity = event.getEntity();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(layer.getTexture()));
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, layer.isAdditive() ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, layer.getOpacity());
        model.render(entity, event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), 0.0625F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private boolean isBlockbusterActor(EntityLivingBase entity) {
        String className = entity.getClass().getName();
        return className.equals("mchorse.blockbuster.entity.EntityActor") || className.endsWith(".EntityActor");
    }
}
