package net.minecraft.world.item.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;

class TypedEntityData$1 implements Codec<TypedEntityData<T>> {
   TypedEntityData$1(final Codec val$typeCodec) {
      this.val$typeCodec = val$typeCodecx;
   }

   public <V> DataResult<Pair<TypedEntityData<T>, V>> decode(final DynamicOps<V> ops, final V input) {
      return CustomData.COMPOUND_TAG_CODEC
         .decode(ops, input)
         .flatMap(
            pair -> {
               CompoundTag tagWithoutType = ((CompoundTag)pair.getFirst()).copy();
               Tag typeTag = tagWithoutType.remove("id");
               return typeTag == null
                  ? DataResult.error(() -> "Expected 'id' field in " + input)
                  : typeCodec.parse(asNbtOps(ops), typeTag).map(type -> Pair.of(new TypedEntityData<>(type, tagWithoutType), pair.getSecond()));
            }
         );
   }

   public <V> DataResult<V> encode(final TypedEntityData<T> input, final DynamicOps<V> ops, final V prefix) {
      return this.val$typeCodec.encodeStart(asNbtOps(ops), input.type).flatMap(typeTag -> {
         CompoundTag tag = input.tag.copy();
         tag.put("id", typeTag);
         return CustomData.COMPOUND_TAG_CODEC.encode(tag, ops, prefix);
      });
   }

   private static <T> DynamicOps<Tag> asNbtOps(final DynamicOps<T> ops) {
      return (DynamicOps<Tag>)(ops instanceof RegistryOps<T> registryOps ? registryOps.withParent(NbtOps.INSTANCE) : NbtOps.INSTANCE);
   }
}
