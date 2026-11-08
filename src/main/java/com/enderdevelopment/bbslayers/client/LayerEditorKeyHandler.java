package com.enderdevelopment.bbslayers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class LayerEditorKeyHandler {
    private final KeyBinding openEditor = new KeyBinding("Open BBS layer editor", Keyboard.KEY_L, "BBS Layers");

    public LayerEditorKeyHandler() {
        ClientRegistry.registerKeyBinding(openEditor);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openEditor.isPressed() && Minecraft.getMinecraft().currentScreen == null) {
            Minecraft.getMinecraft().displayGuiScreen(new LayerEditorScreen());
        }
    }
}
