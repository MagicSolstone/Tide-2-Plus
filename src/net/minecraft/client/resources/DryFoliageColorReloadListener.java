package net.minecraft.client.resources;

import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.DryFoliageColor;

@Environment(EnvType.CLIENT)
public class DryFoliageColorReloadListener extends SimplePreparableReloadListener<int[]> {
   private static final Identifier LOCATION = Identifier.withDefaultNamespace("textures/colormap/dry_foliage.png");

   protected int[] prepare(final ResourceManager manager, final ProfilerFiller profiler) {
      try {
         return LegacyStuffWrapper.getPixels(manager, LOCATION);
      } catch (IOException var4) {
         throw new IllegalStateException("Failed to load dry foliage color texture", var4);
      }
   }

   protected void apply(final int[] pixels, final ResourceManager manager, final ProfilerFiller profiler) {
      DryFoliageColor.init(pixels);
   }
}
