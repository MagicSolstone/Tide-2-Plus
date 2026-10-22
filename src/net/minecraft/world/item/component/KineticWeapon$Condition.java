package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record KineticWeapon$Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
   public static final Codec<KineticWeapon$Condition> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_duration_ticks").forGetter(KineticWeapon$Condition::maxDurationTicks),
            Codec.FLOAT.optionalFieldOf("min_speed", 0.0F).forGetter(KineticWeapon$Condition::minSpeed),
            Codec.FLOAT.optionalFieldOf("min_relative_speed", 0.0F).forGetter(KineticWeapon$Condition::minRelativeSpeed)
         )
         .apply(i, KineticWeapon$Condition::new)
   );
   public static final StreamCodec<ByteBuf, KineticWeapon$Condition> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      KineticWeapon$Condition::maxDurationTicks,
      ByteBufCodecs.FLOAT,
      KineticWeapon$Condition::minSpeed,
      ByteBufCodecs.FLOAT,
      KineticWeapon$Condition::minRelativeSpeed,
      KineticWeapon$Condition::new
   );

   public boolean test(final int ticksUsed, final double attackerSpeed, final double relativeSpeed, final double entityFactor) {
      return ticksUsed <= this.maxDurationTicks && attackerSpeed >= this.minSpeed * entityFactor && relativeSpeed >= this.minRelativeSpeed * entityFactor;
   }

   public static Optional<KineticWeapon$Condition> ofAttackerSpeed(final int untilTicks, final float minAttackerSpeed) {
      return Optional.of(new KineticWeapon$Condition(untilTicks, minAttackerSpeed, 0.0F));
   }

   public static Optional<KineticWeapon$Condition> ofRelativeSpeed(final int untilTicks, final float minRelativeSpeed) {
      return Optional.of(new KineticWeapon$Condition(untilTicks, 0.0F, minRelativeSpeed));
   }
}
