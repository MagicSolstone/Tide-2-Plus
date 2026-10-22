package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.WeatheringCopperCollection$ByState;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class ChestSpecialRenderer implements NoDataSpecialModelRenderer {
   public static final Identifier ENDER_CHEST = Identifier.withDefaultNamespace("ender");
   public static final MultiblockChestResources<Identifier> REGULAR = createDefaultTextures("normal");
   public static final MultiblockChestResources<Identifier> TRAPPED = createDefaultTextures("trapped");
   public static final MultiblockChestResources<Identifier> CHRISTMAS = createDefaultTextures("christmas");
   public static final WeatheringCopperCollection$ByState<MultiblockChestResources<Identifier>> COPPER = new WeatheringCopperCollection$ByState<>(
      createDefaultTextures("copper"),
      createDefaultTextures("copper_exposed"),
      createDefaultTextures("copper_weathered"),
      createDefaultTextures("copper_oxidized")
   );
   private final SpriteGetter sprites;
   private final ChestModel model;
   private final SpriteId sprite;
   private final float openness;

   public ChestSpecialRenderer(final SpriteGetter sprites, final ChestModel model, final SpriteId sprite, final float openness) {
      this.sprites = sprites;
      this.model = model;
      this.sprite = sprite;
      this.openness = openness;
   }

   private static MultiblockChestResources<Identifier> createDefaultTextures(final String prefix) {
      return new MultiblockChestResources<>(
         Identifier.withDefaultNamespace(prefix), Identifier.withDefaultNamespace(prefix + "_left"), Identifier.withDefaultNamespace(prefix + "_right")
      );
   }

   @Override
   public void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      submitNodeCollector.submitModel(this.model, this.openness, poseStack, lightCoords, overlayCoords, -1, this.sprite, this.sprites, outlineColor);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.model.setupAnim(this.openness);
      this.model.root().getExtentsForGui(poseStack, output);
   }
}
