package net.minecraft.world.entity;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.jspecify.annotations.Nullable;

public final class Leashable$LeashData {
   public static final Codec<Leashable$LeashData> CODEC = Codec.xor(UUIDUtil.CODEC.fieldOf("UUID").codec(), BlockPos.CODEC)
      .xmap(
         Leashable$LeashData::new,
         data -> {
            if (data.leashHolder instanceof LeashFenceKnotEntity leashKnot) {
               return Either.right(leashKnot.getPos());
            } else {
               return data.leashHolder != null
                  ? Either.left(data.leashHolder.getUUID())
                  : Objects.requireNonNull(data.delayedLeashInfo, "Invalid LeashData had no attachment");
            }
         }
      );
   private int delayedLeashHolderId;
   @Nullable
   public Entity leashHolder;
   @Nullable
   public Either<UUID, BlockPos> delayedLeashInfo;
   public double angularMomentum;

   private Leashable$LeashData(final Either<UUID, BlockPos> delayedLeashInfo) {
      this.delayedLeashInfo = delayedLeashInfo;
   }

   private Leashable$LeashData(final Entity entity) {
      this.leashHolder = entity;
   }

   private Leashable$LeashData(final int entityId) {
      this.delayedLeashHolderId = entityId;
   }

   public void setLeashHolder(final Entity leashHolder) {
      this.leashHolder = leashHolder;
      this.delayedLeashInfo = null;
      this.delayedLeashHolderId = 0;
   }
}
