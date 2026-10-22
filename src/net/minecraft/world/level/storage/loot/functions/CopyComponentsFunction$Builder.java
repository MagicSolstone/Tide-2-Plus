package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.level.storage.loot.LootContextArg;

public class CopyComponentsFunction$Builder extends LootItemConditionalFunction$Builder<CopyComponentsFunction$Builder> {
   private final LootContextArg<DataComponentGetter> source;
   private Optional<Builder<DataComponentType<?>>> include = Optional.empty();
   private Optional<Builder<DataComponentType<?>>> exclude = Optional.empty();

   private CopyComponentsFunction$Builder(final LootContextArg<DataComponentGetter> source) {
      this.source = source;
   }

   public CopyComponentsFunction$Builder include(final DataComponentType<?> type) {
      if (this.include.isEmpty()) {
         this.include = Optional.of(ImmutableList.builder());
      }

      this.include.get().add(type);
      return this;
   }

   public CopyComponentsFunction$Builder exclude(final DataComponentType<?> type) {
      if (this.exclude.isEmpty()) {
         this.exclude = Optional.of(ImmutableList.builder());
      }

      this.exclude.get().add(type);
      return this;
   }

   protected CopyComponentsFunction$Builder getThis() {
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new CopyComponentsFunction(this.getCondition(), this.source, this.include.map(Builder::build), this.exclude.map(Builder::build));
   }
}
