package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public class Style$Serializer {
   public static final MapCodec<Style> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            TextColor.CODEC.optionalFieldOf("color").forGetter(o -> Optional.ofNullable(o.color)),
            ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("shadow_color").forGetter(o -> Optional.ofNullable(o.shadowColor)),
            Codec.BOOL.optionalFieldOf("bold").forGetter(o -> Optional.ofNullable(o.bold)),
            Codec.BOOL.optionalFieldOf("italic").forGetter(o -> Optional.ofNullable(o.italic)),
            Codec.BOOL.optionalFieldOf("underlined").forGetter(o -> Optional.ofNullable(o.underlined)),
            Codec.BOOL.optionalFieldOf("strikethrough").forGetter(o -> Optional.ofNullable(o.strikethrough)),
            Codec.BOOL.optionalFieldOf("obfuscated").forGetter(o -> Optional.ofNullable(o.obfuscated)),
            ClickEvent.CODEC.optionalFieldOf("click_event").forGetter(o -> Optional.ofNullable(o.clickEvent)),
            HoverEvent.CODEC.optionalFieldOf("hover_event").forGetter(o -> Optional.ofNullable(o.hoverEvent)),
            Codec.STRING.optionalFieldOf("insertion").forGetter(o -> Optional.ofNullable(o.insertion)),
            FontDescription.CODEC.optionalFieldOf("font").forGetter(o -> Optional.ofNullable(o.font))
         )
         .apply(i, Style::create)
   );
   public static final Codec<Style> CODEC = MAP_CODEC.codec();
   public static final StreamCodec<RegistryFriendlyByteBuf, Style> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);
}
