package net.minecraft.client.renderer.texture;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record TextureManager$PendingReload(ReloadableTexture texture, CompletableFuture<TextureContents> newContents) {
}
