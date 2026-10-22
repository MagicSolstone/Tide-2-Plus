package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

record SetStewEffectFunction$EffectEntry(Holder<MobEffect> effect, Holder<ContextIntProvider> duration) implements LootContextUser {
   public static final Codec<SetStewEffectFunction$EffectEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MobEffect.CODEC.fieldOf("type").forGetter(SetStewEffectFunction$EffectEntry::effect),
            ContextIntProviders.CODEC.fieldOf("duration").forGetter(SetStewEffectFunction$EffectEntry::duration)
         )
         .apply(i, SetStewEffectFunction$EffectEntry::new)
   );

   @Override
   public void validate(final ValidationContext context) {
      LootContextUser.super.validate(context);
      Validatable.validateHolder(context, "duration", this.duration);
   }
}
