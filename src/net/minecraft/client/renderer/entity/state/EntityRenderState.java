package net.minecraft.client.renderer.entity.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReportCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EntityRenderState {
   public static final int NO_OUTLINE = 0;
   public EntityType<?> entityType;
   public double x;
   public double y;
   public double z;
   public float ageInTicks;
   public float boundingBoxWidth;
   public float boundingBoxHeight;
   public float eyeHeight;
   public double distanceToCameraSq;
   public boolean isInvisible;
   public boolean isDiscrete;
   public boolean displayFireAnimation;
   public int lightCoords = 15728880;
   public int outlineColor = 0;
   @Nullable
   public Vec3 passengerOffset;
   @Nullable
   public Component nameTag;
   @Nullable
   public Component scoreText;
   @Nullable
   public Vec3 nameTagAttachment;
   @Nullable
   public List<EntityRenderState$LeashState> leashStates;
   public float shadowRadius;
   public final List<EntityRenderState$ShadowPiece> shadowPieces = new ArrayList<>();

   public boolean appearsGlowing() {
      return this.outlineColor != 0;
   }

   public void fillCrashReportCategory(final CrashReportCategory category) {
      category.setDetail("EntityRenderState", this.getClass().getCanonicalName());
      category.setDetail("Entity's Exact location", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", this.x, this.y, this.z));
   }
}
