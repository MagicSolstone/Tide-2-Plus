package net.minecraft.client.sounds;

import com.mojang.blaze3d.audio.Channel;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChannelAccess$ChannelHandle {
   @Nullable
   private Channel channel;
   private boolean stopped;

   public boolean isStopped() {
      return this.stopped;
   }

   public ChannelAccess$ChannelHandle(final ChannelAccess this$0, final Channel channel) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.channel = channel;
   }

   public void execute(final Consumer<Channel> action) {
      this.this$0.executor.execute(() -> {
         if (this.channel != null) {
            action.accept(this.channel);
         }
      });
   }

   public void release() {
      this.stopped = true;
      this.this$0.library.releaseChannel(this.channel);
      this.channel = null;
   }
}
