package net.minecraft.world.flag;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;

public class FeatureFlagRegistry$Builder {
   private final FeatureFlagUniverse universe;
   private int id;
   private final Map<Identifier, FeatureFlag> flags = new LinkedHashMap<>();

   public FeatureFlagRegistry$Builder(final String universeId) {
      this.universe = new FeatureFlagUniverse(universeId);
   }

   public FeatureFlag createVanilla(final String name) {
      return this.create(Identifier.withDefaultNamespace(name));
   }

   public FeatureFlag create(final Identifier name) {
      if (this.id >= 64) {
         throw new IllegalStateException("Too many feature flags");
      } else {
         FeatureFlag result = new FeatureFlag(this.universe, this.id++);
         FeatureFlag previous = this.flags.put(name, result);
         if (previous != null) {
            throw new IllegalStateException("Duplicate feature flag " + name);
         } else {
            return result;
         }
      }
   }

   public FeatureFlagRegistry build() {
      FeatureFlagSet allValues = FeatureFlagSet.create(this.universe, this.flags.values());
      return new FeatureFlagRegistry(this.universe, allValues, Map.copyOf(this.flags));
   }
}
