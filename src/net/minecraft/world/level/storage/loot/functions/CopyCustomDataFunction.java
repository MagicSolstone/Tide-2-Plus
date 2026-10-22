package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext$EntityTarget;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
import org.apache.commons.lang3.mutable.MutableObject;

public class CopyCustomDataFunction extends LootItemConditionalFunction {
   public static final MapCodec<CopyCustomDataFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               NbtProviders.CODEC.fieldOf("source").forGetter(f -> f.source),
               CopyCustomDataFunction$CopyOperation.CODEC.listOf().fieldOf("ops").forGetter(f -> f.operations)
            )
         )
         .apply(i, CopyCustomDataFunction::new)
   );
   private final NbtProvider source;
   private final List<CopyCustomDataFunction$CopyOperation> operations;

   private CopyCustomDataFunction(
      final Optional<Holder<LootItemCondition>> condition, final NbtProvider source, final List<CopyCustomDataFunction$CopyOperation> operations
   ) {
      super(condition);
      this.source = source;
      this.operations = List.copyOf(operations);
   }

   @Override
   public MapCodec<CopyCustomDataFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      Validatable.validate(context, "source", this.source);
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      Tag sourceTag = this.source.get(context);
      if (sourceTag == null) {
         return itemStack;
      } else {
         MutableObject<CompoundTag> result = new MutableObject();
         Supplier<Tag> lazyTargetCopy = () -> {
            if (result.get() == null) {
               result.setValue(itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
            }

            return (Tag)result.get();
         };
         this.operations.forEach(op -> op.apply(lazyTargetCopy, sourceTag));
         CompoundTag resultTag = (CompoundTag)result.get();
         if (resultTag != null) {
            CustomData.set(DataComponents.CUSTOM_DATA, itemStack, resultTag);
         }

         return itemStack;
      }
   }

   @Deprecated
   public static CopyCustomDataFunction$Builder copyData(final NbtProvider source) {
      return new CopyCustomDataFunction$Builder(source);
   }

   public static CopyCustomDataFunction$Builder copyData(final LootContext$EntityTarget source) {
      return new CopyCustomDataFunction$Builder(ContextNbtProvider.forContextEntity(source));
   }
}
