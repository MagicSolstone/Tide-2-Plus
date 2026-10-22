package net.minecraft.stats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record RecipeBookSettings$TypeSettings(boolean open, boolean filtering) {
   public static final RecipeBookSettings$TypeSettings DEFAULT = new RecipeBookSettings$TypeSettings(false, false);
   public static final MapCodec<RecipeBookSettings$TypeSettings> CRAFTING_MAP_CODEC = codec("isGuiOpen", "isFilteringCraftable");
   public static final MapCodec<RecipeBookSettings$TypeSettings> FURNACE_MAP_CODEC = codec("isFurnaceGuiOpen", "isFurnaceFilteringCraftable");
   public static final MapCodec<RecipeBookSettings$TypeSettings> BLAST_FURNACE_MAP_CODEC = codec(
      "isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable"
   );
   public static final MapCodec<RecipeBookSettings$TypeSettings> SMOKER_MAP_CODEC = codec("isSmokerGuiOpen", "isSmokerFilteringCraftable");
   public static final StreamCodec<ByteBuf, RecipeBookSettings$TypeSettings> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL,
      RecipeBookSettings$TypeSettings::open,
      ByteBufCodecs.BOOL,
      RecipeBookSettings$TypeSettings::filtering,
      RecipeBookSettings$TypeSettings::new
   );

   @Override
   public String toString() {
      return "[open=" + this.open + ", filtering=" + this.filtering + "]";
   }

   public RecipeBookSettings$TypeSettings setOpen(final boolean open) {
      return new RecipeBookSettings$TypeSettings(open, this.filtering);
   }

   public RecipeBookSettings$TypeSettings setFiltering(final boolean filtering) {
      return new RecipeBookSettings$TypeSettings(this.open, filtering);
   }

   private static MapCodec<RecipeBookSettings$TypeSettings> codec(final String openFieldName, final String filteringFieldName) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               Codec.BOOL.optionalFieldOf(openFieldName, false).forGetter(RecipeBookSettings$TypeSettings::open),
               Codec.BOOL.optionalFieldOf(filteringFieldName, false).forGetter(RecipeBookSettings$TypeSettings::filtering)
            )
            .apply(i, RecipeBookSettings$TypeSettings::new)
      );
   }
}
