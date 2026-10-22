package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Recipe$CommonInfo(boolean showNotification) {
   public static final MapCodec<Recipe$CommonInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(Recipe$CommonInfo::showNotification)).apply(i, Recipe$CommonInfo::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, Recipe$CommonInfo> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL, Recipe$CommonInfo::showNotification, Recipe$CommonInfo::new
   );
}
