# Minecraft Forge Mod: Blockbuster LayerDraw

Blockbuster LayerDraw is a Minecraft 1.12.2 Forge mod addon for Blockbuster that adds ordered texture layers to Blockbuster actor entities, including configurable opacity, additive blending, persistent world-save storage, and a client-side texture drawing editor.

## Install

1. Install Minecraft 1.12.2 with Forge 14.23.5.2860 or a compatible Forge 1.12.2 build.
2. Install the Blockbuster mod for Minecraft 1.12.2.
3. Build the project with Java 8:

   ```text
   ./gradlew build
   ```

4. Copy the generated `bbs-layers` JAR from `build/libs` into the Minecraft instance's `mods` folder.
5. Start Minecraft with the Forge profile.

## Use

1. Enter a world with a Blockbuster actor.
2. Give yourself operator permission or use a command source with permission level 2.
3. Add a texture layer to an actor:

   ```text
   /bbslayer add <entityUuid> <layerId> <namespace:path> [order] [opacity] [additive]
   ```

   Example:

   ```text
   /bbslayer add 00000000-0000-0000-0000-000000000000 armor mymod:layers/armor 1 0.75 false
   ```

4. Use a lower `order` value to render a layer earlier. The default order is `0`.
5. Set opacity from `0.0` to `1.0`. The default is `1.0`.
6. Set `additive` to `true` for additive blending or `false` for standard alpha blending.
7. View the configured layers:

   ```text
   /bbslayer list <entityUuid>
   ```

8. Remove one layer:

   ```text
   /bbslayer remove <entityUuid> <layerId>
   ```

9. Remove all layers from an actor:

   ```text
   /bbslayer clear <entityUuid>
   ```

10. Press `L` on the client to open the 64x64 drawing editor.
11. Draw with the left mouse button, use `Clear` to erase the canvas, and use the brush button to cycle through brush sizes 2, 4, and 8.
12. Select `Export PNG` to save the canvas to `bbslayers/exported-layer.png` in the Minecraft directory.
13. Copy the exported PNG into a resource pack or mod texture location, then reference it with its `namespace:path` texture identifier in the `add` command.

---

More Minecraft mod projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/1b4411be-c0ef-4bd8-8161-77cbb731a8a7?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=1b4411be-c0ef-4bd8-8161-77cbb731a8a7
