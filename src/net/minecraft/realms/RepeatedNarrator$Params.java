package net.minecraft.realms;

import com.google.common.util.concurrent.RateLimiter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
record RepeatedNarrator$Params(Component narration, RateLimiter rateLimiter) {
}
