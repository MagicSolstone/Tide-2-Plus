package net.minecraft.client.renderer.feature;

import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.SubmitNode;

@Environment(EnvType.CLIENT)
public record FeatureRendererType<Submit extends SubmitNode>(int id, String name) {
   private static final AtomicInteger NEXT_ID = new AtomicInteger();

   @Deprecated
   public FeatureRendererType(int id, String name) {
      this.id = id;
      this.name = name;
   }

   public static <Submit extends SubmitNode> FeatureRendererType<Submit> create(final String name) {
      return new FeatureRendererType<>(NEXT_ID.getAndIncrement(), name);
   }

   @Override
   public String toString() {
      return this.name;
   }
}
