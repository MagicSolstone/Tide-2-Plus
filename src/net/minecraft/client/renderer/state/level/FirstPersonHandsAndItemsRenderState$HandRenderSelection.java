package net.minecraft.client.renderer.state.level;

import com.google.common.annotations.VisibleForTesting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.InteractionHand;

@Environment(EnvType.CLIENT)
@VisibleForTesting
public enum FirstPersonHandsAndItemsRenderState$HandRenderSelection {
   RENDER_BOTH_HANDS(true, true),
   RENDER_MAIN_HAND_ONLY(true, false),
   RENDER_OFF_HAND_ONLY(false, true);

   public final boolean renderMainHand;
   public final boolean renderOffHand;

   private FirstPersonHandsAndItemsRenderState$HandRenderSelection(final boolean renderMainHand, final boolean renderOffHand) {
      this.renderMainHand = renderMainHand;
      this.renderOffHand = renderOffHand;
   }

   public static FirstPersonHandsAndItemsRenderState$HandRenderSelection onlyForHand(final InteractionHand hand) {
      return hand == InteractionHand.MAIN_HAND ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
   }
}
