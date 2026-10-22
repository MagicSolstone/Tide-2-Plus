package net.minecraft.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.SelectItemModel$UnbakedSwitch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ComponentContents<T>(DataComponentType<T> componentType) implements SelectItemModelProperty<T> {
   private static final SelectItemModelProperty$Type<? extends ComponentContents<?>, ?> TYPE = createType();

   private static <T> SelectItemModelProperty$Type<ComponentContents<T>, T> createType() {
      Codec<? extends DataComponentType<?>> rawComponentCodec = BuiltInRegistries.DATA_COMPONENT_TYPE
         .byNameCodec()
         .validate(t -> t.isTransient() ? DataResult.error(() -> "Component can't be serialized") : DataResult.success(t));
      MapCodec<SelectItemModel$UnbakedSwitch<ComponentContents<T>, T>> switchCodec = rawComponentCodec.dispatchMap(
         "component",
         switchObject -> ((ComponentContents)switchObject.property()).componentType,
         componentType -> SelectItemModelProperty$Type.createCasesFieldCodec(componentType.codecOrThrow())
            .xmap(cases -> new SelectItemModel$UnbakedSwitch<>(new ComponentContents(componentType), cases), SelectItemModel$UnbakedSwitch::cases)
      );
      return new SelectItemModelProperty$Type<>(switchCodec);
   }

   public static <T> SelectItemModelProperty$Type<ComponentContents<T>, T> castType() {
      return (SelectItemModelProperty$Type<ComponentContents<T>, T>)TYPE;
   }

   @Nullable
   @Override
   public T get(
      final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext displayContext
   ) {
      return itemStack.get(this.componentType);
   }

   @Override
   public SelectItemModelProperty$Type<ComponentContents<T>, T> type() {
      return castType();
   }

   @Override
   public Codec<T> valueCodec() {
      return this.componentType.codecOrThrow();
   }
}
