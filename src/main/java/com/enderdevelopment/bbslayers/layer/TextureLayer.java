package com.enderdevelopment.bbslayers.layer;

import net.minecraft.nbt.NBTTagCompound;

public class TextureLayer {
    private final String id;
    private String texture;
    private int order;
    private float opacity;
    private boolean additive;

    public TextureLayer(String id, String texture, int order, float opacity, boolean additive) {
        this.id = id;
        this.texture = texture;
        this.order = order;
        this.opacity = clamp(opacity);
        this.additive = additive;
    }

    public String getId() { return id; }
    public String getTexture() { return texture; }
    public int getOrder() { return order; }
    public float getOpacity() { return opacity; }
    public boolean isAdditive() { return additive; }
    public void setTexture(String texture) { this.texture = texture; }
    public void setOrder(int order) { this.order = order; }
    public void setOpacity(float opacity) { this.opacity = clamp(opacity); }
    public void setAdditive(boolean additive) { this.additive = additive; }

    public NBTTagCompound write() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", id);
        tag.setString("texture", texture);
        tag.setInteger("order", order);
        tag.setFloat("opacity", opacity);
        tag.setBoolean("additive", additive);
        return tag;
    }

    public static TextureLayer read(NBTTagCompound tag) {
        return new TextureLayer(tag.getString("id"), tag.getString("texture"), tag.getInteger("order"), tag.getFloat("opacity"), tag.getBoolean("additive"));
    }

    private static float clamp(float value) { return Math.max(0.0F, Math.min(1.0F, value)); }
}
