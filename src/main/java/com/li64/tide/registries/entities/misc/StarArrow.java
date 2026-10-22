package com.li64.tide.registries.entities.misc;

import com.li64.tide.registries.TideEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class StarArrow extends AbstractArrow {
    public int lifetime = 50;

    public StarArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("unused")
    public StarArrow(Level level, LivingEntity entity, ItemStack pickupStack, ItemStack firedFromWeapon) {
        super(TideEntityTypes.STAR_ARROW, entity, level, pickupStack, firedFromWeapon);
    }

    @Override
    public void tick() {
        super.tick();
        if (!onGround()) {
            Vec3 vel = this.getDeltaMovement();
            double vx = vel.x;
            double vy = vel.y;
            double vz = vel.z;
            if (random.nextBoolean()) {
                this.level().addParticle(ParticleTypes.END_ROD,
                        this.getX(), this.getY(), this.getZ(),
                        -vx * 0.03, -vy * 0.01, -vz * 0.03);
            }
            if (!level().isClientSide()) {
                lifetime--;
                if (lifetime <= 0) {
                    this.spawnBreakParticles(blockPosition());
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        HitResult.Type hitType = hitResult.getType();
        if (hitType == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)hitResult);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.getLocation(), GameEvent.Context.of(this, null));
            this.spawnBreakParticles(blockPosition());
            this.discard();
        } else if (hitType == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)hitResult;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.spawnBreakParticles(blockpos);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
            this.discard();
        }
    }

    private void spawnBreakParticles(BlockPos pos) {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        serverLevel.sendParticles(ParticleTypes.END_ROD,
                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                12, 0.1, 0.1, 0.1, 0.04);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }

    @Override
    public @NotNull Vec3 getDeltaMovement() {
        if (isCritArrow()) return super.getDeltaMovement().normalize().scale(2.5f);
        else return super.getDeltaMovement().normalize().scale(1.8f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void setNoGravity(boolean value) {
        super.setNoGravity(false);
    }

    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

}