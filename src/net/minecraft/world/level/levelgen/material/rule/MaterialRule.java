package net.minecraft.world.level.levelgen.material.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Direct;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public interface MaterialRule {
   Codec<MaterialRule> DIRECT_CODEC = BuiltInRegistries.MATERIAL_RULE_TYPE.byNameCodec().dispatch(MaterialRule::codec, Function.identity());
   Codec<Holder<MaterialRule>> HOLDER_CODEC = RegistryCodecs.holder(Registries.MATERIAL_RULE, DIRECT_CODEC);
   Codec<MaterialRule> CODEC = HOLDER_CODEC.xmap(holder -> {
      return (MaterialRule)(switch (holder) {
         case Holder$Direct<MaterialRule> direct -> (MaterialRule)direct.value();
         case Holder$Reference<MaterialRule> reference -> new MaterialRule$HolderHolder(reference);
         default -> throw new MatchException(null, null);
      });
   }, value -> {
      return switch (value) {
         case MaterialRule$HolderHolder(Holder patt3$temp) -> patt3$temp;
         default -> Holder.direct(value);
      };
   });

   RuleEvaluator compile(MaterialRuleContext context);

   MapCodec<? extends MaterialRule> codec();
}
