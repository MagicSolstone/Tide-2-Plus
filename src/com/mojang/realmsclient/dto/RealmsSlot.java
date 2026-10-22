package com.mojang.realmsclient.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class RealmsSlot implements ReflectionBasedSerialization {
   @SerializedName("slotId")
   public int slotId;
   @SerializedName("options")
   @JsonAdapter(RealmsSlot$RealmsWorldOptionsJsonAdapter.class)
   public RealmsWorldOptions options;
   @SerializedName("settings")
   public List<RealmsSetting> settings;

   public RealmsSlot(final int slotId, final RealmsWorldOptions options, final List<RealmsSetting> settings) {
      this.slotId = slotId;
      this.options = options;
      this.settings = settings;
   }

   public static RealmsSlot defaults(final int slotId) {
      return new RealmsSlot(slotId, RealmsWorldOptions.createEmptyDefaults(), List.of(RealmsSetting.hardcoreSetting(false)));
   }

   public RealmsSlot copy() {
      return new RealmsSlot(this.slotId, this.options.copy(), new ArrayList<>(this.settings));
   }

   public boolean isHardcore() {
      return RealmsSetting.isHardcore(this.settings);
   }
}
