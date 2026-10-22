package net.minecraft.world.level.gameevent.vibrations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.ExtraCodecs;
import org.jspecify.annotations.Nullable;

public final class VibrationSystem$Data {
   public static final Codec<VibrationSystem$Data> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(o -> Optional.ofNullable(o.currentVibration)),
            VibrationSelector.CODEC.fieldOf("selector").forGetter(VibrationSystem$Data::getSelectionStrategy),
            ExtraCodecs.optionalAlwaysPresentFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "event_delay", 0).forGetter(VibrationSystem$Data::getTravelTimeInTicks)
         )
         .apply(
            i,
            (currentVibration, selectionStrategy, travelTimeInTicks) -> new VibrationSystem$Data(
               (VibrationInfo)currentVibration.orElse(null), selectionStrategy, travelTimeInTicks, true
            )
         )
   );
   public static final String NBT_TAG_KEY = "listener";
   @Nullable
   private VibrationInfo currentVibration;
   private int travelTimeInTicks;
   private final VibrationSelector selectionStrategy;
   private boolean reloadVibrationParticle;

   private VibrationSystem$Data(
      @Nullable final VibrationInfo currentVibration,
      final VibrationSelector selectionStrategy,
      final int travelTimeInTicks,
      final boolean reloadVibrationParticle
   ) {
      this.currentVibration = currentVibration;
      this.travelTimeInTicks = travelTimeInTicks;
      this.selectionStrategy = selectionStrategy;
      this.reloadVibrationParticle = reloadVibrationParticle;
   }

   public VibrationSystem$Data() {
      this(null, new VibrationSelector(), 0, false);
   }

   public VibrationSelector getSelectionStrategy() {
      return this.selectionStrategy;
   }

   @Nullable
   public VibrationInfo getCurrentVibration() {
      return this.currentVibration;
   }

   public void setCurrentVibration(@Nullable final VibrationInfo currentVibration) {
      this.currentVibration = currentVibration;
   }

   public int getTravelTimeInTicks() {
      return this.travelTimeInTicks;
   }

   public void setTravelTimeInTicks(final int travelTimeInTicks) {
      this.travelTimeInTicks = travelTimeInTicks;
   }

   public void decrementTravelTime() {
      this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1);
   }

   public boolean shouldReloadVibrationParticle() {
      return this.reloadVibrationParticle;
   }

   public void setReloadVibrationParticle(final boolean reloadVibrationParticle) {
      this.reloadVibrationParticle = reloadVibrationParticle;
   }
}
