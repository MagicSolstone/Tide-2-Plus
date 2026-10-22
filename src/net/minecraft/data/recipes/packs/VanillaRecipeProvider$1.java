package net.minecraft.data.recipes.packs;

import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.MultiRegistryBootstrap$BootstrapGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

class VanillaRecipeProvider$1 implements MultiRegistryBootstrap {
   @Override
   public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
      return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
   }

   @Override
   public void run(final MultiRegistryBootstrap$BootstrapGetter registries) {
      new VanillaRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
   }
}
