package net.minecraft.client.resources.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.cuboid.MissingCuboidModel;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ModelDiscovery {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Object2ObjectMap<Identifier, ModelDiscovery$ModelWrapper> modelWrappers = new Object2ObjectOpenHashMap();
   private final ModelDiscovery$ModelWrapper missingModel;
   private final Object2ObjectFunction<Identifier, ModelDiscovery$ModelWrapper> uncachedResolver;
   private final ResolvableModel$Resolver resolver;
   private final Queue<ModelDiscovery$ModelWrapper> parentDiscoveryQueue = new ArrayDeque<>();

   public ModelDiscovery(final Map<Identifier, UnbakedModel> unbakedModels, final UnbakedModel missingUnbakedModel) {
      this.missingModel = new ModelDiscovery$ModelWrapper(MissingCuboidModel.LOCATION, missingUnbakedModel, true);
      this.modelWrappers.put(MissingCuboidModel.LOCATION, this.missingModel);
      this.uncachedResolver = rawId -> {
         Identifier id = (Identifier)rawId;
         UnbakedModel rawModel = unbakedModels.get(id);
         if (rawModel == null) {
            LOGGER.warn("Missing block model: {}", id);
            return this.missingModel;
         } else {
            return this.createAndQueueWrapper(id, rawModel);
         }
      };
      this.resolver = this::getOrCreateModel;
   }

   private static boolean isRoot(final UnbakedModel model) {
      return model.parent() == null;
   }

   private ModelDiscovery$ModelWrapper getOrCreateModel(final Identifier id) {
      return (ModelDiscovery$ModelWrapper)this.modelWrappers.computeIfAbsent(id, this.uncachedResolver);
   }

   private ModelDiscovery$ModelWrapper createAndQueueWrapper(final Identifier id, final UnbakedModel rawModel) {
      boolean isRoot = isRoot(rawModel);
      ModelDiscovery$ModelWrapper result = new ModelDiscovery$ModelWrapper(id, rawModel, isRoot);
      if (!isRoot) {
         this.parentDiscoveryQueue.add(result);
      }

      return result;
   }

   public void addRoot(final ResolvableModel model) {
      model.resolveDependencies(this.resolver);
   }

   public void addSpecialModel(final Identifier id, final UnbakedModel model) {
      if (!isRoot(model)) {
         LOGGER.warn("Trying to add non-root special model {}, ignoring", id);
      } else {
         ModelDiscovery$ModelWrapper previous = (ModelDiscovery$ModelWrapper)this.modelWrappers.put(id, this.createAndQueueWrapper(id, model));
         if (previous != null) {
            LOGGER.warn("Duplicate special model {}", id);
         }
      }
   }

   public ResolvedModel missingModel() {
      return this.missingModel;
   }

   public Map<Identifier, ResolvedModel> resolve() {
      List<ModelDiscovery$ModelWrapper> toValidate = new ArrayList<>();
      this.discoverDependencies(toValidate);
      propagateValidity(toValidate);
      Builder<Identifier, ResolvedModel> result = ImmutableMap.builder();
      this.modelWrappers.forEach((location, model) -> {
         if (model.valid) {
            result.put(location, model);
         } else {
            LOGGER.warn("Model {} ignored due to cyclic dependency", location);
         }
      });
      return result.build();
   }

   private void discoverDependencies(final List<ModelDiscovery$ModelWrapper> toValidate) {
      ModelDiscovery$ModelWrapper current;
      while ((current = this.parentDiscoveryQueue.poll()) != null) {
         Identifier parentLocation = Objects.requireNonNull(current.wrapped.parent());
         ModelDiscovery$ModelWrapper parent = this.getOrCreateModel(parentLocation);
         current.parent = parent;
         if (parent.valid) {
            current.valid = true;
         } else {
            toValidate.add(current);
         }
      }
   }

   private static void propagateValidity(final List<ModelDiscovery$ModelWrapper> toValidate) {
      boolean progressed = true;

      while (progressed) {
         progressed = false;
         Iterator<ModelDiscovery$ModelWrapper> iterator = toValidate.iterator();

         while (iterator.hasNext()) {
            ModelDiscovery$ModelWrapper model = iterator.next();
            if (Objects.requireNonNull(model.parent).valid) {
               model.valid = true;
               iterator.remove();
               progressed = true;
            }
         }
      }
   }
}
