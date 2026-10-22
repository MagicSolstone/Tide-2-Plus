package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects$Entry;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetStewEffectFunction extends LootItemConditionalFunction {
   private static final Codec<List<SetStewEffectFunction$EffectEntry>> EFFECTS_LIST = SetStewEffectFunction$EffectEntry.CODEC.listOf().validate(entries -> {
      Set<Holder<MobEffect>> seenEffects = new ObjectOpenHashSet();

      for (SetStewEffectFunction$EffectEntry entry : entries) {
         if (!seenEffects.add(entry.effect())) {
            return DataResult.error(() -> "Encountered duplicate mob effect: '" + entry.effect() + "'");
         }
      }

      return DataResult.success(entries);
   });
   public static final MapCodec<SetStewEffectFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i).and(EFFECTS_LIST.optionalFieldOf("effects", List.of()).forGetter(f -> f.effects)).apply(i, SetStewEffectFunction::new)
   );
   private final List<SetStewEffectFunction$EffectEntry> effects;

   private SetStewEffectFunction(final Optional<Holder<LootItemCondition>> condition, final List<SetStewEffectFunction$EffectEntry> effects) {
      super(condition);
      this.effects = effects;
   }

   @Override
   public MapCodec<SetStewEffectFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      Validatable.validate(context, "effects", this.effects);
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      if (itemStack.is(Items.SUSPICIOUS_STEW) && !this.effects.isEmpty()) {
         SetStewEffectFunction$EffectEntry entry = Util.getRandom(this.effects, context.getRandom());
         Holder<MobEffect> effect = entry.effect();
         int duration = entry.duration().value().getInt(context);
         if (!effect.value().isInstantaneous()) {
            duration *= 20;
         }

         SuspiciousStewEffects$Entry newEntry = new SuspiciousStewEffects$Entry(effect, duration);
         itemStack.update(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY, newEntry, SuspiciousStewEffects::withEffectAdded);
         return itemStack;
      } else {
         return itemStack;
      }
   }

   public static SetStewEffectFunction$Builder stewEffect() {
      return new SetStewEffectFunction$Builder();
   }
}
