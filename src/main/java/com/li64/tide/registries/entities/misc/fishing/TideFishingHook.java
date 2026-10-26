package com.li64.tide.registries.entities.misc.fishing;

import com.li64.tide.data.player.TidePlayerData;

import com.google.common.collect.ImmutableList;
import com.li64.tide.Tide;
import com.li64.tide.compat.CompatHelper;
import com.li64.tide.compat.seasons.SeasonsCompat;
import com.li64.tide.data.TideCriteriaTriggers;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.fishing.CatchResult;
import com.li64.tide.data.fishing.CrateData;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.mediums.FishingMedium;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.loot.LootTableRef;
import com.li64.tide.data.rods.BaitContents;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.network.messages.EchoRodHookedMsg;
import com.li64.tide.registries.TideItems;
import com.li64.tide.registries.entities.misc.LootCrateEntity;
import com.li64.tide.registries.items.TideFishingRodItem;
import com.li64.tide.util.BaitUtils;
import com.li64.tide.util.TideUtils;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.*;

public class TideFishingHook extends Projectile {
    private final RandomSource syncedRandom = RandomSource.create();
    private boolean biting;
    private int outOfWaterTime;
    private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(TideFishingHook.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(TideFishingHook.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DATA_ROD_ITEM = SynchedEntityData.defineId(TideFishingHook.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> DATA_CATCH_TYPE = SynchedEntityData.defineId(TideFishingHook.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_MINIGAME_ACTIVE = SynchedEntityData.defineId(TideFishingHook.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_INITIAL_YAW = SynchedEntityData.defineId(TideFishingHook.class, EntityDataSerializers.FLOAT);
    private int life;
    private int nibble;
    private int timeUntilLured;
    private int timeUntilHooked;
    private float fishAngle;
    private boolean openWater = true;
    private Entity hookedIn;
    private FishHookState currentState = FishHookState.FLYING;
    private FishingMedium medium;
    private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
    private final int luck;
    private final int lureSpeed;
    private boolean minigameActive = false;
    protected ItemStack rod;
    protected List<ItemStack> hookedItems;
    protected CatchType catchType = CatchType.NOTHING;
    private CrateData crateData;
    private int particleTimer = 0;
    private boolean wasPerfectCatch;
    private long minigameStartTime;

    private TideFishingHook(EntityType<? extends TideFishingHook> entityType, Level level, int luck, int lureSpeed, ItemStack rod) {
        super(entityType, level);
        this.rod = rod;
        this.entityData.set(DATA_ROD_ITEM, rod);
        this.luck = luck;
        this.lureSpeed = lureSpeed;
    }

    public TideFishingHook(EntityType<? extends TideFishingHook> entityType, Level level) {
        this(entityType, level, 0, 0, Items.FISHING_ROD.getDefaultInstance());
    }

    public TideFishingHook(EntityType<? extends TideFishingHook> hookType, Player player, Level level,
                           int luck, int lureSpeed, float charge, ItemStack rod) {
        this(hookType, level, luck, lureSpeed, rod);
        this.setOwner(player);

        float f = player.getXRot();
        float f1 = player.getYRot();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));

        double d0 = player.getX() - (double)f3 * 0.3D;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double)f2 * 0.3D;
        this.snapTo(d0, d1, d2, f1, f);

        Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -2.5f, 1.75f), -f2);
        double d3 = vec3.length();
        vec3 = vec3.multiply(charge, charge, charge); // Slow the speed if not fully charged
        vec3 = vec3.multiply(0.6D / d3 + this.random.triangle(0.5D, 0.0103365D), 0.6D / d3 + this.random.triangle(0.5D, 0.0103365D), 0.6D / d3 + this.random.triangle(0.5D, 0.0103365D));
        this.setDeltaMovement(vec3);

        this.setYRot(f1);
        this.setXRot(0);
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        this.entityData.set(DATA_INITIAL_YAW, f1);
        this.entityData.set(DATA_ROD_ITEM, rod);
    }

    public void invalidateCatch() {
        hookedItems = null;
        catchType = CatchType.NOTHING;
        this.getEntityData().set(DATA_CATCH_TYPE, catchType.ordinal());
    }

    @SuppressWarnings("unused")
    public int getLureSpeed() {
        return lureSpeed;
    }

    public int getLuck() {
        return luck;
    }

    public float getInitialYaw() {
        return this.entityData.get(DATA_INITIAL_YAW);
    }

    public Holder<Biome> getBiome() {
        return level().getBiome(blockPosition());
    }

    public FluidState getFluidState() {
        return fluidState;
    }

    public boolean isOpenWaterFishing() {
        return this.openWater;
    }

    public boolean isFishHooked() {
        return this.biting;
    }

