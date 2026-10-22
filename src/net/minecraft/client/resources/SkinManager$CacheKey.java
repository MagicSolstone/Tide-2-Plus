package net.minecraft.client.resources;

import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record SkinManager$CacheKey(UUID profileId, @Nullable Property packedTextures) {
}
