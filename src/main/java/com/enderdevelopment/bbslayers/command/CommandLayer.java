package com.enderdevelopment.bbslayers.command;

import com.enderdevelopment.bbslayers.layer.LayerData;
import com.enderdevelopment.bbslayers.layer.TextureLayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import java.util.Collections;
import java.util.List;

public class CommandLayer extends CommandBase {
    @Override public String getName() { return "bbslayer"; }
    @Override public String getUsage(ICommandSender sender) { return "/bbslayer <add|remove|clear|list> <entityUuid> ..."; }
    @Override public int getRequiredPermissionLevel() { return 2; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length < 2) { sender.sendMessage(new TextComponentString(getUsage(sender))); return; }
        Entity entity;
        try { entity = getEntity(server, sender, args[1]); }
        catch (Exception error) { sender.sendMessage(new TextComponentString("Entity not found: " + args[1])); return; }
        String id = entity.getUniqueID().toString();
        LayerData data = LayerData.get(entity.world);
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 4) { sender.sendMessage(new TextComponentString("Usage: /bbslayer add <uuid> <layerId> <namespace:path> [order] [opacity] [additive]")); return; }
            try {
                int order = args.length > 4 ? parseInt(args[4], 0) : 0;
                float opacity = args.length > 5 ? Float.parseFloat(args[5]) : 1.0F;
                boolean additive = args.length > 6 && Boolean.parseBoolean(args[6]);
                ResourceLocation texture = new ResourceLocation(args[3]);
                data.setLayer(id, new TextureLayer(args[2], texture.toString(), order, opacity, additive));
                sender.sendMessage(new TextComponentString("Layer saved for " + id));
            } catch (IllegalArgumentException error) { sender.sendMessage(new TextComponentString("Invalid texture, order, or opacity.")); }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 3 || !data.removeLayer(id, args[2])) { sender.sendMessage(new TextComponentString("Layer not found.")); return; }
            sender.sendMessage(new TextComponentString("Layer removed."));
        } else if (args[0].equalsIgnoreCase("clear")) {
            data.clear(id); sender.sendMessage(new TextComponentString("All layers cleared."));
        } else if (args[0].equalsIgnoreCase("list")) {
            List<TextureLayer> layers = data.getLayers(id);
            sender.sendMessage(new TextComponentString("Layers: " + layers.size()));
            for (TextureLayer layer : layers) sender.sendMessage(new TextComponentString(layer.getId() + " -> " + layer.getTexture() + " order=" + layer.getOrder()));
        } else sender.sendMessage(new TextComponentString(getUsage(sender)));
    }

    @Override public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) { return Collections.emptyList(); }
    @Override public boolean isUsernameIndex(String[] args, int index) { return index == 1; }
}
