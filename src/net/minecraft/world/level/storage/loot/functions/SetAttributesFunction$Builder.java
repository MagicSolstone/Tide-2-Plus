package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.Lists;
import java.util.List;

public class SetAttributesFunction$Builder extends LootItemConditionalFunction$Builder<SetAttributesFunction$Builder> {
   private final boolean replace;
   private final List<SetAttributesFunction$Modifier> modifiers = Lists.newArrayList();

   public SetAttributesFunction$Builder(final boolean replace) {
      this.replace = replace;
   }

   public SetAttributesFunction$Builder() {
      this(false);
   }

   protected SetAttributesFunction$Builder getThis() {
      return this;
   }

   public SetAttributesFunction$Builder withModifier(final SetAttributesFunction$ModifierBuilder modifier) {
      this.modifiers.add(modifier.build());
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new SetAttributesFunction(this.getCondition(), this.modifiers, this.replace);
   }
}
