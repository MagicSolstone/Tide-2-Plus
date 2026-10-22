package net.minecraft.advancements;

import io.netty.buffer.ByteBuf;
import java.time.Instant;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

public class CriterionProgress {
   public static final StreamCodec<ByteBuf, CriterionProgress> STREAM_CODEC = ByteBufCodecs.INSTANT
      .apply(ByteBufCodecs::optional)
      .map(obtained -> new CriterionProgress((Instant)obtained.orElse(null)), progress -> Optional.ofNullable(progress.obtained));
   @Nullable
   private Instant obtained;

   public CriterionProgress() {
   }

   public CriterionProgress(@Nullable final Instant obtained) {
      this.obtained = obtained;
   }

   public boolean isDone() {
      return this.obtained != null;
   }

   public void grant() {
      this.obtained = Instant.now();
   }

   public void revoke() {
      this.obtained = null;
   }

   @Nullable
   public Instant getObtained() {
      return this.obtained;
   }

   @Override
   public String toString() {
      return "CriterionProgress{obtained=" + (this.obtained == null ? "false" : this.obtained) + "}";
   }
}
