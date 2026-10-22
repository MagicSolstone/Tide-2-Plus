package net.minecraft.world.entity;

import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Brightness;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public abstract class Display extends Entity {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final int NO_BRIGHTNESS_OVERRIDE = -1;
   private static final EntityDataAccessor<Integer> DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID = SynchedEntityData.defineId(
      Display.class, EntityDataSerializers.INT
   );
   private static final EntityDataAccessor<Integer> DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID = SynchedEntityData.defineId(
      Display.class, EntityDataSerializers.INT
   );
   private static final EntityDataAccessor<Integer> DATA_POS_ROT_INTERPOLATION_DURATION_ID = SynchedEntityData.defineId(
      Display.class, EntityDataSerializers.INT
   );
   private static final EntityDataAccessor<Vector3fc> DATA_TRANSLATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.VECTOR3);
   private static final EntityDataAccessor<Vector3fc> DATA_SCALE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.VECTOR3);
   private static final EntityDataAccessor<Quaternionfc> DATA_LEFT_ROTATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.QUATERNION);
   private static final EntityDataAccessor<Quaternionfc> DATA_RIGHT_ROTATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.QUATERNION);
   private static final EntityDataAccessor<Byte> DATA_BILLBOARD_RENDER_CONSTRAINTS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Integer> DATA_BRIGHTNESS_OVERRIDE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> DATA_VIEW_RANGE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> DATA_SHADOW_RADIUS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> DATA_SHADOW_STRENGTH_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> DATA_WIDTH_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> DATA_HEIGHT_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Integer> DATA_GLOW_COLOR_OVERRIDE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
   private static final IntSet RENDER_STATE_IDS = IntSet.of(
      new int[]{
         DATA_TRANSLATION_ID.id(),
         DATA_SCALE_ID.id(),
         DATA_LEFT_ROTATION_ID.id(),
         DATA_RIGHT_ROTATION_ID.id(),
         DATA_BILLBOARD_RENDER_CONSTRAINTS_ID.id(),
         DATA_BRIGHTNESS_OVERRIDE_ID.id(),
         DATA_SHADOW_RADIUS_ID.id(),
         DATA_SHADOW_STRENGTH_ID.id()
      }
   );
   private static final int INITIAL_TRANSFORMATION_INTERPOLATION_DURATION = 0;
   private static final int INITIAL_TRANSFORMATION_START_INTERPOLATION = 0;
   private static final int INITIAL_POS_ROT_INTERPOLATION_DURATION = 0;
   private static final float INITIAL_SHADOW_RADIUS = 0.0F;
   private static final float INITIAL_SHADOW_STRENGTH = 1.0F;
   private static final float INITIAL_VIEW_RANGE = 1.0F;
   private static final float INITIAL_WIDTH = 0.0F;
   private static final float INITIAL_HEIGHT = 0.0F;
   private static final int NO_GLOW_COLOR_OVERRIDE = -1;
   public static final String TAG_POS_ROT_INTERPOLATION_DURATION = "teleport_duration";
   public static final String TAG_TRANSFORMATION_INTERPOLATION_DURATION = "interpolation_duration";
   public static final String TAG_TRANSFORMATION_START_INTERPOLATION = "start_interpolation";
   public static final String TAG_TRANSFORMATION = "transformation";
   public static final String TAG_BILLBOARD = "billboard";
   public static final String TAG_BRIGHTNESS = "brightness";
   public static final String TAG_VIEW_RANGE = "view_range";
   public static final String TAG_SHADOW_RADIUS = "shadow_radius";
   public static final String TAG_SHADOW_STRENGTH = "shadow_strength";
   public static final String TAG_WIDTH = "width";
   public static final String TAG_HEIGHT = "height";
   public static final String TAG_GLOW_COLOR_OVERRIDE = "glow_color_override";
   private long interpolationStartClientTick = -2147483648L;
   private int interpolationDuration;
   private float lastProgress;
   private AABB cullingBoundingBox;
   private boolean noCulling = true;
   protected boolean updateRenderState;
   private boolean updateStartTick;
   private boolean updateInterpolationDuration;
   @Nullable
   private Display$RenderState renderState;

   public Display(final EntityType<?> type, final Level level) {
      super(type, level);
      this.noPhysics = true;
      this.cullingBoundingBox = this.getBoundingBox();
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (DATA_HEIGHT_ID.equals(accessor) || DATA_WIDTH_ID.equals(accessor)) {
         this.updateCulling();
      }

      if (DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID.equals(accessor)) {
         this.updateStartTick = true;
      }

      if (DATA_POS_ROT_INTERPOLATION_DURATION_ID.equals(accessor) && this.interpolationHandler instanceof LinearInterpolationHandler interpolation) {
         interpolation.setInterpolationLength(this.getPosRotInterpolationDuration());
      }

      if (DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID.equals(accessor)) {
         this.updateInterpolationDuration = true;
      }

      if (RENDER_STATE_IDS.contains(accessor.id())) {
         this.updateRenderState = true;
      }
   }

   @Override
   public final boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
      return false;
   }

   private static Transformation createTransformation(final SynchedEntityData entityData) {
      Vector3fc translation = entityData.get(DATA_TRANSLATION_ID);
      Quaternionfc leftRotation = entityData.get(DATA_LEFT_ROTATION_ID);
      Vector3fc scale = entityData.get(DATA_SCALE_ID);
      Quaternionfc rightRotation = entityData.get(DATA_RIGHT_ROTATION_ID);
      return new Transformation(translation, leftRotation, scale, rightRotation);
   }

   @Override
   public void tick() {
      Entity vehicle = this.getVehicle();
      if (vehicle != null && vehicle.isRemoved()) {
         this.stopRiding();
      }

      if (this.level().isClientSide()) {
         if (this.updateStartTick) {
            this.updateStartTick = false;
            int interpolationStartDelta = this.getTransformationInterpolationDelay();
            this.interpolationStartClientTick = this.tickCount + interpolationStartDelta;
         }

         if (this.updateInterpolationDuration) {
            this.updateInterpolationDuration = false;
            this.interpolationDuration = this.getTransformationInterpolationDuration();
         }

         if (this.updateRenderState) {
            this.updateRenderState = false;
            boolean shouldInterpolate = this.interpolationDuration != 0;
            if (shouldInterpolate && this.renderState != null) {
               this.renderState = this.createInterpolatedRenderState(this.renderState, this.lastProgress);
            } else {
               this.renderState = this.createFreshRenderState();
            }

            this.updateRenderSubState(shouldInterpolate, this.lastProgress);
         }
      }
   }

   @Override
   protected InterpolationHandler createInterpolationHandler() {
      return LinearInterpolationHandler.create(this, 0);
   }

   protected abstract void updateRenderSubState(boolean shouldInterpolate, float progress);

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      entityData.define(DATA_POS_ROT_INTERPOLATION_DURATION_ID, 0);
      entityData.define(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID, 0);
      entityData.define(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID, 0);
      entityData.define(DATA_TRANSLATION_ID, new Vector3f());
      entityData.define(DATA_SCALE_ID, new Vector3f(1.0F, 1.0F, 1.0F));
      entityData.define(DATA_RIGHT_ROTATION_ID, new Quaternionf());
      entityData.define(DATA_LEFT_ROTATION_ID, new Quaternionf());
      entityData.define(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, Display$BillboardConstraints.FIXED.getId());
      entityData.define(DATA_BRIGHTNESS_OVERRIDE_ID, -1);
      entityData.define(DATA_VIEW_RANGE_ID, 1.0F);
      entityData.define(DATA_SHADOW_RADIUS_ID, 0.0F);
      entityData.define(DATA_SHADOW_STRENGTH_ID, 1.0F);
      entityData.define(DATA_WIDTH_ID, 0.0F);
      entityData.define(DATA_HEIGHT_ID, 0.0F);
      entityData.define(DATA_GLOW_COLOR_OVERRIDE_ID, -1);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      this.setTransformation(input.<Transformation>read("transformation", Transformation.EXTENDED_CODEC).orElse(Transformation.IDENTITY));
      this.setTransformationInterpolationDuration(input.getIntOr("interpolation_duration", 0));
      this.setTransformationInterpolationDelay(input.getIntOr("start_interpolation", 0));
      int teleportDuration = input.getIntOr("teleport_duration", 0);
      this.setPosRotInterpolationDuration(Mth.clamp(teleportDuration, 0, 59));
      this.setBillboardConstraints(
         input.<Display$BillboardConstraints>read("billboard", Display$BillboardConstraints.CODEC).orElse(Display$BillboardConstraints.FIXED)
      );
      this.setViewRange(input.getFloatOr("view_range", 1.0F));
      this.setShadowRadius(input.getFloatOr("shadow_radius", 0.0F));
      this.setShadowStrength(input.getFloatOr("shadow_strength", 1.0F));
      this.setWidth(input.getFloatOr("width", 0.0F));
      this.setHeight(input.getFloatOr("height", 0.0F));
      this.setGlowColorOverride(input.getIntOr("glow_color_override", -1));
      this.setBrightnessOverride(input.<Brightness>read("brightness", Brightness.CODEC).orElse(null));
   }

   private void setTransformation(final Transformation transformation) {
      this.entityData.set(DATA_TRANSLATION_ID, transformation.translation());
      this.entityData.set(DATA_LEFT_ROTATION_ID, transformation.leftRotation());
      this.entityData.set(DATA_SCALE_ID, transformation.scale());
      this.entityData.set(DATA_RIGHT_ROTATION_ID, transformation.rightRotation());
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      output.store("transformation", Transformation.EXTENDED_CODEC, createTransformation(this.entityData));
      output.store("billboard", Display$BillboardConstraints.CODEC, this.getBillboardConstraints());
      output.putInt("interpolation_duration", this.getTransformationInterpolationDuration());
      output.putInt("teleport_duration", this.getPosRotInterpolationDuration());
      output.putFloat("view_range", this.getViewRange());
      output.putFloat("shadow_radius", this.getShadowRadius());
      output.putFloat("shadow_strength", this.getShadowStrength());
      output.putFloat("width", this.getWidth());
      output.putFloat("height", this.getHeight());
      output.putInt("glow_color_override", this.getGlowColorOverride());
      output.storeNullable("brightness", Brightness.CODEC, this.getBrightnessOverride());
   }

   @Override
   public AABB getInterpolatedBoundingBox(final float partialTicks) {
      return this.xOld == this.getX() && this.yOld == this.getY() && this.zOld == this.getZ()
         ? this.cullingBoundingBox
         : this.cullingBoundingBox
            .move(
               Mth.lerp((double)partialTicks, this.xOld, this.getX()) - this.getX(),
               Mth.lerp((double)partialTicks, this.yOld, this.getY()) - this.getY(),
               Mth.lerp((double)partialTicks, this.zOld, this.getZ()) - this.getZ()
            );
   }

   public boolean affectedByCulling() {
      return !this.noCulling;
   }

   @Override
   public PushReaction getPistonPushReaction() {
      return PushReaction.IGNORE_ENTITY;
   }

   @Override
   public boolean isIgnoringBlockTriggers() {
      return true;
   }

   @Nullable
   public Display$RenderState renderState() {
      return this.renderState;
   }

   private void setTransformationInterpolationDuration(final int duration) {
      this.entityData.set(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID, duration);
   }

   private int getTransformationInterpolationDuration() {
      return this.entityData.get(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID);
   }

   private void setTransformationInterpolationDelay(final int ticks) {
      this.entityData.set(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID, ticks, true);
   }

   private int getTransformationInterpolationDelay() {
      return this.entityData.get(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID);
   }

   private void setPosRotInterpolationDuration(final int duration) {
      this.entityData.set(DATA_POS_ROT_INTERPOLATION_DURATION_ID, duration);
   }

   private int getPosRotInterpolationDuration() {
      return this.entityData.get(DATA_POS_ROT_INTERPOLATION_DURATION_ID);
   }

   private void setBillboardConstraints(final Display$BillboardConstraints constraints) {
      this.entityData.set(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, constraints.getId());
   }

   private Display$BillboardConstraints getBillboardConstraints() {
      return Display$BillboardConstraints.BY_ID.apply(this.entityData.get(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID));
   }

   private void setBrightnessOverride(@Nullable final Brightness brightness) {
      this.entityData.set(DATA_BRIGHTNESS_OVERRIDE_ID, brightness != null ? brightness.pack() : -1);
   }

   @Nullable
   private Brightness getBrightnessOverride() {
      int value = this.entityData.get(DATA_BRIGHTNESS_OVERRIDE_ID);
      return value != -1 ? Brightness.unpack(value) : null;
   }

   private int getPackedBrightnessOverride() {
      return this.entityData.get(DATA_BRIGHTNESS_OVERRIDE_ID);
   }

   private void setViewRange(final float range) {
      this.entityData.set(DATA_VIEW_RANGE_ID, range);
   }

   private float getViewRange() {
      return this.entityData.get(DATA_VIEW_RANGE_ID);
   }

   private void setShadowRadius(final float size) {
      this.entityData.set(DATA_SHADOW_RADIUS_ID, size);
   }

   private float getShadowRadius() {
      return this.entityData.get(DATA_SHADOW_RADIUS_ID);
   }

   private void setShadowStrength(final float strength) {
      this.entityData.set(DATA_SHADOW_STRENGTH_ID, strength);
   }

   private float getShadowStrength() {
      return this.entityData.get(DATA_SHADOW_STRENGTH_ID);
   }

   private void setWidth(final float width) {
      this.entityData.set(DATA_WIDTH_ID, width);
   }

   private float getWidth() {
      return this.entityData.get(DATA_WIDTH_ID);
   }

   private void setHeight(final float width) {
      this.entityData.set(DATA_HEIGHT_ID, width);
   }

   private int getGlowColorOverride() {
      return this.entityData.get(DATA_GLOW_COLOR_OVERRIDE_ID);
   }

   private void setGlowColorOverride(final int value) {
      this.entityData.set(DATA_GLOW_COLOR_OVERRIDE_ID, value);
   }

   public float calculateInterpolationProgress(final float partialTickTime) {
      int duration = this.interpolationDuration;
      if (duration <= 0) {
         return 1.0F;
      } else {
         float ticksSinceUpdate = (float)(this.tickCount - this.interpolationStartClientTick);
         float partialTicksSinceLastUpdate = ticksSinceUpdate + partialTickTime;
         float result = Mth.clamp(Mth.inverseLerp(partialTicksSinceLastUpdate, 0.0F, (float)duration), 0.0F, 1.0F);
         this.lastProgress = result;
         return result;
      }
   }

   private float getHeight() {
      return this.entityData.get(DATA_HEIGHT_ID);
   }

   @Override
   public void setPos(final double x, final double y, final double z) {
      super.setPos(x, y, z);
      this.updateCulling();
   }

   private void updateCulling() {
      float width = this.getWidth();
      float height = this.getHeight();
      this.noCulling = width == 0.0F || height == 0.0F;
      float w = width / 2.0F;
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      this.cullingBoundingBox = new AABB(x - w, y, z - w, x + w, y + height, z + w);
   }

   @Override
   public boolean shouldRenderAtSqrDistance(final double distanceSqr) {
      return distanceSqr < Mth.square(this.getViewRange() * 64.0 * getViewScale());
   }

   @Override
   public int getTeamColor() {
      int glowColorOverride = this.getGlowColorOverride();
      return glowColorOverride != -1 ? glowColorOverride : super.getTeamColor();
   }

   private Display$RenderState createFreshRenderState() {
      return new Display$RenderState(
         Display$GenericInterpolator.constant(createTransformation(this.entityData)),
         this.getBillboardConstraints(),
         this.getPackedBrightnessOverride(),
         Display$FloatInterpolator.constant(this.getShadowRadius()),
         Display$FloatInterpolator.constant(this.getShadowStrength()),
         this.getGlowColorOverride()
      );
   }

   private Display$RenderState createInterpolatedRenderState(final Display$RenderState previousState, final float progress) {
      Transformation currentTransform = previousState.transformation.get(progress);
      float currentShadowRadius = previousState.shadowRadius.get(progress);
      float currentShadowStrength = previousState.shadowStrength.get(progress);
      return new Display$RenderState(
         new Display$TransformationInterpolator(currentTransform, createTransformation(this.entityData)),
         this.getBillboardConstraints(),
         this.getPackedBrightnessOverride(),
         new Display$LinearFloatInterpolator(currentShadowRadius, this.getShadowRadius()),
         new Display$LinearFloatInterpolator(currentShadowStrength, this.getShadowStrength()),
         this.getGlowColorOverride()
      );
   }
}
