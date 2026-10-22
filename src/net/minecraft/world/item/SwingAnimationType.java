package net.minecraft.world.item;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public enum SwingAnimationType implements StringRepresentable {
   NONE(0, "none"),
   WHACK(1, "whack"),
   STAB(2, "stab");

   private static final IntFunction<SwingAnimationType> BY_ID = ByIdMap.continuous(SwingAnimationType::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StringRepresentable$EnumCodec<SwingAnimationType> CODEC = StringRepresentable.fromEnum(SwingAnimationType::values);
   public static final StreamCodec<ByteBuf, SwingAnimationType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SwingAnimationType::getId);
   private final int id;
   private final String name;

   private SwingAnimationType(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   @Contract("_,!null->!null;_,null->_")
   @Nullable
   public static SwingAnimationType byName(final String name, @Nullable final SwingAnimationType defaultAnimation) {
      SwingAnimationType result = CODEC.byName(name);
      return result != null ? result : defaultAnimation;
   }
}
