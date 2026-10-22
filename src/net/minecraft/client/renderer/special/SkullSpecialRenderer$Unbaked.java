package net.minecraft.client.renderer.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SkullBlock$Type;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record SkullSpecialRenderer$Unbaked(SkullBlock$Type kind, Optional<Identifier> textureOverride, float animation)
   implements NoDataSpecialModelRenderer$Unbaked {
   public static final MapCodec<SkullSpecialRenderer$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            SkullBlock$Type.CODEC.fieldOf("kind").forGetter(SkullSpecialRenderer$Unbaked::kind),
            Identifier.CODEC.optionalFieldOf("texture").forGetter(SkullSpecialRenderer$Unbaked::textureOverride),
            Codec.FLOAT.optionalFieldOf("animation", 0.0F).forGetter(SkullSpecialRenderer$Unbaked::animation)
         )
         .apply(i, SkullSpecialRenderer$Unbaked::new)
   );

   public SkullSpecialRenderer$Unbaked(final SkullBlock$Type kind) {
      this(kind, Optional.empty(), 0.0F);
   }

   @Override
   public MapCodec<SkullSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   @Nullable
   public SkullSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      SkullModelBase model = SkullBlockRenderer.createModel(context.entityModelSet(), this.kind);
      Identifier textureOverride = this.textureOverride.<Identifier>map(t -> t.withPath(p -> "textures/entity/" + p + ".png")).orElse(null);
      if (model == null) {
         return null;
      } else {
         RenderType renderType = SkullBlockRenderer.getSkullRenderType(this.kind, textureOverride);
         return new SkullSpecialRenderer(model, this.animation, renderType);
      }
   }
}
