package net.minecraft.world.entity.ai.village.poi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record PoiSection$Packed(boolean isValid, List<PoiRecord$Packed> records) {
   public static final Codec<PoiSection$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.BOOL.lenientOptionalFieldOf("Valid", false).forGetter(PoiSection$Packed::isValid),
            PoiRecord$Packed.CODEC.listOf().fieldOf("Records").forGetter(PoiSection$Packed::records)
         )
         .apply(i, PoiSection$Packed::new)
   );

   public PoiSection unpack(final Runnable setDirty) {
      return new PoiSection(setDirty, this.isValid, this.records.stream().map(record -> record.unpack(setDirty)).toList());
   }
}
