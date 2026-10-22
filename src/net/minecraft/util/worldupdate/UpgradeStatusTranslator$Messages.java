package net.minecraft.util.worldupdate;

import net.minecraft.network.chat.Component;

public record UpgradeStatusTranslator$Messages(Component upgrading, Component finished) {
   public static UpgradeStatusTranslator$Messages create(final String type) {
      return new UpgradeStatusTranslator$Messages(
         Component.translatable("optimizeWorld.stage.upgrading." + type), Component.translatable("optimizeWorld.stage.finished." + type)
      );
   }

   public Component forStatus(final UpgradeProgress$Status status) {
      return switch (status) {
         case UPGRADING -> this.upgrading;
         case FINISHED -> this.finished;
         default -> throw new IllegalStateException("Invalid Status received: " + status);
      };
   }
}
