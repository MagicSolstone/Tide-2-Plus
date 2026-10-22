package net.minecraft.world.timeline;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

record Timeline$TimeMarkerInfo(int ticks, boolean showInCommands) {
   private static final Codec<Timeline$TimeMarkerInfo> FULL_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks").forGetter(Timeline$TimeMarkerInfo::ticks),
            Codec.BOOL.optionalFieldOf("show_in_commands", false).forGetter(Timeline$TimeMarkerInfo::showInCommands)
         )
         .apply(i, Timeline$TimeMarkerInfo::new)
   );
   public static final Codec<Timeline$TimeMarkerInfo> CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, FULL_CODEC)
      .xmap(
         either -> (Timeline$TimeMarkerInfo)either.map(t -> new Timeline$TimeMarkerInfo(t, false), t -> t),
         timeMarker -> timeMarker.showInCommands ? Either.right(timeMarker) : Either.left(timeMarker.ticks)
      );
}
