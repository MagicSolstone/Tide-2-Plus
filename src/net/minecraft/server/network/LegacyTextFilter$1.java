package net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import java.util.Objects;

class LegacyTextFilter$1 extends ServerTextFilter$PlayerContext {
   LegacyTextFilter$1(final LegacyTextFilter this$0, final GameProfile profile) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0, profile);
   }

   @Override
   public void join() {
      this.this$0.processJoinOrLeave(this.profile, this.this$0.joinEndpoint, this.this$0.joinEncoder, this.streamExecutor);
   }

   @Override
   public void leave() {
      this.this$0.processJoinOrLeave(this.profile, this.this$0.leaveEndpoint, this.this$0.leaveEncoder, this.streamExecutor);
   }
}
