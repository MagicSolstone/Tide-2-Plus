package net.minecraft.world.entity.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Crypt;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.SignatureValidator;

public record ProfilePublicKey$Data(Instant expiresAt, PublicKey key, byte[] keySignature) {
   private static final int MAX_KEY_SIGNATURE_SIZE = 4096;
   public static final Codec<ProfilePublicKey$Data> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.INSTANT_ISO8601.fieldOf("expires_at").forGetter(ProfilePublicKey$Data::expiresAt),
            Crypt.PUBLIC_KEY_CODEC.fieldOf("key").forGetter(ProfilePublicKey$Data::key),
            ExtraCodecs.BASE64_STRING.fieldOf("signature_v2").forGetter(ProfilePublicKey$Data::keySignature)
         )
         .apply(i, ProfilePublicKey$Data::new)
   );
   public static final StreamCodec<ByteBuf, ProfilePublicKey$Data> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INSTANT,
      ProfilePublicKey$Data::expiresAt,
      ByteBufCodecs.PUBLIC_KEY,
      ProfilePublicKey$Data::key,
      ByteBufCodecs.byteArray(4096),
      ProfilePublicKey$Data::keySignature,
      ProfilePublicKey$Data::new
   );

   private boolean validateSignature(final SignatureValidator validator, final UUID profileId) {
      return validator.validate(this.signedPayload(profileId), this.keySignature);
   }

   private byte[] signedPayload(final UUID profileId) {
      byte[] keyBytes = this.key.getEncoded();
      byte[] signedPayload = new byte[24 + keyBytes.length];
      ByteBuffer buffer = ByteBuffer.wrap(signedPayload).order(ByteOrder.BIG_ENDIAN);
      buffer.putLong(profileId.getMostSignificantBits()).putLong(profileId.getLeastSignificantBits()).putLong(this.expiresAt.toEpochMilli()).put(keyBytes);
      return signedPayload;
   }

   public boolean hasExpired() {
      return this.expiresAt.isBefore(Instant.now());
   }

   public boolean hasExpired(final Duration gracePeriod) {
      return this.expiresAt.plus(gracePeriod).isBefore(Instant.now());
   }

   @Override
   public boolean equals(final Object o) {
      return !(o instanceof ProfilePublicKey$Data data)
         ? false
         : this.expiresAt.equals(data.expiresAt) && this.key.equals(data.key) && Arrays.equals(this.keySignature, data.keySignature);
   }
}
