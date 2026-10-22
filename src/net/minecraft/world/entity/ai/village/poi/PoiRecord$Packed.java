package net.minecraft.world.entity.ai.village.poi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;

public record PoiRecord$Packed(BlockPos pos, Holder<PoiType> poiType, int freeTickets) {
   public static final Codec<PoiRecord$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(PoiRecord$Packed::pos),
            RegistryCodecs.holder(Registries.POINT_OF_INTEREST_TYPE).fieldOf("type").forGetter(PoiRecord$Packed::poiType),
            Codec.INT.optionalFieldOf("free_tickets", 0).forGetter(PoiRecord$Packed::freeTickets)
         )
         .apply(i, PoiRecord$Packed::new)
   );

   public PoiRecord unpack(final Runnable setDirty) {
      return new PoiRecord(this.pos, this.poiType, this.freeTickets, setDirty);
   }
}
