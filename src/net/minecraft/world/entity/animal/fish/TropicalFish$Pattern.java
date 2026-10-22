package net.minecraft.world.entity.animal.fish;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item$TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public enum TropicalFish$Pattern implements StringRepresentable, TooltipProvider {
   KOB("kob", TropicalFish$Base.SMALL, 0),
   SUNSTREAK("sunstreak", TropicalFish$Base.SMALL, 1),
   SNOOPER("snooper", TropicalFish$Base.SMALL, 2),
   DASHER("dasher", TropicalFish$Base.SMALL, 3),
   BRINELY("brinely", TropicalFish$Base.SMALL, 4),
   SPOTTY("spotty", TropicalFish$Base.SMALL, 5),
   FLOPPER("flopper", TropicalFish$Base.LARGE, 0),
   STRIPEY("stripey", TropicalFish$Base.LARGE, 1),
   GLITTER("glitter", TropicalFish$Base.LARGE, 2),
   BLOCKFISH("blockfish", TropicalFish$Base.LARGE, 3),
   BETTY("betty", TropicalFish$Base.LARGE, 4),
   CLAYFISH("clayfish", TropicalFish$Base.LARGE, 5);

   public static final Codec<TropicalFish$Pattern> CODEC = StringRepresentable.fromEnum(TropicalFish$Pattern::values);
   private static final IntFunction<TropicalFish$Pattern> BY_ID = ByIdMap.sparse(TropicalFish$Pattern::getPackedId, values(), KOB);
   public static final StreamCodec<ByteBuf, TropicalFish$Pattern> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TropicalFish$Pattern::getPackedId);
   private final String name;
   private final Component displayName;
   private final TropicalFish$Base base;
   private final int packedId;

   private TropicalFish$Pattern(final String name, final TropicalFish$Base base, final int index) {
      this.name = name;
      this.base = base;
      this.packedId = base.id | index << 8;
      this.displayName = Component.translatable("entity.minecraft.tropical_fish.type." + this.name);
   }

   public static TropicalFish$Pattern byId(final int packedId) {
      return BY_ID.apply(packedId);
   }

   public TropicalFish$Base base() {
      return this.base;
   }

   public int getPackedId() {
      return this.packedId;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public Component displayName() {
      return this.displayName;
   }

   @Override
   public void addToTooltip(final Item$TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
      DyeColor baseColor = components.getOrDefault(DataComponents.TROPICAL_FISH_BASE_COLOR, TropicalFish.DEFAULT_VARIANT.baseColor());
      DyeColor patternColor = components.getOrDefault(DataComponents.TROPICAL_FISH_PATTERN_COLOR, TropicalFish.DEFAULT_VARIANT.patternColor());
      ChatFormatting[] styles = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
      int commonIndex = TropicalFish.COMMON_VARIANTS.indexOf(new TropicalFish$Variant(this, baseColor, patternColor));
      if (commonIndex != -1) {
         consumer.accept(Component.translatable(TropicalFish.getPredefinedName(commonIndex)).withStyle(styles));
      } else {
         consumer.accept(this.displayName.plainCopy().withStyle(styles));
         MutableComponent colorComponent = Component.translatable("color.minecraft." + baseColor.getName());
         if (baseColor != patternColor) {
            colorComponent.append(", ").append(Component.translatable("color.minecraft." + patternColor.getName()));
         }

         colorComponent.withStyle(styles);
         consumer.accept(colorComponent);
      }
   }
}
