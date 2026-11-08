package com.enderdevelopment.bbslayers.proxy;

import com.enderdevelopment.bbslayers.client.LayerEditorKeyHandler;
import com.enderdevelopment.bbslayers.client.LayerRenderer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override public void preInit() {
        MinecraftForge.EVENT_BUS.register(new LayerRenderer());
        MinecraftForge.EVENT_BUS.register(new LayerEditorKeyHandler());
    }
}
