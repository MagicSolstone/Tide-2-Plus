package com.mojang.realmsclient.dto;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ServiceQuality$RealmsServiceQualityJsonAdapter extends TypeAdapter<ServiceQuality> {
   private static final Logger LOGGER = LogUtils.getLogger();

   public void write(final JsonWriter jsonWriter, final ServiceQuality quality) throws IOException {
      jsonWriter.value(quality.value);
   }

   public ServiceQuality read(final JsonReader jsonReader) throws IOException {
      int value = jsonReader.nextInt();
      ServiceQuality quality = ServiceQuality.byValue(value);
      if (quality == null) {
         LOGGER.warn("Unsupported ServiceQuality {}", value);
         return ServiceQuality.UNKNOWN;
      } else {
         return quality;
      }
   }
}
