package net.minecraft.client.gui.screens.packs;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;

@Environment(EnvType.CLIENT)
public abstract class PackSelectionModel$EntryBase implements PackSelectionModel$Entry {
   private final Pack pack;

   public PackSelectionModel$EntryBase(final PackSelectionModel this$0, final Pack pack) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.pack = pack;
   }

   protected abstract List<Pack> getSelfList();

   protected abstract List<Pack> getOtherList();

   @Override
   public Identifier getIconTexture() {
      return this.this$0.iconGetter.apply(this.pack);
   }

   @Override
   public PackCompatibility getCompatibility() {
      return this.pack.getCompatibility();
   }

   @Override
   public String getId() {
      return this.pack.getId();
   }

   @Override
   public Component getTitle() {
      return this.pack.getTitle();
   }

   @Override
   public Component getDescription() {
      return this.pack.getDescription();
   }

   @Override
   public PackSource getPackSource() {
      return this.pack.getPackSource();
   }

   @Override
   public boolean isFixedPosition() {
      return this.pack.isFixedPosition();
   }

   @Override
   public boolean isRequired() {
      return this.pack.isRequired();
   }

   protected void toggleSelection() {
      this.getSelfList().remove(this.pack);
      this.pack.getDefaultPosition().insert(this.getOtherList(), this.pack, Pack::selectionConfig, true);
      this.this$0.onListChanged.accept(this);
      this.this$0.updateRepoSelectedList();
      this.updateHighContrastOptionInstance();
   }

   private void updateHighContrastOptionInstance() {
      if (this.pack.getId().equals("high_contrast")) {
         OptionInstance<Boolean> highContrastMode = Minecraft.getInstance().options.highContrast();
         highContrastMode.set(!highContrastMode.get());
      }
   }

   protected void move(final int direction) {
      List<Pack> list = this.getSelfList();
      int currentPos = list.indexOf(this.pack);
      list.remove(currentPos);
      list.add(currentPos + direction, this.pack);
      this.this$0.onListChanged.accept(this);
   }

   @Override
   public boolean canMoveUp() {
      List<Pack> list = this.getSelfList();
      int index = list.indexOf(this.pack);
      return index > 0 && !list.get(index - 1).isFixedPosition();
   }

   @Override
   public void moveUp() {
      this.move(-1);
   }

   @Override
   public boolean canMoveDown() {
      List<Pack> list = this.getSelfList();
      int index = list.indexOf(this.pack);
      return index >= 0 && index < list.size() - 1 && !list.get(index + 1).isFixedPosition();
   }

   @Override
   public void moveDown() {
      this.move(1);
   }
}
