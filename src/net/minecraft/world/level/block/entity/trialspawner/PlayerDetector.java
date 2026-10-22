package net.minecraft.world.level.block.entity.trialspawner;

import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext$Block;
import net.minecraft.world.level.ClipContext$Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult$Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public interface PlayerDetector {
   PlayerDetector NO_CREATIVE_PLAYERS = (level, selector, pos, requiredPlayerRange, requireLineOfSight) -> selector.getPlayers(
         level, p -> p.blockPosition().closerThan(pos, requiredPlayerRange) && !p.isCreative() && !p.isSpectator()
      )
      .stream()
      .filter(player -> !requireLineOfSight || inLineOfSight(level, Vec3.atCenterOf(pos), player.getEyePosition()))
      .map(Entity::getUUID)
      .toList();
   PlayerDetector INCLUDING_CREATIVE_PLAYERS = (level, selector, pos, requiredPlayerRange, requireLineOfSight) -> selector.getPlayers(
         level, p -> p.blockPosition().closerThan(pos, requiredPlayerRange) && !p.isSpectator()
      )
      .stream()
      .filter(player -> !requireLineOfSight || inLineOfSight(level, Vec3.atCenterOf(pos), player.getEyePosition()))
      .map(Entity::getUUID)
      .toList();
   PlayerDetector SHEEP = (level, selector, pos, requiredPlayerRange, requireLineOfSight) -> {
      AABB area = new AABB(pos).inflate(requiredPlayerRange);
      return selector.getEntities(level, EntityTypes.SHEEP, area, LivingEntity::isAlive)
         .stream()
         .filter(entity -> !requireLineOfSight || inLineOfSight(level, Vec3.atCenterOf(pos), entity.getEyePosition()))
         .map(Entity::getUUID)
         .toList();
   };

   List<UUID> detect(
      final ServerLevel level,
      final PlayerDetector$EntitySelector selector,
      final BlockPos spawnerPos,
      final double requiredPlayerRange,
      final boolean requireLineOfSight
   );

   private static boolean inLineOfSight(final Level level, final Vec3 origin, final Vec3 dest) {
      BlockHitResult hitResult = level.clip(new ClipContext(dest, origin, ClipContext$Block.VISUAL, ClipContext$Fluid.NONE, CollisionContext.empty()));
      return hitResult.getBlockPos().equals(BlockPos.containing(origin)) || hitResult.getType() == HitResult$Type.MISS;
   }
}
