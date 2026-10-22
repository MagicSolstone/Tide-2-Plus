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
public class RealmsRegion$RealmsRegionJsonAdapter extends TypeAdapter<RealmsRegion> {
   private static final Logger LOGGER = LogUtils.getLogger();

   public void write(final JsonWriter jsonWriter, final RealmsRegion realmsRegion) throws IOException {
      jsonWriter.value(realmsRegion.nameId);
   }

   public RealmsRegion read(final JsonReader jsonReader) throws IOException {
      String nameId = jsonReader.nextString();
      RealmsRegion realmsRegion = RealmsRegion.findByNameId(nameId);
      if (realmsRegion == null) {
         LOGGER.warn("Unsupported RealmsRegion {}", nameId);
         return RealmsRegion.INVALID_REGION;
      } else {
         return realmsRegion;
      }
   }
}
