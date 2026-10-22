package net.minecraft.server.packs.resources;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;

public interface ResourceManager extends ResourceProvider {
   Set<String> getNamespaces();

   List<Resource> getResourceStack(Identifier location);

   Map<Identifier, Resource> listResources(String directory, ResourceManager$Selector selector);

   Map<Identifier, List<Resource>> listResourceStacks(String directory, ResourceManager$Selector selector);

   Stream<PackResources> listPacks();
}