    public enum CatchType {
        FISH, CRATE,
        ITEM, NOTHING
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HOOKED_ENTITY, 0);
        builder.define(DATA_BITING, false);
        builder.define(DATA_ROD_ITEM, Items.FISHING_ROD.getDefaultInstance());
        builder.define(DATA_CATCH_TYPE, CatchType.NOTHING.ordinal());
        builder.define(DATA_MINIGAME_ACTIVE, false);
        builder.define(DATA_INITIAL_YAW, 0f);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> data) {
        if (DATA_HOOKED_ENTITY.equals(data)) {
            int i = this.getEntityData().get(DATA_HOOKED_ENTITY);
            this.hookedIn = i > 0 ? this.level().getEntity(i - 1) : null;
        }

        if (DATA_BITING.equals(data)) {
            this.biting = this.getEntityData().get(DATA_BITING);
            if (this.biting) {
                this.setDeltaMovement(this.getDeltaMovement().x, -0.4F * Mth.nextFloat(this.syncedRandom, 0.6F, 1.0F), this.getDeltaMovement().z);
            }
        }

        if (DATA_ROD_ITEM.equals(data)) this.rod = this.getEntityData().get(DATA_ROD_ITEM);

        if (DATA_CATCH_TYPE.equals(data) && level().isClientSide()) {
            this.catchType = CatchType.values()[this.getEntityData().get(DATA_CATCH_TYPE)];
        }

        if (DATA_MINIGAME_ACTIVE.equals(data)) {
            minigameActive = this.getEntityData().get(DATA_MINIGAME_ACTIVE);
            if (!level().isClientSide() && !minigameActive) restartFishingSequence();
        }

        super.onSyncedDataUpdated(data);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dst) {
        return dst < 4096.0D;
    }

    @Override
    public void tick() {
        if (getRodItem() == null) discard();

        this.syncedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        super.tick();
        if (this.level().isClientSide()) lineParticles();
        else glow();

        // Particle effects
        if (BaitUtils.hasBait(TideItems.LUCKY_BAIT, rod) && this.currentState == FishHookState.BOBBING) {
            particleTimer++;
            if (particleTimer >= 5) {
                this.level().addParticle(ParticleTypes.WAX_ON,
                        this.getRandomX(0.8),
                        this.getY((2.0 * this.random.nextDouble() - 1.0) * 0.4) + 0.2,
                        this.getRandomZ(0.8),
                        0.0, 0.0, 0.0);
                particleTimer = 0;
            }
        }

        if (BaitUtils.hasBait(TideItems.MAGNETIC_BAIT, rod) && this.currentState == FishHookState.BOBBING) {
            particleTimer++;
            if (particleTimer >= 8) {
                Vec3 pos = this.position();
                Vec3 randomPos = new Vec3(
                        this.getX() + 0.5 * (this.random.nextGaussian() - this.random.nextGaussian()),
                        this.getY() + 0.5 * (this.random.nextGaussian() - this.random.nextGaussian()),
                        this.getZ() + 0.5 * (this.random.nextGaussian() - this.random.nextGaussian()));
                Vec3 speed = pos.vectorTo(randomPos);
                this.level().addParticle(ParticleTypes.OMINOUS_SPAWNING,
                        pos.x(), pos.y(), pos.z(),
                        speed.x(), speed.y(), speed.z());
                particleTimer = 0;
            }
        }

        Player player = this.getPlayerOwner();
        if (player == null) this.discard();
        else if (this.level().isClientSide() || this.shouldKeepFishing(player)) {
            if (this.onGround()) {
                this.life++;
                if (this.life >= 1200) {
                    this.discard();
                    return;
                }
            } else this.life = 0;

            float fluidHeight = 0.0f;
            BlockPos pos = this.blockPosition();
            this.fluidState = this.level().getFluidState(pos);
            this.medium = getCurrentMedium();

            if (medium != null) fluidHeight = medium.getHeight(this);
            boolean inMedium = fluidHeight > 0.0f && medium != null;

            if (this.currentState == FishHookState.FLYING) {
                if (this.hookedIn != null) {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.currentState = FishHookState.HOOKED_IN_ENTITY;
                    return;
                }

                if (inMedium) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
                    this.currentState = FishHookState.BOBBING;
                    lureHint();
                    return;
                }

                this.checkCollision();
            }
            else {
                if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
                    if (this.hookedIn != null) {
                        if (!this.hookedIn.isRemoved() && this.hookedIn.level().dimension() == this.level().dimension()) {
                            this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
                        } else {
                            this.setHookedEntity(null);
                            this.currentState = FishHookState.FLYING;
                        }
                    }

                    return;
                }

                if (this.currentState == FishHookState.BOBBING) {
                    Vec3 delta = this.getDeltaMovement();
                    double toSurface = this.getY() + delta.y - (double)pos.getY() - (double)fluidHeight;
                    if (Math.abs(toSurface) < 0.01) toSurface += Math.signum(toSurface) * 0.1;
                    this.setDeltaMovement(
                            delta.x * 0.9,
                            delta.y - toSurface * (double)this.random.nextFloat() * 0.2,
                            delta.z * 0.9
                    );

                    if (this.nibble <= 0 && this.timeUntilHooked <= 0) this.openWater = true;
                    else this.openWater = this.openWater && this.outOfWaterTime < 20 && this.calculateOpenWater(pos);

                    if (inMedium) {
                        this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
                        if (this.biting) this.setDeltaMovement(this.getDeltaMovement().add(0.0,
                                -0.1 * (double)this.syncedRandom.nextFloat() * (double)this.syncedRandom.nextFloat(), 0.0));

                        if (!this.level().isClientSide()) this.catchingFish(pos);
                    }
                    else this.outOfWaterTime = Math.min(20, this.outOfWaterTime + 1);
                }
            }

            if (getCurrentMedium() == null) this.setDeltaMovement(
                    this.getDeltaMovement().add(0.0, -0.03, 0.0));

            this.move(MoverType.SELF, this.getDeltaMovement());

            if (this.currentState == FishHookState.FLYING
                    && (this.onGround() || this.horizontalCollision))
                this.setDeltaMovement(Vec3.ZERO);

            double scalar = 0.92;
            this.setDeltaMovement(this.getDeltaMovement().scale(scalar));
            this.reapplyPosition();
        }
    }

    @Nullable
    public FishingMedium getCurrentMedium() {
        return FishingMedium.MEDIUMS.stream()
                .filter(medium -> medium.canFishIn(this))
                .findFirst().orElse(null);
    }

    private boolean shouldKeepFishing(Player player) {
        boolean flag = player.getMainHandItem().equals(rod);
        boolean flag1 = player.getOffhandItem().equals(rod);
        if (!player.isRemoved() && player.isAlive() && (flag || flag1) && !(this.distanceToSqr(player) > 1224.0)) {
            return true;
        } else {
            this.discard();
            return false;
        }
    }

    private void checkCollision() {
        HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        this.hitTargetOrDeflectSelf(result);
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return super.canHitEntity(entity) || entity.isAlive() && entity instanceof ItemEntity;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide()) {
            if (result.getEntity() == this.getOwner()) this.discard();
            this.setHookedEntity(result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(result.distanceTo(this)));
    }

    private void setHookedEntity(Entity p_150158_) {
        this.hookedIn = p_150158_;
        this.getEntityData().set(DATA_HOOKED_ENTITY, p_150158_ == null ? 0 : p_150158_.getId() + 1);
    }

    private void catchingFish(BlockPos pos) {
        if (medium == null) return;
        if (Tide.PLATFORM.isModLoaded("stardew_fishing")) {
            List<ItemStack> rewards = CompatHelper.stardewFishingGetRewards((HookAccessor) getPlayerOwner().fishing);
            if (!rewards.isEmpty()) return;
        }

        int i = 1;
        BlockPos abovePos = pos.above();
        if (isWaterFishing() && this.random.nextFloat() < 0.25 && this.level().isRainingAt(abovePos)) i++;
        if (isWaterFishing() && this.random.nextFloat() < 0.5 && !this.level().canSeeSky(abovePos)) i++;

        if (this.nibble > 0) {
            // When a fish is on the hook
            if (!minigameActive) this.nibble--;
            if (this.nibble <= 0) restartFishingSequence();
        }
        else if (this.timeUntilHooked > 0) {
            // When a fish is visible, but not yet caught
            this.timeUntilHooked -= i;

            if (this.timeUntilHooked > 0) {

                this.fishAngle += (float) this.random.triangle(0.0, 9.188);

                float angleRad = this.fishAngle * ((float)Math.PI / 180f);
                float sin = Mth.sin(angleRad);
                float cos = Mth.cos(angleRad);

                double fishX = this.getX() + (double)(sin * (float)this.timeUntilHooked * 0.1f);
                double fishY = (float)Mth.floor(this.getY()) + 1.0f;
                double fishZ = this.getZ() + (double)(cos * (float)this.timeUntilHooked * 0.1f);

                medium.drawFishTrail(this, angleRad, sin, cos, fishX, fishY, fishZ);
            }
            else {
                // When a fish first touches the hook
                if (rod.is(TideItems.CRYSTAL_FISHING_ROD)) {
                    this.level().playSound(
                            null, getPlayerOwner().blockPosition(),
                            SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.MASTER, 1.5f,
                            1.0f - (this.random.nextFloat() - this.random.nextFloat()) * 0.1f
                    );
                }
                if (rod.is(TideItems.ECHO_FISHING_ROD)) {
                    this.level().playSound(
                            null, getPlayerOwner().blockPosition(),
                            SoundEvents.SCULK_CLICKING, SoundSource.MASTER, 1.0f,
                            1.0f - (this.random.nextFloat() - this.random.nextFloat()) * 0.1f
                    );
                }

                medium.onFishBite(this);

                // Select a catch
                this.nibble = Mth.nextInt(this.random, 20, 40);
                this.getEntityData().set(DATA_BITING, true);
                if (Tide.CI) Tide.LOG.info("TIDE-CI bite");
                if (getPlayerOwner() != null) {
                    this.selectCatch(rod);
                    String bobberPath = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(getBobber().getItem()).getPath();
                    if (bobberPath.equals("bell_bobber"))
                        this.level().playSound(null, getPlayerOwner().blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 0.7f, 1.4f);
                    if (bobberPath.equals("ender_eye_bobber") && !rod.is(TideItems.ECHO_FISHING_ROD) && getPlayerOwner() instanceof ServerPlayer peeker) {
                        if ((catchType == CatchType.ITEM || catchType == CatchType.FISH) && !hookedItems.isEmpty() && !hookedItems.get(0).isEmpty())
                            Tide.NETWORK.sendToPlayer(new EchoRodHookedMsg(hookedItems.get(0)), peeker);
                        else if (catchType == CatchType.CRATE && crateData != null)
                            Tide.NETWORK.sendToPlayer(new EchoRodHookedMsg(new ItemStack(crateData.blockProvider().getState(level(), random, blockPosition()).getBlock().asItem())), peeker);
                    }
                    if (rod.is(TideItems.ECHO_FISHING_ROD)) {
                        ServerPlayer serverPlayer = (ServerPlayer) getPlayerOwner();
                        if (catchType == CatchType.ITEM || catchType == CatchType.FISH) {
                            if (!hookedItems.isEmpty() && !hookedItems.get(0).isEmpty())
                                Tide.NETWORK.sendToPlayer(new EchoRodHookedMsg(hookedItems.get(0)), serverPlayer);
                        }
                        else if (catchType == CatchType.CRATE && crateData != null) {
                            Item crate = crateData.blockProvider().getState(level(), random, pos).getBlock().asItem();
                            Tide.NETWORK.sendToPlayer(new EchoRodHookedMsg(new ItemStack(crate)), serverPlayer);
                        }
                    }
                }
                else {
                    catchType = CatchType.NOTHING;
                    getEntityData().set(DATA_CATCH_TYPE, catchType.ordinal());
                }
            }
        }
        else if (this.timeUntilLured > 0) {
            // While a fish is waiting to appear
            this.timeUntilLured -= i;

            float f5 = 0.15f;
            if (this.timeUntilLured < 20) f5 += (float)(20 - this.timeUntilLured) * 0.05f;
            else if (this.timeUntilLured < 40) f5 += (float)(40 - this.timeUntilLured) * 0.02f;
            else if (this.timeUntilLured < 60) f5 += (float)(60 - this.timeUntilLured) * 0.01f;

            if (this.random.nextFloat() < f5) {
                float angleRad = Mth.nextFloat(this.random, 0.0f, 360.0f) * ((float)Math.PI / 180f);
                float distance = Mth.nextFloat(this.random, 25.0f, 60.0f);
                double x = this.getX() + (double)(Mth.sin(angleRad) * distance) * 0.1;
                double y = (float)Mth.floor(this.getY()) + 1.0f;
                double z = this.getZ() + (double)(Mth.cos(angleRad) * distance) * 0.1;
                medium.drawAmbientSplash(this, angleRad, distance, x, y, z);
            }

            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0f, 360.0f);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        }
        else {
            this.timeUntilLured = (int) (Mth.nextInt(this.random, 200, 600) * this.medium.biteTimeMultiplier());
            double globalBiteTimeMultiplier = Tide.PLATFORM.isModLoaded("stardew_fishing") ? CompatHelper.stardewFishingBiteTimeMultiplier() : 1.0;
            // fishing level: patience (how long until a bite)
            if (this.getPlayerOwner() instanceof ServerPlayer angler)
                // fishing level, then one bite-speed number: gear + lure/bait/rod speed points, softened (see RodEffects.softenBite)
                this.timeUntilLured = (int) (this.timeUntilLured * com.li64.tide.data.angler.Angler.stats(angler).biteWait()
                        * com.li64.tide.data.rods.RodEffects.of(angler, this.rod).biteWaitFactor(this.lureSpeed));
            else this.timeUntilLured = (int) (this.timeUntilLured * com.li64.tide.data.rods.RodEffects.NONE.biteWaitFactor(this.lureSpeed));
            this.timeUntilLured = Math.max((int) (this.timeUntilLured * globalBiteTimeMultiplier), 30);
        }
    }

    private boolean isWaterFishing() {
        return medium == FishingMedium.WATER;
    }

    public void restartFishingSequence() {
        catchType = CatchType.NOTHING;
        getEntityData().set(DATA_CATCH_TYPE, catchType.ordinal());
        this.timeUntilLured = 0;
        this.timeUntilHooked = 0;
        this.getEntityData().set(DATA_BITING, false);
    }

    private boolean calculateOpenWater(BlockPos pos) {
        OpenFluidType openWaterType = OpenFluidType.INVALID;

        for(int i = -1; i <= 2; ++i) {
            OpenFluidType openWaterType1 = this.getOpenWaterTypeForArea(pos.offset(-2, i, -2), pos.offset(2, i, 2));
            switch (openWaterType1) {
                case INVALID:
                    return false;
                case ABOVE_WATER:
                    if (openWaterType == OpenFluidType.INVALID) {
                        return false;
                    }
                    break;
                case INSIDE_WATER:
                    if (openWaterType == OpenFluidType.ABOVE_WATER) {
                        return false;
                    }
            }

            openWaterType = openWaterType1;
        }

        return true;
    }

    private OpenFluidType getOpenWaterTypeForArea(BlockPos pos1, BlockPos pos2) {
        return BlockPos.betweenClosedStream(pos1, pos2).map(this::getOpenWaterTypeForBlock).reduce((type1, type2) -> type1 == type2 ? type1 : OpenFluidType.INVALID).orElse(OpenFluidType.INVALID);
    }

    private OpenFluidType getOpenWaterTypeForBlock(BlockPos pos) {
        BlockState blockState = this.level().getBlockState(pos);
        if (!blockState.isAir() && !blockState.is(Blocks.LILY_PAD)) {
            FluidState fluidState = blockState.getFluidState();
            return FishingMedium.WATER.canFishIn(this) && fluidState.isSource()
                    && blockState.getCollisionShape(this.level(), pos).isEmpty()
                    ? OpenFluidType.INSIDE_WATER : OpenFluidType.INVALID;
        }
        else return OpenFluidType.ABOVE_WATER;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull net.minecraft.world.level.storage.ValueOutput tag) {}

    @Override
    protected void readAdditionalSaveData(@NotNull net.minecraft.world.level.storage.ValueInput tag) {}

    public void retrieve() {
        this.retrieve(false);
    }

    /** How cleanly the fish was hooked in the minigame (sets catch quality odds). */
    public com.li64.tide.data.angler.Angler.Hook hookResult = com.li64.tide.data.angler.Angler.Hook.GOOD;
    /** The quality rolled when the fish bit (the minigame fights accordingly); null without a minigame. */
    public com.li64.tide.data.angler.CatchQuality pendingQuality = null;

    public void retrieve(boolean perfectCatch) {
        if (getRodItem() == null) discard();
        this.wasPerfectCatch = perfectCatch;
        getRodItem().retrieveHook(rod, getPlayerOwner(), level());
    }

    /** The fish as it would come out of a furnace (keeping its quality and species name), or as caught if it can't be cooked. */
    public static ItemStack cookOnTheLine(ServerLevel level, ItemStack raw) {
        net.minecraft.world.item.crafting.SingleRecipeInput input = new net.minecraft.world.item.crafting.SingleRecipeInput(raw);
        return level.recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING, input, level)
                .map(r -> {
                    ItemStack cooked = r.value().assemble(input);
                    cooked.setCount(raw.getCount());
                    return cooked;
                })
                .filter(s -> !s.isEmpty())
                .orElse(raw);
    }

    // ------------------------------------------------------------------ the glow hook lights the water around it

    private net.minecraft.core.BlockPos glowPos = null;

    /** A faint light where a glow hook sits (an invisible light block, waterlogged in water), taken away when it moves or is reeled in. */
    private void glow() {
        if (!(this.level() instanceof ServerLevel server)) return;
        boolean wants = this.getHook().is(TideItems.GLOW_HOOK) && !this.isRemoved()
                && (this.getCurrentMedium() == FishingMedium.WATER || this.onGround());
        net.minecraft.core.BlockPos at = wants ? this.blockPosition() : null;
        if (java.util.Objects.equals(at, glowPos)) return;
        clearGlow();
        if (at == null) return;
        net.minecraft.world.level.block.state.BlockState here = server.getBlockState(at);
        boolean water = here.is(net.minecraft.world.level.block.Blocks.WATER) && here.getFluidState().isSource();
        if (!water && !here.isAir()) return;
        server.setBlock(at, net.minecraft.world.level.block.Blocks.LIGHT.defaultBlockState()
                .setValue(net.minecraft.world.level.block.LightBlock.LEVEL, com.li64.tide.data.rods.GlowLights.LEVEL)
                .setValue(net.minecraft.world.level.block.LightBlock.WATERLOGGED, water), 3);
        glowPos = at.immutable();
        com.li64.tide.data.rods.GlowLights.placed(server, glowPos);     // remembered with the world, in case the server stops mid-cast
    }

    private void clearGlow() {
        if (glowPos == null || !(this.level() instanceof ServerLevel server)) return;
        com.li64.tide.data.rods.GlowLights.restore(server, glowPos);
        com.li64.tide.data.rods.GlowLights.removed(server, glowPos);
        glowPos = null;
    }

    /** Once the hook settles: does anything that favors this lure live here? (A count, never names.) */
    private void lureHint() {
        if (!(this.getPlayerOwner() instanceof net.minecraft.server.level.ServerPlayer player) || !CustomRodManager.hasLure(rod)) return;
        try {
            ItemStack lure = CustomRodManager.getLure(rod);
            int n = com.li64.tide.data.rods.FavoriteLures.here(getContext(), lure);
            player.sendOverlayMessage(net.minecraft.network.chat.Component.literal(n == 0
                    ? "No fish here favor your " + lure.getHoverName().getString()
                    : n + (n == 1 ? " fish here favors" : " fish here favor") + " your " + lure.getHoverName().getString())
                    .withStyle(n == 0 ? net.minecraft.ChatFormatting.GRAY : net.minecraft.ChatFormatting.AQUA));
        } catch (Exception e) {
            Tide.LOG.debug("lure hint failed", e);
        }
    }

    /** Some lines give off particles along their length while cast (golden sparkles, redstone sparks...). */
    private void lineParticles() {
        Player owner = getPlayerOwner();
        if (owner == null) return;
        net.minecraft.core.particles.ParticleOptions particle = com.li64.tide.registries.items.FishingLineItem.particle(getLine());
        if (particle == null) return;
        // a hint, not a light show: about one every second or two (long-lived glowing ones even fewer),
        // and never right at the hook, so the bobber stays easy to watch
        boolean lingers = particle == ParticleTypes.END_ROD || particle == ParticleTypes.GLOW;
        if (this.random.nextInt(lingers ? 60 : 25) != 0) return;
        net.minecraft.world.phys.Vec3 hand = owner.position().add(0, owner.getEyeHeight() * 0.75, 0);
        double t = 0.1 + this.random.nextDouble() * 0.6;
        net.minecraft.world.phys.Vec3 at = hand.lerp(this.position().add(0, 0.25, 0), t);
        this.level().addParticle(particle, at.x, at.y - Math.sin(t * Math.PI) * 0.3, at.z, 0, 0.01, 0);
    }

    public int retrieve(ItemStack rod, ServerLevel level, Player player) {
        if (player == null || player.fishing == null || !(player instanceof ServerPlayer serverPlayer)) return 0;
        if (this.level().isClientSide() || !shouldKeepFishing(player)) return 0;
        int i = 0;
        if (this.getHookedIn() != null) {
            this.pullEntity(this.getHookedIn());
            CriteriaTriggers.FISHING_ROD_HOOKED.trigger(serverPlayer, rod, player.fishing, Collections.emptyList());
            this.level().broadcastEntityEvent(this, (byte) 31);
            i = this.getHookedIn() instanceof ItemEntity ? 3 : 5;
        }
        else if (nibble > 0) {
            if (!hasHookedItem()) catchType = CatchType.NOTHING;

            switch (catchType) {
                case FISH, ITEM:
                    if (medium == FishingMedium.LAVA) TideCriteriaTriggers.FISHED_IN_LAVA.trigger(serverPlayer);
                    if (medium == FishingMedium.VOID) TideCriteriaTriggers.FISHED_IN_VOID.trigger(serverPlayer);
                    CriteriaTriggers.FISHING_ROD_HOOKED.trigger(serverPlayer, rod, player.fishing, hookedItems);

                    List<ItemStack> hookedItemsCopy = hookedItems.stream().map(ItemStack::copy).toList();

                    for (ItemStack stack : hookedItems) {
                        if (stack.isEmpty()) continue;
                        if (catchType == CatchType.FISH)
                            safely("grading the catch", () -> com.li64.tide.data.angler.CatchQuality.apply(serverPlayer, stack, this.pendingQuality));

                        // the rod's extras: a second fish (treble hook), or one cooked on the line (redstone line, blazing rod)
                        ItemStack drop = stack;
                        int copies = 1;
                        if (catchType == CatchType.FISH) try {
                            com.li64.tide.data.rods.RodEffects extras = com.li64.tide.data.rods.RodEffects.of(serverPlayer, this.rod);
                            if (extras.cook() > 0 && (Tide.CI || this.random.nextDouble() < extras.cook())) {
                                drop = cookOnTheLine(level, stack);
                                if (drop != stack) com.li64.tide.data.angler.Angler.markExtra(serverPlayer, "Came up cooked");
                            }
                            if (extras.twin() > 0 && (Tide.CI || this.random.nextDouble() < Math.min(extras.twin(), com.li64.tide.data.rods.RodEffects.TWIN_CAP))) {
                                copies = 2;
                                com.li64.tide.data.angler.Angler.markTwin(serverPlayer);
                            }
                        } catch (RuntimeException e) {
                            Tide.LOG.error("Tide+: the rod's extras failed (the catch still lands)", e);
                            drop = stack;
                            copies = 1;
                        }
                        if (Tide.CI && catchType == CatchType.FISH)
                            Tide.LOG.info("TIDE-CI extras: drop {} x{}{}", drop.getHoverName().getString(), copies, drop == stack ? "" : " (cooked)");
                        for (int copy = 0; copy < copies; copy++) {
                        ItemStack thisDrop = copy == 0 ? drop : drop.copy();
                        Entity entity;
                        if (medium == FishingMedium.LAVA) {
                            entity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), thisDrop) {
                                public boolean displayFireAnimation() { return false; }
                                public void lavaHurt() {}
                            };
                        } else entity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), thisDrop);

                        double dx = player.getX() - this.getX();
                        double dy = player.getY() - this.getY();
                        double dz = player.getZ() - this.getZ();

                        entity.setDeltaMovement(pullToward(player, this.getX(), this.getY(), this.getZ()));
                        if (CompatHelper.isHybridAquaticLoaded() && entity instanceof ItemEntity itemEntity) {
                            entity = CompatHelper.hybridAquaticPullEntity(itemEntity, player, this);
                        }
                        if (Tide.PLATFORM.isModLoaded("fishingreal") && entity instanceof ItemEntity) {
                            Entity converted = CompatHelper.fishingRealConvertItemStack(thisDrop, player, position());
                            if (converted != null) {
                                entity = converted;
                                this.level().addFreshEntity(converted);
                            }
                        }
                        if (entity instanceof ItemEntity) {
                            this.level().addFreshEntity(entity);
                            if (stack.is(ItemTags.FISHES)) player.awardStat(Stats.FISH_CAUGHT, 1);
                        }
                        }
                        // the rod's bonus drops (honeycomb rod's honey...)
                        if (catchType == CatchType.FISH) safely("the rod's bonus drops", () -> {
                            int q = TideItemData.FISH_QUALITY.getOrDefault(stack, 1);
                            for (com.li64.tide.data.rods.RodEffects.Drop d : com.li64.tide.data.rods.RodEffects.drops(this.rod)) {
                                if (!Tide.CI && (q < d.minQuality() || this.random.nextDouble() >= d.chance())) continue;
                                net.minecraft.world.item.Item extraItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                                        .getValue(net.minecraft.resources.Identifier.parse(d.item()));
                                ItemEntity extra = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(extraItem));
                                com.li64.tide.data.angler.Angler.markExtra(serverPlayer, "+ " + new ItemStack(extraItem).getHoverName().getString());
                                double ex = player.getX() - this.getX(), ey = player.getY() - this.getY(), ez = player.getZ() - this.getZ();
                                extra.setDeltaMovement(pullToward(player, this.getX(), this.getY(), this.getZ()));
                                this.level().addFreshEntity(extra);
                            }
                            com.li64.tide.data.rods.RodEffects.Pick pick = com.li64.tide.data.rods.RodEffects.pick(this.rod);
                            if (pick != null && (Tide.CI || this.random.nextDouble() < pick.chance())) {
                                ItemStack bonus = pick.roll(this.random);
                                if (!bonus.isEmpty()) {
                                    ItemEntity extra = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), bonus);
                                    com.li64.tide.data.angler.Angler.markExtra(serverPlayer, "+ " + bonus.getHoverName().getString());
                                    double ex = player.getX() - this.getX(), ey = player.getY() - this.getY(), ez = player.getZ() - this.getZ();
                                    extra.setDeltaMovement(pullToward(player, this.getX(), this.getY(), this.getZ()));
                                    this.level().addFreshEntity(extra);
                                    if (Tide.CI) Tide.LOG.info("TIDE-CI pick: {}", bonus.getHoverName().getString());
                                }
                            }
                            com.li64.tide.data.rods.FoundRodTricks.onCatch(serverPlayer, this.rod, this.random);
                        });

                        player.level().addFreshEntity(new ExperienceOrb(
                                player.level(), player.getX(), player.getY() + 0.5, player.getZ() + 0.5,
                                this.random.nextInt(4) + (this.wasPerfectCatch ? 4 : 1)));
                        if (this.rod.is(TideItems.DIAMOND_FISHING_ROD)) player.level().addFreshEntity(new ExperienceOrb(
                                player.level(), player.getX(), player.getY() + 0.5, player.getZ() + 0.5,
                                this.random.nextInt(4) + 1));

                        if (catchType == CatchType.ITEM) com.li64.tide.data.angler.Angler.onItemCaught(serverPlayer);
                        if (catchType == CatchType.FISH) {
                            if (!player.isCreative() && BaitUtils.hasBait(rod)) {
                                // consume used bait
                                BaitContents.Mutable contents = new BaitContents.Mutable(TideItemData.BAIT_CONTENTS.getOrDefault(rod, new BaitContents()));
                                contents.shrinkAll();
                                TideItemData.BAIT_CONTENTS.set(rod, contents.toImmutable());
                            }

                            // fishing XP (before the journal, so first catches and records count), then the journal
                            boolean newSpecies = TidePlayerData.getOrCreate(serverPlayer).getDataFor(stack.getItem())
                                    .flatMap(f -> f.stats).map(st -> st.getAmountCaught() == 0).orElse(true);
                            // each extra on its own: one failing must never cost the journal entry (or the others)
                            safely("the server record", () -> {
                                if (com.li64.tide.data.angler.ServerRecords.onCatch(serverPlayer, stack))
                                    com.li64.tide.data.angler.Angler.markServerRecord(serverPlayer);
                            });
                            long[] gained = {0};
                            safely("fishing XP", () -> gained[0] = com.li64.tide.data.angler.Angler.onFishCaught(serverPlayer, stack,
                                    this.wasPerfectCatch, com.li64.tide.data.rods.RodEffects.of(serverPlayer, this.rod).xp()));
                            TideUtils.tryLogCatch(stack, serverPlayer);
                            safely("the catch log", () -> com.li64.tide.data.angler.Angler.logCatch(serverPlayer, stack, this.hookResult, gained[0], newSpecies));
                        }
                    }
                    break;

                case CRATE:

                    BlockState lootCrate = crateData.blockProvider().getState(level(), random, blockPosition());

                    LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) this.level())
                            .withParameter(LootContextParams.ORIGIN, this.position())
                            .withParameter(LootContextParams.TOOL, rod)
                            .withParameter(LootContextParams.BLOCK_STATE, lootCrate)
                            .withParameter(LootContextParams.THIS_ENTITY, this);

                    LootParams params = lootParamsBuilder
                            .withLuck((float)luck + player.getLuck())
                            .create(LootContextParamSets.FISHING);

                    level.setBlockAndUpdate(this.blockPosition(), lootCrate);

                    double dx = player.getX() - this.blockPosition().getX();
                    double dy = player.getY() - this.blockPosition().getY();
                    double dz = player.getZ() - this.blockPosition().getZ();

                    LootCrateEntity.create(level, this.blockPosition(), lootCrate,
                            dx * 0.0666,
                            dy * 0.0666 + Math.sqrt(Math.sqrt(dx * dx + dy * dy + dz * dz)) * 0.082 + 0.27,
                            dz * 0.0666,
                            params, crateData.lootTable().map(LootTableRef::of).orElse(null)
                    );

                    if (fluidState.is(TideTags.Fluids.LAVA_FISHING) && fluidState.is(Fluids.LAVA)) level.setBlockAndUpdate(this.blockPosition(), Blocks.LAVA.defaultBlockState());
                    if (fluidState.is(TideTags.Fluids.WATER_FISHING) && fluidState.is(Fluids.WATER)) level.setBlockAndUpdate(this.blockPosition(), Blocks.WATER.defaultBlockState());

                    TideCriteriaTriggers.PULLED_CRATE.trigger(serverPlayer);
                    com.li64.tide.data.angler.Angler.onCrateCaught(serverPlayer);
                    break;

                case NOTHING:
                    break;
            }
            i = 1;
        }
        if (this.onGround()) i = 2;
        this.startRetrieving();

        return i;
    }

    /**
     * The push that sends a catch to the angler's feet. It used to scale with the distance (so a far cast sent the fish
     * over their head); this works out an arc of 10-28 ticks (longer for farther) that comes down at the angler,
     * allowing for the item's gravity, air drag and the slide after it lands.
     */
    private static net.minecraft.world.phys.Vec3 pullToward(Player player, double fromX, double fromY, double fromZ) {
        double dx = player.getX() - fromX, dz = player.getZ() - fromZ, dy = player.getY() + 0.25 - fromY;
        double dist = Math.sqrt(dx * dx + dz * dz);
        final double drag = 0.98, gravity = 0.04, slide = 2.4;           // slide: blocks gone per unit of speed on landing
        int ticks = (int) Mth.clamp(Math.round(10 + dist * 0.4), 10, 28);
        double fall = Math.pow(drag, ticks);
        double sum = (1 - fall) / (1 - drag);                              // total of the horizontal speeds over the flight, per unit
        double horizontal = dist / (sum + slide * fall);                   // flight plus slide covers the distance
        double terminal = -gravity / (1 - drag);
        double lift = terminal + (dy - ticks * terminal) / sum;
        double scale = dist > 0.01 ? horizontal / dist : 0;
        return new net.minecraft.world.phys.Vec3(dx * scale, lift + gravity, dz * scale);
    }

    public void selectCatch(ItemStack rod) {
        MinecraftServer server = level().getServer();
        if (server == null) return;

        FishingContext context = getContext();
        LootParams params = context.createFishingLootParams();
        CatchResult result = Tide.FISHING_MANAGER.selectCatch(context);

        if (result.isEmpty()) {
            Tide.LOG.warn("Could not find a valid catch for this context: {}", context);
            LootTable table = TideUtils.getLootTable(BuiltInLootTables.FISHING_JUNK, server);
            List<ItemStack> items = table.getRandomItems(params);
            this.hookedItems = items.isEmpty() ? List.of() : items;
        }
        else this.hookedItems = result.items();

        if (rod.is(TideItems.MIDAS_FISHING_ROD) && medium != FishingMedium.LAVA) {
            ItemStack bonus = TideUtils.getBonusGoldItem(server, params);
            if (bonus != null) hookedItems = ImmutableList.<ItemStack>builder()
                    .addAll(hookedItems).add(bonus).build();
        }
        if (rod.is(TideItems.VILLAGE_FISHING_ROD) && medium != FishingMedium.LAVA) {
            ItemStack bonus = TideUtils.getBonusVillageItem(server, params);
            if (bonus != null) hookedItems = ImmutableList.<ItemStack>builder()
                    .addAll(hookedItems).add(bonus).build();
        }

        this.catchType = result.isFish() || hookedItems.stream().anyMatch(TideUtils::isJournalFish)
                ? CatchType.FISH : (hookedItems.isEmpty() ? CatchType.NOTHING : CatchType.ITEM);
        if (result.entry().isPresent() && result.isCrate()
                && result.entry().get() instanceof CrateData data) {
            crateData = data;
            catchType = CatchType.CRATE;
        }

        getEntityData().set(DATA_CATCH_TYPE, catchType.ordinal());
    }

    public void handleEntityEvent(byte event) {
        if (event == 31 && this.level().isClientSide() && this.hookedIn instanceof Player && ((Player)this.hookedIn).isLocalPlayer()) {
            this.pullEntity(this.hookedIn);
        }

        super.handleEntityEvent(event);
    }

    protected void pullEntity(Entity entity) {
        Entity owner = this.getOwner();
        if (owner != null) {
            Vec3 vec3 = (new Vec3(owner.getX() - this.getX(), owner.getY() - this.getY(), owner.getZ() - this.getZ())).scale(0.1D);
            entity.setDeltaMovement(entity.getDeltaMovement().add(vec3));
        }
    }

    protected void startRetrieving() {
        catchType = CatchType.NOTHING;
        this.discard();
    }

    public FishingContext getContext() {
        boolean netherOverride = rod.is(TideItems.BLAZING_FISHING_ROD) && medium == FishingMedium.LAVA;

        float temperatureAddition = 0f;
        ItemStack hook = this.getHook();
        if (hook.is(TideItems.FIERY_HOOK)) temperatureAddition = 0.25f;
        if (hook.is(TideItems.PERMAFROST_HOOK)) temperatureAddition = -0.25f;

        return new FishingContext(
                (ServerLevel) level(),
                this, this.rod,
                level().getRandom(),
                position(),
                blockPosition(),
                Mth.floor(getLuck() + getPlayerOwner().getLuck()),
                Optional.ofNullable(medium).orElse(FishingMedium.WATER).id().getPath(),
                getBiome(),
                TideUtils.findClosestNonWaterBiome(level(), blockPosition(), 12, 2).orElse(getBiome()),
                netherOverride ? Level.NETHER : level().dimension(),
                Mth.clamp(sampleTemperature() + temperatureAddition, -1.0f, 1.0f),
                com.li64.tide.util.Mc26.moonPhase(level(), position()),
                SeasonsCompat.getSeason(level())
        );
    }

    public float sampleTemperature() {
        if (!(level() instanceof ServerLevel level)) {
            Tide.LOG.error("Tried to sample temperature on the client");
            return 0f;
        }
        return TideUtils.getTemperatureAt(blockPosition(), level);
    }

    protected @NotNull MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    public void remove(@NotNull RemovalReason reason) {
        clearGlow();
        this.updateOwnerInfo(null);
        super.remove(reason);
    }

    public void onClientRemoval() {
        this.updateOwnerInfo(null);
    }

    public void setOwner(Entity entity) {
        super.setOwner(entity);
        this.updateOwnerInfo(this);
    }

    private void updateOwnerInfo(TideFishingHook hook) {
        Player player = this.getPlayerOwner();
        if (player != null) {
            if (hook == null && player.fishing != null) ((HookAccessor)player.fishing).clearHook(player);
            if (player.fishing == null && hook != null) player.fishing = new HookAccessor(hook, level());
        }
    }

    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player)entity : null;
    }

    public Entity getHookedIn() {
        return this.hookedIn;
    }

    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        if (this.getPlayerOwner() == null) {
            int i = packet.getData();
            Tide.LOG.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.level().getEntity(i), i);
            this.discard();
        }
    }

    @Override
    public boolean fireImmune() {
        if (canFishInLava()) return true;
        else return super.fireImmune();
    }

    @Override
    public boolean isOnFire() {
        if (canFishInLava()) return false;
        else return super.isOnFire();
    }

    public boolean canFishInLava() {
        if (getRodItem() == null) return false;
        return getRodItem().isLavaproof(rod);
    }

    public boolean canFishInVoid() {
        if (getRodItem() == null) return false;
        return getRodItem().isVoidproof(rod);
    }

    public ItemStack rod() {
        return this.rod;
    }

    public TideFishingRodItem getRodItem() {
        if (rod.getItem() instanceof TideFishingRodItem rodItem) return rodItem;
        return null;
    }

    public ItemStack getBobber() {
        return CustomRodManager.getBobber(rod);
    }
    public ItemStack getHook() {
        return CustomRodManager.getHook(rod);
    }
    public ItemStack getLine() {
        return CustomRodManager.getLine(rod);
    }

    public ItemStack getRod() {
        return rod;
    }

    public long getMinigameStartTime() {
        return minigameStartTime;
    }

    public void setMinigameStartTime(long time) {
        this.minigameStartTime = time;
    }

    public void replacePrimaryCatch(ItemStack stack) {
        if (hookedItems == null || hookedItems.isEmpty()) return;
        List<ItemStack> items = new ArrayList<>(hookedItems);
        items.set(0, stack);
        hookedItems = items;
    }

    public void clearHookItem() {
        ItemStack mainHandItem = getPlayerOwner().getMainHandItem();
        ItemStack offhandItem = getPlayerOwner().getOffhandItem();
        if (mainHandItem.getItem() instanceof TideFishingRodItem) {
            CustomRodManager.setHook(mainHandItem, null);
            getPlayerOwner().setItemInHand(InteractionHand.MAIN_HAND, mainHandItem);
        } else if (offhandItem.getItem() instanceof TideFishingRodItem) {
            CustomRodManager.setHook(offhandItem, null);
            getPlayerOwner().setItemInHand(InteractionHand.OFF_HAND, offhandItem);
        }
    }

    public boolean hasHookedItem() {
        return hookedItems != null && !hookedItems.isEmpty();
    }

    public List<ItemStack> getHookedItems() {
        return hookedItems;
    }

    public void setMinigameActive(boolean state) {
        this.minigameActive = state;
        if (!minigameActive) nibble = 0;
        this.entityData.set(DATA_MINIGAME_ACTIVE, state);
    }

    public CatchType getCatchType() {
        return CatchType.values()[getEntityData().get(DATA_CATCH_TYPE)];
    }

    public @NotNull RandomSource getRandom() {
        return this.level().getRandom();
    }

    enum FishHookState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING
    }

    enum OpenFluidType {
        ABOVE_WATER,
        INSIDE_WATER,
        INVALID
    }

    /** Tide+'s extras around a catch: if one throws, log it and carry on, so the catch itself always lands. */
    private static void safely(String what, Runnable r) {
        try {
            r.run();
        } catch (RuntimeException e) {
            Tide.LOG.error("Tide+: {} failed (the catch still counts)", what, e);
        }
    }

}