package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatternLayers$Builder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetBannerPatternFunction extends LootItemConditionalFunction {
   public static final MapCodec<SetBannerPatternFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(i.group(BannerPatternLayers.CODEC.fieldOf("patterns").forGetter(f -> f.patterns), Codec.BOOL.fieldOf("append").forGetter(f -> f.append)))
         .apply(i, SetBannerPatternFunction::new)
   );
   private final BannerPatternLayers patterns;
   private final boolean append;

   private SetBannerPatternFunction(final Optional<Holder<LootItemCondition>> condition, final BannerPatternLayers patterns, final boolean append) {
      super(condition);
      this.patterns = patterns;
      this.append = append;
   }

   @Override
   protected ItemStack run(final ItemStack itemStack, final LootContext context) {
      if (this.append) {
         itemStack.update(
            DataComponents.BANNER_PATTERNS,
            BannerPatternLayers.EMPTY,
            this.patterns,
            (base, appended) -> new BannerPatternLayers$Builder().addAll(base).addAll(appended).build()
         );
      } else {
         itemStack.set(DataComponents.BANNER_PATTERNS, this.patterns);
      }

      return itemStack;
   }

   @Override
   public MapCodec<SetBannerPatternFunction> codec() {
      return MAP_CODEC;
   }

   public static SetBannerPatternFunction$Builder setBannerPattern(final boolean append) {
      return new SetBannerPatternFunction$Builder(append);
   }
}
