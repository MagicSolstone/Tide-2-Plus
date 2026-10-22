package net.minecraft.network.chat;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.SignatureValidator;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.entity.player.ProfilePublicKey$Data;
import net.minecraft.world.entity.player.ProfilePublicKey$ValidationException;

public record RemoteChatSession$Data(UUID sessionId, ProfilePublicKey$Data profilePublicKey) {
   public static final StreamCodec<ByteBuf, RemoteChatSession$Data> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC,
      RemoteChatSession$Data::sessionId,
      ProfilePublicKey$Data.STREAM_CODEC,
      RemoteChatSession$Data::profilePublicKey,
      RemoteChatSession$Data::new
   );

   public RemoteChatSession validate(final GameProfile profile, final SignatureValidator serviceSignatureValidator) throws ProfilePublicKey$ValidationException {
      return new RemoteChatSession(this.sessionId, ProfilePublicKey.createValidated(serviceSignatureValidator, profile.id(), this.profilePublicKey));
   }
}
