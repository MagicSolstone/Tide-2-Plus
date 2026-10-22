package net.minecraft.world.entity.player;

import com.mojang.serialization.Codec;
import java.time.Duration;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.util.SignatureValidator;

public record ProfilePublicKey(ProfilePublicKey$Data data) {
   public static final Component EXPIRED_PROFILE_PUBLIC_KEY = Component.translatable("multiplayer.disconnect.expired_public_key");
   private static final Component INVALID_SIGNATURE = Component.translatable("multiplayer.disconnect.invalid_public_key_signature");
   public static final Duration EXPIRY_GRACE_PERIOD = Duration.ofHours(8L);
   public static final Codec<ProfilePublicKey> TRUSTED_CODEC = ProfilePublicKey$Data.CODEC.xmap(ProfilePublicKey::new, ProfilePublicKey::data);

   public static ProfilePublicKey createValidated(final SignatureValidator validator, final UUID profileId, final ProfilePublicKey$Data data) throws ProfilePublicKey$ValidationException {
      if (!data.validateSignature(validator, profileId)) {
         throw new ProfilePublicKey$ValidationException(INVALID_SIGNATURE);
      } else {
         return new ProfilePublicKey(data);
      }
   }

   public SignatureValidator createSignatureValidator() {
      return SignatureValidator.from(this.data.key, "SHA256withRSA");
   }
}
