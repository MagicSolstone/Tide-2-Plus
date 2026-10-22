package net.minecraft.client.renderer.item.properties.select;

import com.ibm.icu.util.TimeZone;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record LocalTime$Data(String format, String localeId, Optional<TimeZone> timeZone) {
}
