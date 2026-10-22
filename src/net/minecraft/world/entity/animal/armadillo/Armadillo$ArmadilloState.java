package net.minecraft.world.entity.animal.armadillo;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum Armadillo$ArmadilloState implements StringRepresentable {
   IDLE("idle", false, 0, 0),
   ROLLING("rolling", true, 10, 1),
   SCARED("scared", true, 50, 2),
   UNROLLING("unrolling", true, 30, 3);

   private static final Codec<Armadillo$ArmadilloState> CODEC = StringRepresentable.fromEnum(Armadillo$ArmadilloState::values);
   private static final IntFunction<Armadillo$ArmadilloState> BY_ID = ByIdMap.continuous(
      Armadillo$ArmadilloState::id, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, Armadillo$ArmadilloState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Armadillo$ArmadilloState::id);
   private final String name;
   private final boolean isThreatened;
   private final int animationDuration;
   private final int id;

   private Armadillo$ArmadilloState(final String name, final boolean isThreatened, final int animationDuration, final int id) {
      this.name = name;
      this.isThreatened = isThreatened;
      this.animationDuration = animationDuration;
      this.id = id;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   private int id() {
      return this.id;
   }

   public abstract boolean shouldHideInShell(final long ticksInState);

   public boolean isThreatened() {
      return this.isThreatened;
   }

   public int animationDuration() {
      return this.animationDuration;
   }
}
