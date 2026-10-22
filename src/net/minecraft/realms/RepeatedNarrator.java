package net.minecraft.realms;

import com.google.common.util.concurrent.RateLimiter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.GameNarrator;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class RepeatedNarrator {
   private final float permitsPerSecond;
   private final AtomicReference<RepeatedNarrator$Params> params = new AtomicReference<>();

   public RepeatedNarrator(final Duration repeatDelay) {
      this.permitsPerSecond = 1000.0F / (float)repeatDelay.toMillis();
   }

   public void narrate(final GameNarrator narrator, final Component narration) {
      RepeatedNarrator$Params params = this.params
         .updateAndGet(
            existing -> existing != null && narration.equals(existing.narration)
               ? existing
               : new RepeatedNarrator$Params(narration, RateLimiter.create(this.permitsPerSecond))
         );
      if (params.rateLimiter.tryAcquire(1)) {
         narrator.saySystemNow(narration);
      }
   }
}
