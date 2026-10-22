package net.minecraft.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ContextEntityType() implements SelectItemModelProperty<ResourceKey<EntityType<?>>> {
   public static final Codec<ResourceKey<EntityType<?>>> VALUE_CODEC = ResourceKey.codec(Registries.ENTITY_TYPE);
   public static final SelectItemModelProperty$Type<ContextEntityType, ResourceKey<EntityType<?>>> TYPE = SelectItemModelProperty$Type.create(
      MapCodec.unit(new ContextEntityType()), VALUE_CODEC
   );

   @Nullable
   public ResourceKey<EntityType<?>> get(
      final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext displayContext
   ) {
      return owner == null ? null : owner.typeHolder().unwrapKey().orElse(null);
   }

   @Override
   public SelectItemModelProperty$Type<ContextEntityType, ResourceKey<EntityType<?>>> type() {
      return TYPE;
   }

   @Override
   public Codec<ResourceKey<EntityType<?>>> valueCodec() {
      return VALUE_CODEC;
   }
}
