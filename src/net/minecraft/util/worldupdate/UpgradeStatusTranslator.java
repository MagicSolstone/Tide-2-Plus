package net.minecraft.util.worldupdate;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;

public class UpgradeStatusTranslator {
   private final Map<DataFixTypes, UpgradeStatusTranslator$Messages> messages = Util.make(new EnumMap<>(DataFixTypes.class), map -> {
      map.put(DataFixTypes.CHUNK, UpgradeStatusTranslator$Messages.create("chunks"));
      map.put(DataFixTypes.ENTITY_CHUNK, UpgradeStatusTranslator$Messages.create("entities"));
      map.put(DataFixTypes.POI_CHUNK, UpgradeStatusTranslator$Messages.create("poi"));
   });
   private static final Component FAILED = Component.translatable("optimizeWorld.stage.failed");
   private static final Component COUNTING = Component.translatable("optimizeWorld.stage.counting");
   private static final Component UPGRADING = Component.translatable("optimizeWorld.stage.upgrading");

   public Component translate(final UpgradeProgress upgradeProgress) {
      UpgradeProgress$Status status = upgradeProgress.getStatus();
      if (status == UpgradeProgress$Status.FAILED) {
         return FAILED;
      } else if (status == UpgradeProgress$Status.COUNTING) {
         return COUNTING;
      } else {
         DataFixTypes dataFixType = upgradeProgress.getDataFixType();
         if (dataFixType == null) {
            return COUNTING;
         } else {
            UpgradeStatusTranslator$Messages typeMessages = this.messages.get(dataFixType);
            return typeMessages == null ? UPGRADING : typeMessages.forStatus(status);
         }
      }
   }
}
