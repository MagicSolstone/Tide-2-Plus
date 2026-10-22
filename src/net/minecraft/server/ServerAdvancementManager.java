package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementTree;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter$Collector;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ServerAdvancementManager {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Map<Identifier, AdvancementHolder> advancements;
   private final AdvancementTree tree;

   public ServerAdvancementManager(final HolderLookup$Provider registries) {
      HolderLookup$RegistryLookup<Advancement> advancements = registries.lookupOrThrow(Registries.ADVANCEMENT);
      Builder<Identifier, AdvancementHolder> builder = ImmutableMap.builder();
      advancements.listElements().forEach(advancement -> {
         validate(registries, (Holder$Reference<Advancement>)advancement);
         builder.put(advancement.key().identifier(), new AdvancementHolder(advancement.key().identifier(), advancement.value()));
      });
      this.advancements = builder.buildOrThrow();
      AdvancementTree tree = new AdvancementTree();
      tree.addAll(this.advancements.values());
      tree.repositionNodes();
      this.tree = tree;
   }

   private static void validate(final HolderLookup$Provider registries, final Holder$Reference<Advancement> advancement) {
      ProblemReporter$Collector problemCollector = new ProblemReporter$Collector();
      advancement.value().validate(problemCollector, registries);
      if (!problemCollector.isEmpty()) {
         LOGGER.warn("Found validation problems in advancement {}: \n{}", advancement.key().identifier(), problemCollector.getReport());
      }
   }

   @Nullable
   public AdvancementHolder get(final Identifier id) {
      return this.advancements.get(id);
   }

   public AdvancementTree tree() {
      return this.tree;
   }

   public Collection<AdvancementHolder> getAllAdvancements() {
      return this.advancements.values();
   }
}
