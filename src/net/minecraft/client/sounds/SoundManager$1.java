package net.minecraft.client.sounds;

import com.google.gson.reflect.TypeToken;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundEventRegistration;

@Environment(EnvType.CLIENT)
class SoundManager$1 extends TypeToken<Map<String, SoundEventRegistration>> {
}
