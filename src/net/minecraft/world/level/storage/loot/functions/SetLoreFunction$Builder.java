package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.LootContext$EntityTarget;

public class SetLoreFunction$Builder extends LootItemConditionalFunction$Builder<SetLoreFunction$Builder> {
   private Optional<LootContext$EntityTarget> resolutionContext = Optional.empty();
   private final Builder<Component> lore = ImmutableList.builder();
   private ListOperation mode = ListOperation$Append.INSTANCE;

   public SetLoreFunction$Builder setMode(final ListOperation mode) {
      this.mode = mode;
      return this;
   }

   public SetLoreFunction$Builder setResolutionContext(final LootContext$EntityTarget resolutionContext) {
      this.resolutionContext = Optional.of(resolutionContext);
      return this;
   }

   public SetLoreFunction$Builder addLine(final Component line) {
      this.lore.add(line);
      return this;
   }

   protected SetLoreFunction$Builder getThis() {
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new SetLoreFunction(this.getCondition(), this.lore.build(), this.mode, this.resolutionContext);
   }
}
