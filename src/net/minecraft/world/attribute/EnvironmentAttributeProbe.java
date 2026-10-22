package net.minecraft.world.attribute;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EnvironmentAttributeProbe {
   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeProbe$ValueProbe<?>> valueProbes = new Reference2ObjectOpenHashMap();
   private final Function<EnvironmentAttribute<?>, EnvironmentAttributeProbe$ValueProbe<?>> valueProbeFactory = x$0 -> new EnvironmentAttributeProbe$ValueProbe(
      this, x$0
   );
   @Nullable
   private Level level;
   @Nullable
   private Vec3 position;
   private final SpatialAttributeInterpolator biomeInterpolator = new SpatialAttributeInterpolator();

   public void reset() {
      this.level = null;
      this.position = null;
      this.biomeInterpolator.clear();
      this.valueProbes.clear();
   }

   public void tick(final Level level, final Vec3 position) {
      this.level = level;
      this.position = position;
      this.valueProbes.values().removeIf(EnvironmentAttributeProbe$ValueProbe::tick);
      this.biomeInterpolator.clear();
      GaussianSampler.sample(
         position.scale(0.25),
         level.getBiomeManager()::getNoiseBiomeAtQuart,
         (weight, biome) -> this.biomeInterpolator.accumulate(weight, biome.value().getAttributes())
      );
   }

   public <Value> Value getValue(final EnvironmentAttribute<Value> attribute, final float partialTicks) {
      EnvironmentAttributeProbe$ValueProbe<Value> valueProbe = (EnvironmentAttributeProbe$ValueProbe<Value>)this.valueProbes
         .computeIfAbsent(attribute, this.valueProbeFactory);
      return valueProbe.get(attribute, partialTicks);
   }
}
