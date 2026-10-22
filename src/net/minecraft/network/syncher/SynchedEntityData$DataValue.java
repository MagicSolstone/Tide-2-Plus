package net.minecraft.network.syncher;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.RegistryFriendlyByteBuf;

public record SynchedEntityData$DataValue<T>(int id, EntityDataSerializer<T> serializer, T value) {
   public static <T> SynchedEntityData$DataValue<T> create(final EntityDataAccessor<T> accessor, final T value) {
      EntityDataSerializer<T> serializer = accessor.serializer();
      return new SynchedEntityData$DataValue<>(accessor.id(), serializer, serializer.copy(value));
   }

   public void write(final RegistryFriendlyByteBuf output) {
      int serializerId = EntityDataSerializers.getSerializedId(this.serializer);
      if (serializerId < 0) {
         throw new EncoderException("Unknown serializer type " + this.serializer);
      } else {
         output.writeByte(this.id);
         output.writeVarInt(serializerId);
         this.serializer.codec().encode(output, this.value);
      }
   }

   public static SynchedEntityData$DataValue<?> read(final RegistryFriendlyByteBuf input, final int id) {
      int type = input.readVarInt();
      EntityDataSerializer<?> serializer = EntityDataSerializers.getSerializer(type);
      if (serializer == null) {
         throw new DecoderException("Unknown serializer type " + type);
      } else {
         return read(input, id, serializer);
      }
   }

   private static <T> SynchedEntityData$DataValue<T> read(final RegistryFriendlyByteBuf input, final int id, final EntityDataSerializer<T> serializer) {
      return new SynchedEntityData$DataValue<>(id, serializer, serializer.codec().decode(input));
   }
}
