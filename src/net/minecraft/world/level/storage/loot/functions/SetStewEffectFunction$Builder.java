package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class SetStewEffectFunction$Builder extends LootItemConditionalFunction$Builder<SetStewEffectFunction$Builder> {
   private final Builder<SetStewEffectFunction$EffectEntry> effects = ImmutableList.builder();

   protected SetStewEffectFunction$Builder getThis() {
      return this;
   }

   public SetStewEffectFunction$Builder withEffect(final Holder<MobEffect> effect, final Holder<ContextIntProvider> duration) {
      this.effects.add(new SetStewEffectFunction$EffectEntry(effect, duration));
      return this;
   }

   public SetStewEffectFunction$Builder withEffect(final Holder<MobEffect> effect, final int duration) {
      return this.withEffect(effect, ContextIntProviders.exactly(duration));
   }

   @Override
   public LootItemFunction build() {
      return new SetStewEffectFunction(this.getCondition(), this.effects.build());
   }
}
