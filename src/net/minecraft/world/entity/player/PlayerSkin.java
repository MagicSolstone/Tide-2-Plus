package net.minecraft.world.entity.player;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.core.ClientAsset$Texture;
import org.jspecify.annotations.Nullable;

public record PlayerSkin(
   ClientAsset$Texture body, @Nullable ClientAsset$Texture cape, @Nullable ClientAsset$Texture elytra, PlayerModelType model, boolean secure
) {
   public static PlayerSkin insecure(
      final ClientAsset$Texture body, @Nullable final ClientAsset$Texture cape, @Nullable final ClientAsset$Texture elytra, final PlayerModelType model
   ) {
      return new PlayerSkin(body, cape, elytra, model, false);
   }

   public PlayerSkin with(final PlayerSkin$Patch patch) {
      return patch.equals(PlayerSkin$Patch.EMPTY)
         ? this
         : insecure(
            (ClientAsset$Texture)DataFixUtils.orElse(patch.body, this.body),
            (ClientAsset$Texture)DataFixUtils.orElse(patch.cape, this.cape),
            (ClientAsset$Texture)DataFixUtils.orElse(patch.elytra, this.elytra),
            patch.model.orElse(this.model)
         );
   }
}
