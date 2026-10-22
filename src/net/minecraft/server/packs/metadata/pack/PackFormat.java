package net.minecraft.server.packs.metadata.pack;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult.Error;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import org.slf4j.Logger;

public record PackFormat(int major, int minor) implements Comparable<PackFormat> {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Codec<PackFormat> BOTTOM_CODEC = fullCodec(0);
   public static final Codec<PackFormat> TOP_CODEC = fullCodec(Integer.MAX_VALUE);

   private static Codec<PackFormat> fullCodec(final int defaultMinor) {
      return ExtraCodecs.compactListCodec(ExtraCodecs.NON_NEGATIVE_INT, ExtraCodecs.NON_NEGATIVE_INT.listOf(1, 256))
         .xmap(
            list -> list.size() > 1 ? of((Integer)list.getFirst(), (Integer)list.get(1)) : of((Integer)list.getFirst(), defaultMinor),
            pf -> pf.minor != defaultMinor ? List.of(pf.major(), pf.minor()) : List.of(pf.major())
         );
   }

   public static <ResultType, HolderType extends PackFormat$IntermediaryFormatHolder> DataResult<List<ResultType>> validateHolderList(
      final List<HolderType> list, final int lastPreMinorVersion, final BiFunction<HolderType, InclusiveRange<PackFormat>, ResultType> constructor
   ) {
      int minVersion = list.stream()
         .map(PackFormat$IntermediaryFormatHolder::format)
         .mapToInt(PackFormat$IntermediaryFormat::effectiveMinMajorVersion)
         .min()
         .orElse(Integer.MAX_VALUE);
      List<ResultType> result = new ArrayList<>(list.size());

      for (HolderType entry : list) {
         PackFormat$IntermediaryFormat format = entry.format();
         if (format.min().isEmpty() && format.max().isEmpty() && format.supported().isEmpty()) {
            LOGGER.warn("Unknown or broken overlay entry {}", entry);
         } else {
            DataResult<InclusiveRange<PackFormat>> entryResult = format.validate(
               lastPreMinorVersion, false, minVersion <= lastPreMinorVersion, "Overlay \"" + entry + "\"", "formats"
            );
            if (!entryResult.isSuccess()) {
               return DataResult.error(((Error)entryResult.error().get())::message);
            }

            result.add(constructor.apply(entry, (InclusiveRange<PackFormat>)entryResult.getOrThrow()));
         }
      }

      return DataResult.success(List.copyOf(result));
   }

   @VisibleForTesting
   public static int lastPreMinorVersion(final PackType type) {
      return switch (type) {
         case CLIENT_RESOURCES -> 64;
         case SERVER_DATA -> 81;
      };
   }

   public static MapCodec<InclusiveRange<PackFormat>> packCodec(final PackType type) {
      int lastPreMinorVersion = lastPreMinorVersion(type);
      return PackFormat$IntermediaryFormat.PACK_CODEC
         .flatXmap(
            intermediaryFormat -> intermediaryFormat.validate(lastPreMinorVersion, true, false, "Pack", "supported_formats"),
            range -> DataResult.success(PackFormat$IntermediaryFormat.fromRange(range, lastPreMinorVersion))
         );
   }

   public static PackFormat of(final int major, final int minor) {
      return new PackFormat(major, minor);
   }

   public static PackFormat of(final int major) {
      return new PackFormat(major, 0);
   }

   public InclusiveRange<PackFormat> minorRange() {
      return new InclusiveRange<>(this, of(this.major, Integer.MAX_VALUE));
   }

   public int compareTo(final PackFormat other) {
      int majorDiff = Integer.compare(this.major(), other.major());
      return majorDiff != 0 ? majorDiff : Integer.compare(this.minor(), other.minor());
   }

   @Override
   public String toString() {
      return this.minor == Integer.MAX_VALUE
         ? String.format(Locale.ROOT, "%d.*", this.major())
         : String.format(Locale.ROOT, "%d.%d", this.major(), this.minor());
   }
}
