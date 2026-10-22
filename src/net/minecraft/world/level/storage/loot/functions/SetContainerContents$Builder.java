package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer$Builder;

public class SetContainerContents$Builder extends LootItemConditionalFunction$Builder<SetContainerContents$Builder> {
   private final Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
   private final ContainerComponentManipulator<?> component;

   public SetContainerContents$Builder(final ContainerComponentManipulator<?> component) {
      this.component = component;
   }

   protected SetContainerContents$Builder getThis() {
      return this;
   }

   public SetContainerContents$Builder withEntry(final LootPoolEntryContainer$Builder<?> entry) {
      this.entries.add(entry.build());
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new SetContainerContents(this.getCondition(), this.component, this.entries.build());
   }
}
