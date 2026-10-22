package com.mojang.realmsclient.dto;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RealmsSlot$RealmsWorldOptionsJsonAdapter extends TypeAdapter<RealmsWorldOptions> {
   private RealmsSlot$RealmsWorldOptionsJsonAdapter() {
   }

   public void write(final JsonWriter jsonWriter, final RealmsWorldOptions realmsSlotOptions) throws IOException {
      jsonWriter.jsonValue(new GuardedSerializer().toJson(realmsSlotOptions));
   }

   public RealmsWorldOptions read(final JsonReader jsonReader) throws IOException {
      String json = jsonReader.nextString();
      return RealmsWorldOptions.parse(new GuardedSerializer(), json);
   }
}
