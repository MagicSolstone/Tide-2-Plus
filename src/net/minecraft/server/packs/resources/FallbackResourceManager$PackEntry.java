package net.minecraft.server.packs.resources;

import java.util.Collection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackResources$Filter;
import org.jspecify.annotations.Nullable;

record FallbackResourceManager$PackEntry(String name, @Nullable PackResources resources, @Nullable PackResources$Filter filter) {
   public void filterAll(final Collection<Identifier> collection) {
      if (this.filter != null) {
         collection.removeIf(this.filter::isFiltered);
      }
   }

   public boolean isFiltered(final Identifier location) {
      return this.filter != null && this.filter.isFiltered(location);
   }
}
