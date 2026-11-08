package com.enderdevelopment.bbslayers;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.SidedProxy;
import org.apache.logging.log4j.Logger;
import com.enderdevelopment.bbslayers.proxy.CommonProxy;
import com.enderdevelopment.bbslayers.command.CommandLayer;

@Mod(modid = BbsLayersMod.MODID, name = BbsLayersMod.NAME, version = BbsLayersMod.VERSION, dependencies = "required-after:forge;required-after:blockbuster")
public class BbsLayersMod {
    public static final String MODID = "bbslayers";
    public static final String NAME = "Blockbuster Layered Textures";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static BbsLayersMod INSTANCE;

    @SidedProxy(clientSide = "com.enderdevelopment.bbslayers.proxy.ClientProxy", serverSide = "com.enderdevelopment.bbslayers.proxy.CommonProxy")
    public static CommonProxy PROXY;

    public static Logger LOGGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLayer());
    }
}
