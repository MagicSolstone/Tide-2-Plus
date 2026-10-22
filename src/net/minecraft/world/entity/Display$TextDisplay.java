package net.minecraft.world.entity;

import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Display$TextDisplay extends Display {
   public static final String TAG_TEXT = "text";
   private static final String TAG_LINE_WIDTH = "line_width";
   private static final String TAG_TEXT_OPACITY = "text_opacity";
   private static final String TAG_BACKGROUND_COLOR = "background";
   private static final String TAG_SHADOW = "shadow";
   private static final String TAG_SEE_THROUGH = "see_through";
   private static final String TAG_USE_DEFAULT_BACKGROUND = "default_background";
   private static final String TAG_ALIGNMENT = "alignment";
   public static final byte FLAG_SHADOW = 1;
   public static final byte FLAG_SEE_THROUGH = 2;
   public static final byte FLAG_USE_DEFAULT_BACKGROUND = 4;
   public static final byte FLAG_ALIGN_LEFT = 8;
   public static final byte FLAG_ALIGN_RIGHT = 16;
   private static final byte INITIAL_TEXT_OPACITY = -1;
   public static final int INITIAL_BACKGROUND = 1073741824;
   private static final int INITIAL_LINE_WIDTH = 200;
   private static final EntityDataAccessor<Component> DATA_TEXT_ID = SynchedEntityData.defineId(Display$TextDisplay.class, EntityDataSerializers.COMPONENT);
   private static final EntityDataAccessor<Integer> DATA_LINE_WIDTH_ID = SynchedEntityData.defineId(Display$TextDisplay.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> DATA_BACKGROUND_COLOR_ID = SynchedEntityData.defineId(Display$TextDisplay.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Byte> DATA_TEXT_OPACITY_ID = SynchedEntityData.defineId(Display$TextDisplay.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Byte> DATA_STYLE_FLAGS_ID = SynchedEntityData.defineId(Display$TextDisplay.class, EntityDataSerializers.BYTE);
   private static final IntSet TEXT_RENDER_STATE_IDS = IntSet.of(
      new int[]{DATA_TEXT_ID.id(), DATA_LINE_WIDTH_ID.id(), DATA_BACKGROUND_COLOR_ID.id(), DATA_TEXT_OPACITY_ID.id(), DATA_STYLE_FLAGS_ID.id()}
   );
   @Nullable
   private Display$TextDisplay$CachedInfo clientDisplayCache;
   @Nullable
   private Display$TextDisplay$TextRenderState textRenderState;

   public Display$TextDisplay(final EntityType<?> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_TEXT_ID, Component.empty());
      entityData.define(DATA_LINE_WIDTH_ID, 200);
      entityData.define(DATA_BACKGROUND_COLOR_ID, 1073741824);
      entityData.define(DATA_TEXT_OPACITY_ID, (byte)-1);
      entityData.define(DATA_STYLE_FLAGS_ID, (byte)0);
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (TEXT_RENDER_STATE_IDS.contains(accessor.id())) {
         this.updateRenderState = true;
      }
   }

   private Component getText() {
      return this.entityData.get(DATA_TEXT_ID);
   }

   private void setText(final Component text) {
      this.entityData.set(DATA_TEXT_ID, text);
   }

   private int getLineWidth() {
      return this.entityData.get(DATA_LINE_WIDTH_ID);
   }

   private void setLineWidth(final int width) {
      this.entityData.set(DATA_LINE_WIDTH_ID, width);
   }

   private byte getTextOpacity() {
      return this.entityData.get(DATA_TEXT_OPACITY_ID);
   }

   private void setTextOpacity(final byte opacity) {
      this.entityData.set(DATA_TEXT_OPACITY_ID, opacity);
   }

   private int getBackgroundColor() {
      return this.entityData.get(DATA_BACKGROUND_COLOR_ID);
   }

   private void setBackgroundColor(final int color) {
      this.entityData.set(DATA_BACKGROUND_COLOR_ID, color);
   }

   private byte getFlags() {
      return this.entityData.get(DATA_STYLE_FLAGS_ID);
   }

   private void setFlags(final byte flags) {
      this.entityData.set(DATA_STYLE_FLAGS_ID, flags);
   }

   private static byte loadFlag(final byte flags, final ValueInput input, final String id, final byte mask) {
      return input.getBooleanOr(id, false) ? (byte)(flags | mask) : flags;
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setLineWidth(input.getIntOr("line_width", 200));
      this.setTextOpacity(input.getByteOr("text_opacity", (byte)-1));
      this.setBackgroundColor(input.getIntOr("background", 1073741824));
      byte flags = loadFlag((byte)0, input, "shadow", (byte)1);
      flags = loadFlag(flags, input, "see_through", (byte)2);
      flags = loadFlag(flags, input, "default_background", (byte)4);
      Optional<Display$TextDisplay$Align> alignment = input.read("alignment", Display$TextDisplay$Align.CODEC);
      if (alignment.isPresent()) {
         flags = switch ((Display$TextDisplay$Align)alignment.get()) {
            case CENTER -> flags;
            case LEFT -> (byte)(flags | 8);
            case RIGHT -> (byte)(flags | 16);
         };
      }

      this.setFlags(flags);
      Optional<Component> text = input.read("text", ComponentSerialization.CODEC);
      if (text.isPresent()) {
         try {
            if (this.level() instanceof ServerLevel serverLevel) {
               CommandSourceStack context = this.createCommandSourceStackForNameResolution(serverLevel).withPermission(LevelBasedPermissionSet.GAMEMASTER);
               Component resolvedText = ComponentUtils.resolve(ResolutionContext.create(context), text.get());
               this.setText(resolvedText);
            } else {
               this.setText(Component.empty());
            }
         } catch (Exception var8) {
            Display.LOGGER.warn("Failed to parse display entity text {}", text, var8);
         }
      }
   }

   private static void storeFlag(final byte flags, final ValueOutput output, final String id, final byte mask) {
      output.putBoolean(id, (flags & mask) != 0);
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("text", ComponentSerialization.CODEC, this.getText());
      output.putInt("line_width", this.getLineWidth());
      output.putInt("background", this.getBackgroundColor());
      output.putByte("text_opacity", this.getTextOpacity());
      byte flags = this.getFlags();
      storeFlag(flags, output, "shadow", (byte)1);
      storeFlag(flags, output, "see_through", (byte)2);
      storeFlag(flags, output, "default_background", (byte)4);
      output.store("alignment", Display$TextDisplay$Align.CODEC, getAlign(flags));
   }

   @Override
   protected void updateRenderSubState(final boolean shouldInterpolate, final float progress) {
      if (shouldInterpolate && this.textRenderState != null) {
         this.textRenderState = this.createInterpolatedTextRenderState(this.textRenderState, progress);
      } else {
         this.textRenderState = this.createFreshTextRenderState();
      }

      this.clientDisplayCache = null;
   }

   @Nullable
   public Display$TextDisplay$TextRenderState textRenderState() {
      return this.textRenderState;
   }

   private Display$TextDisplay$TextRenderState createFreshTextRenderState() {
      return new Display$TextDisplay$TextRenderState(
         this.getText(),
         this.getLineWidth(),
         Display$IntInterpolator.constant(this.getTextOpacity()),
         Display$IntInterpolator.constant(this.getBackgroundColor()),
         this.getFlags()
      );
   }

   private Display$TextDisplay$TextRenderState createInterpolatedTextRenderState(final Display$TextDisplay$TextRenderState previous, final float progress) {
      int currentBackground = previous.backgroundColor.get(progress);
      int currentOpacity = previous.textOpacity.get(progress);
      return new Display$TextDisplay$TextRenderState(
         this.getText(),
         this.getLineWidth(),
         new Display$LinearIntInterpolator(currentOpacity, this.getTextOpacity()),
         new Display$ColorInterpolator(currentBackground, this.getBackgroundColor()),
         this.getFlags()
      );
   }

   public Display$TextDisplay$CachedInfo cacheDisplay(final Display$TextDisplay$LineSplitter splitter) {
      if (this.clientDisplayCache == null) {
         if (this.textRenderState != null) {
            this.clientDisplayCache = splitter.split(this.textRenderState.text(), this.textRenderState.lineWidth());
         } else {
            this.clientDisplayCache = new Display$TextDisplay$CachedInfo(List.of(), 0);
         }
      }

      return this.clientDisplayCache;
   }

   public static Display$TextDisplay$Align getAlign(final byte flags) {
      if ((flags & 8) != 0) {
         return Display$TextDisplay$Align.LEFT;
      } else {
         return (flags & 16) != 0 ? Display$TextDisplay$Align.RIGHT : Display$TextDisplay$Align.CENTER;
      }
   }
}
