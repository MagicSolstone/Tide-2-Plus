package net.minecraft.client.renderer.item;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.CacheSlot;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty$Type;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import net.minecraft.util.RegistryContextSwapper;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record SelectItemModel$UnbakedSwitch<P extends SelectItemModelProperty<T>, T>(P property, List<SelectItemModel$SwitchCase<T>> cases) {
   public static final MapCodec<SelectItemModel$UnbakedSwitch<?, ?>> MAP_CODEC = SelectItemModelProperties.CODEC
      .dispatchMap("property", unbaked -> unbaked.property().type(), SelectItemModelProperty$Type::switchCodec);

   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation, final ItemModel fallback) {
      Object2ObjectMap<T, ItemModel> bakedModels = new Object2ObjectOpenHashMap();

      for (SelectItemModel$SwitchCase<T> c : this.cases) {
         ItemModel$Unbaked caseModel = c.model;
         ItemModel bakedCaseModel = caseModel.bake(context, transformation);

         for (T value : c.values) {
            bakedModels.put(value, bakedCaseModel);
         }
      }

      bakedModels.defaultReturnValue(fallback);
      return new SelectItemModel<>(this.property, this.createModelGetter(bakedModels, context.contextSwapper()));
   }

   private SelectItemModel$ModelSelector<T> createModelGetter(
      final Object2ObjectMap<T, ItemModel> originalModels, @Nullable final RegistryContextSwapper registrySwapper
   ) {
      if (registrySwapper == null) {
         return (value, var2) -> (ItemModel)originalModels.get(value);
      } else {
         ItemModel defaultModel = (ItemModel)originalModels.defaultReturnValue();
         CacheSlot<ClientLevel, Object2ObjectMap<T, ItemModel>> remappedModelCache = new CacheSlot<>(
            clientLevel -> {
               Object2ObjectMap<T, ItemModel> remappedModels = new Object2ObjectOpenHashMap(originalModels.size());
               remappedModels.defaultReturnValue(defaultModel);
               originalModels.forEach(
                  (value, model) -> registrySwapper.swapTo(this.property.valueCodec(), value, clientLevel.registryAccess())
                     .ifSuccess(remappedValue -> remappedModels.put(remappedValue, model))
               );
               return remappedModels;
            }
         );
         return (value, context) -> {
            if (context == null) {
               return (ItemModel)originalModels.get(value);
            } else {
               return value == null ? defaultModel : (ItemModel)remappedModelCache.compute(context).get(value);
            }
         };
      }
   }

   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      for (SelectItemModel$SwitchCase<?> c : this.cases) {
         c.model.resolveDependencies(resolver);
      }
   }
}
