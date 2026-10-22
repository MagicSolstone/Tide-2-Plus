package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.entity.SignText;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SignRenderState extends BlockEntityRenderState {
   @Nullable
   public SignText frontText;
   @Nullable
   public SignText backText;
   public int textLineHeight;
   public int maxTextLineWidth;
   public boolean isTextFilteringEnabled;
   public boolean drawOutline;
   public SignRenderState$SignTransformations transformations = SignRenderState$SignTransformations.IDENTITY;
}
