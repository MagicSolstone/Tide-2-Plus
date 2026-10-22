package net.minecraft.util.worldupdate;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum UpgradeProgress$Type implements StringRepresentable {
   FILES("files"),
   LEGACY_STRUCTURES("legacy_structures"),
   REGIONS("regions");

   private final String id;
   private final Component label;

   private UpgradeProgress$Type(final String id) {
      this.id = id;
      this.label = Component.translatable("upgradeWorld.progress.type." + this.getSerializedName());
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }

   public Component label() {
      return this.label;
   }
}
