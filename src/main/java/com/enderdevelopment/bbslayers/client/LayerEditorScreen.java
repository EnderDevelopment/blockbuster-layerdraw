package com.enderdevelopment.bbslayers.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class LayerEditorScreen extends GuiScreen {
    private static final int SIZE = 64;
    private static final int CANVAS_SIZE = 256;
    private final BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
    private DynamicTexture texture;
    private ResourceLocation textureLocation;
    private int brushColor = 0xFFFF3B30;
    private int brushSize = 2;
    private boolean drawing;

    public LayerEditorScreen() {
        clearImage();
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 130, height / 2 + 135, 80, 20, "Clear"));
        buttonList.add(new GuiButton(1, width / 2 - 42, height / 2 + 135, 80, 20, "Export PNG"));
        buttonList.add(new GuiButton(2, width / 2 + 46, height / 2 + 135, 80, 20, "Brush: " + brushSize));
        uploadTexture();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int left = width / 2 - CANVAS_SIZE / 2;
        int top = height / 2 - CANVAS_SIZE / 2 - 15;
        drawCenteredString(fontRenderer, "BBS Layer Editor 64x64", width / 2, top - 22, 0xFFFFFF);
        drawRect(left - 2, top - 2, left + CANVAS_SIZE + 2, top + CANVAS_SIZE + 2, 0xFF202020);
        if (textureLocation != null) {
            mc.getTextureManager().bindTexture(textureLocation);
            drawModalRectWithCustomSizedTexture(left, top, 0, 0, CANVAS_SIZE, CANVAS_SIZE, CANVAS_SIZE, CANVAS_SIZE);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0 && insideCanvas(mouseX, mouseY)) {
            drawing = true;
            paint(mouseX, mouseY);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (drawing && clickedMouseButton == 0) paint(mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        drawing = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            clearImage();
            uploadTexture();
        } else if (button.id == 1) {
            exportImage();
        } else if (button.id == 2) {
            brushSize = brushSize == 2 ? 4 : brushSize == 4 ? 8 : 2;
            button.displayString = "Brush: " + brushSize;
        }
    }

    private boolean insideCanvas(int mouseX, int mouseY) {
        int left = width / 2 - CANVAS_SIZE / 2;
        int top = height / 2 - CANVAS_SIZE / 2 - 15;
        return mouseX >= left && mouseX < left + CANVAS_SIZE && mouseY >= top && mouseY < top + CANVAS_SIZE;
    }

    private void paint(int mouseX, int mouseY) {
        int left = width / 2 - CANVAS_SIZE / 2;
        int top = height / 2 - CANVAS_SIZE / 2 - 15;
        int x = (mouseX - left) * SIZE / CANVAS_SIZE;
        int y = (mouseY - top) * SIZE / CANVAS_SIZE;
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(brushColor, true));
        graphics.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
        graphics.dispose();
        uploadTexture();
    }

    private void clearImage() {
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(java.awt.AlphaComposite.Clear);
        graphics.fillRect(0, 0, SIZE, SIZE);
        graphics.dispose();
    }

    private void uploadTexture() {
        if (texture == null) {
            texture = new DynamicTexture(image);
            textureLocation = mc.getTextureManager().getDynamicTextureLocation("bbslayers_editor", texture);
        } else {
            texture.updateDynamicTexture();
        }
    }

    private void exportImage() {
        File output = new File(Minecraft.getMinecraft().gameDir, "bbslayers/exported-layer.png");
        if (output.getParentFile() != null) output.getParentFile().mkdirs();
        try {
            ImageIO.write(image, "png", output);
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Layer exported to " + output.getPath()));
        } catch (IOException error) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Could not export layer: " + error.getMessage()));
        }
    }
}
