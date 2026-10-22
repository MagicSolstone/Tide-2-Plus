package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers$Builder;

public class SetBannerPatternFunction$Builder extends LootItemConditionalFunction$Builder<SetBannerPatternFunction$Builder> {
   private final BannerPatternLayers$Builder patterns = new BannerPatternLayers$Builder();
   private final boolean append;

   private SetBannerPatternFunction$Builder(final boolean append) {
      this.append = append;
   }

   protected SetBannerPatternFunction$Builder getThis() {
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new SetBannerPatternFunction(this.getCondition(), this.patterns.build(), this.append);
   }

   public SetBannerPatternFunction$Builder addPattern(final Holder<BannerPattern> pattern, final DyeColor color) {
      this.patterns.add(pattern, color);
      return this;
   }
}
