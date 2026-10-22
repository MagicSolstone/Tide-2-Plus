package net.minecraft.world.level.saveddata.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.jspecify.annotations.Nullable;

public record MapBanner(BlockPos pos, DyeColor color, Optional<Component> name) {
   public static final Codec<MapBanner> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(MapBanner::pos),
            DyeColor.CODEC.lenientOptionalFieldOf("color", DyeColor.WHITE).forGetter(MapBanner::color),
            ComponentSerialization.CODEC.lenientOptionalFieldOf("name").forGetter(MapBanner::name)
         )
         .apply(i, MapBanner::new)
   );

   @Nullable
   public static MapBanner fromWorld(final BlockGetter level, final BlockPos pos) {
      if (level.getBlockEntity(pos) instanceof BannerBlockEntity banner) {
         DyeColor color = banner.getBaseColor();
         Optional<Component> name = Optional.ofNullable(banner.getCustomName());
         return new MapBanner(pos, color, name);
      } else {
         return null;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public Holder<MapDecorationType> getDecoration() {
      return switch (MapBanner$1.$SwitchMap$net$minecraft$world$item$DyeColor[this.color.ordinal()]) {
         case 1 -> MapDecorationTypes.WHITE_BANNER;
         case 2 -> MapDecorationTypes.ORANGE_BANNER;
         case 3 -> MapDecorationTypes.MAGENTA_BANNER;
         case 4 -> MapDecorationTypes.LIGHT_BLUE_BANNER;
         case 5 -> MapDecorationTypes.YELLOW_BANNER;
         case 6 -> MapDecorationTypes.LIME_BANNER;
         case 7 -> MapDecorationTypes.PINK_BANNER;
         case 8 -> MapDecorationTypes.GRAY_BANNER;
         case 9 -> MapDecorationTypes.LIGHT_GRAY_BANNER;
         case 10 -> MapDecorationTypes.CYAN_BANNER;
         case 11 -> MapDecorationTypes.PURPLE_BANNER;
         case 12 -> MapDecorationTypes.BLUE_BANNER;
         case 13 -> MapDecorationTypes.BROWN_BANNER;
         case 14 -> MapDecorationTypes.GREEN_BANNER;
         case 15 -> MapDecorationTypes.RED_BANNER;
         case 16 -> MapDecorationTypes.BLACK_BANNER;
         default -> throw new MatchException(null, null);
      };
   }

   public String getId() {
      return "banner-" + this.pos.getX() + "," + this.pos.getY() + "," + this.pos.getZ();
   }
}
