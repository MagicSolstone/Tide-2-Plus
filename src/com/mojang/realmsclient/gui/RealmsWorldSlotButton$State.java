package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServer$Compatibility;
import com.mojang.realmsclient.dto.RealmsSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsWorldSlotButton$State {
   private final String slotName;
   private final String slotVersion;
   private final RealmsServer$Compatibility compatibility;
   private final long imageId;
   @Nullable
   private final String image;
   public final boolean empty;
   public final boolean minigame;
   public final RealmsWorldSlotButton$Action action;
   public final boolean hardcore;
   public final boolean activeSlot;

   public RealmsWorldSlotButton$State(final RealmsServer serverData, final int slotIndex) {
      this.minigame = slotIndex == 4;
      if (this.minigame) {
         this.slotName = RealmsWorldSlotButton.MINIGAME.getString();
         this.imageId = serverData.minigameId;
         this.image = serverData.minigameImage;
         this.empty = serverData.minigameId == -1;
         this.slotVersion = "";
         this.compatibility = RealmsServer$Compatibility.UNVERIFIABLE;
         this.hardcore = false;
         this.activeSlot = serverData.isMinigameActive();
      } else {
         RealmsSlot slot = serverData.slots.get(slotIndex);
         this.slotName = slot.options.getSlotName(slotIndex);
         this.imageId = slot.options.templateId;
         this.image = slot.options.templateImage;
         this.empty = slot.options.empty;
         this.slotVersion = slot.options.version;
         this.compatibility = slot.options.compatibility;
         this.hardcore = slot.isHardcore();
         this.activeSlot = serverData.activeSlot == slotIndex && !serverData.isMinigameActive();
      }

      this.action = RealmsWorldSlotButton.getAction(this.activeSlot, this.empty, serverData.expired);
   }
}
