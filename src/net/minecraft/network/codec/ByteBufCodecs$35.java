package net.minecraft.network.codec;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.Utf8String;

class ByteBufCodecs$35 implements StreamCodec<ByteBuf, PropertyMap> {
   public PropertyMap decode(final ByteBuf input) {
      int propertyCount = ByteBufCodecs.readCount(input, 16);
      Builder<String, Property> result = ImmutableMultimap.builder();

      for (int i = 0; i < propertyCount; i++) {
         String name = Utf8String.read(input, 64);
         String value = Utf8String.read(input, 32767);
         String signature = FriendlyByteBuf.readNullable(input, in -> Utf8String.read(in, 1024));
         Property property = new Property(name, value, signature);
         result.put(property.name(), property);
      }

      return new PropertyMap(result.build());
   }

   public void encode(final ByteBuf output, final PropertyMap properties) {
      ByteBufCodecs.writeCount(output, properties.size(), 16);

      for (Property property : properties.values()) {
         Utf8String.write(output, property.name(), 64);
         Utf8String.write(output, property.value(), 32767);
         FriendlyByteBuf.writeNullable(output, property.signature(), (out, signature) -> Utf8String.write(out, signature, 1024));
      }
   }
}
