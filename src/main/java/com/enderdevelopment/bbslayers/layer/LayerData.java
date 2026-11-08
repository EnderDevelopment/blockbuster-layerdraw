package com.enderdevelopment.bbslayers.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class LayerData extends WorldSavedData {
    private static final String DATA_NAME = "bbslayers_data";
    private final List<ActorLayers> actors = new ArrayList<ActorLayers>();

    public LayerData() { super(DATA_NAME); }
    public LayerData(String name) { super(name); }

    public static LayerData get(World world) {
        MapStorage storage = world.getMapStorage();
        LayerData data = (LayerData) storage.getOrLoadData(LayerData.class, DATA_NAME);
        if (data == null) { data = new LayerData(); storage.setData(DATA_NAME, data); }
        return data;
    }

    public synchronized List<TextureLayer> getLayers(String actorId) {
        ActorLayers actor = find(actorId);
        if (actor == null) return Collections.emptyList();
        List<TextureLayer> copy = new ArrayList<TextureLayer>(actor.layers);
        Collections.sort(copy, Comparator.comparingInt(TextureLayer::getOrder));
        return copy;
    }

    public synchronized void setLayer(String actorId, TextureLayer layer) {
        ActorLayers actor = getOrCreate(actorId);
        for (int i = 0; i < actor.layers.size(); i++) {
            if (actor.layers.get(i).getId().equalsIgnoreCase(layer.getId())) { actor.layers.set(i, layer); markDirty(); return; }
        }
        actor.layers.add(layer); markDirty();
    }

    public synchronized boolean removeLayer(String actorId, String layerId) {
        ActorLayers actor = find(actorId);
        if (actor == null) return false;
        boolean changed = false;
        for (int i = actor.layers.size() - 1; i >= 0; i--) {
            if (actor.layers.get(i).getId().equalsIgnoreCase(layerId)) { actor.layers.remove(i); changed = true; }
        }
        if (actor.layers.isEmpty()) actors.remove(actor);
        if (changed) markDirty();
        return changed;
    }

    public synchronized void clear(String actorId) {
        ActorLayers actor = find(actorId);
        if (actor != null) { actors.remove(actor); markDirty(); }
    }

    private ActorLayers find(String id) {
        for (ActorLayers actor : actors) if (actor.actorId.equalsIgnoreCase(id)) return actor;
        return null;
    }

    private ActorLayers getOrCreate(String id) {
        ActorLayers actor = find(id);
        if (actor == null) { actor = new ActorLayers(id); actors.add(actor); }
        return actor;
    }

    @Override public synchronized void readFromNBT(NBTTagCompound compound) {
        actors.clear();
        NBTTagList list = compound.getTagList("actors", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound actorTag = list.getCompoundTagAt(i);
            ActorLayers actor = new ActorLayers(actorTag.getString("actor"));
            NBTTagList layers = actorTag.getTagList("layers", 10);
            for (int j = 0; j < layers.tagCount(); j++) actor.layers.add(TextureLayer.read(layers.getCompoundTagAt(j)));
            actors.add(actor);
        }
    }

    @Override public synchronized NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (ActorLayers actor : actors) {
            NBTTagCompound actorTag = new NBTTagCompound();
            actorTag.setString("actor", actor.actorId);
            NBTTagList layers = new NBTTagList();
            for (TextureLayer layer : actor.layers) layers.appendTag(layer.write());
            actorTag.setTag("layers", layers); list.appendTag(actorTag);
        }
        compound.setTag("actors", list);
        return compound;
    }

    public static String actorId(World world, BlockPos pos) {
        return world.provider.getDimension() + "@" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    private static final class ActorLayers {
        private final String actorId;
        private final List<TextureLayer> layers = new ArrayList<TextureLayer>();
        private ActorLayers(String actorId) { this.actorId = actorId; }
    }
}
