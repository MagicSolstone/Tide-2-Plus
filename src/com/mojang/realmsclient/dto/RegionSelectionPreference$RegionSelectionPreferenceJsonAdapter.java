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
public class RegionSelectionPreference$RegionSelectionPreferenceJsonAdapter extends TypeAdapter<RegionSelectionPreference> {
   private static final Logger LOGGER = LogUtils.getLogger();

   public void write(final JsonWriter jsonWriter, final RegionSelectionPreference regionSelectionPreference) throws IOException {
      jsonWriter.value(regionSelectionPreference.id);
   }

   public RegionSelectionPreference read(final JsonReader jsonReader) throws IOException {
      int id = jsonReader.nextInt();

      for (RegionSelectionPreference value : RegionSelectionPreference.values()) {
         if (value.id == id) {
            return value;
         }
      }

      LOGGER.warn("Unsupported RegionSelectionPreference {}", id);
      return RegionSelectionPreference.DEFAULT_SELECTION;
   }
}
