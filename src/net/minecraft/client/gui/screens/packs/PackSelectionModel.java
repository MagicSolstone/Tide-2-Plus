package net.minecraft.client.gui.screens.packs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

@Environment(EnvType.CLIENT)
public class PackSelectionModel {
   private final PackRepository repository;
   private final List<Pack> selected;
   private final List<Pack> unselected;
   private final Function<Pack, Identifier> iconGetter;
   private final Consumer<PackSelectionModel$EntryBase> onListChanged;
   private final Consumer<PackRepository> output;

   public PackSelectionModel(
      final Consumer<PackSelectionModel$EntryBase> onListChanged,
      final Function<Pack, Identifier> iconGetter,
      final PackRepository repository,
      final Consumer<PackRepository> output
   ) {
      this.onListChanged = onListChanged;
      this.iconGetter = iconGetter;
      this.repository = repository;
      this.selected = Lists.newArrayList(repository.getSelectedPacks());
      Collections.reverse(this.selected);
      this.unselected = Lists.newArrayList(repository.getAvailablePacks());
      this.unselected.removeAll(this.selected);
      this.output = output;
   }

   public Stream<PackSelectionModel$Entry> getUnselected() {
      return this.unselected.stream().map(x$0 -> new PackSelectionModel$UnselectedPackEntry(this, x$0));
   }

   public Stream<PackSelectionModel$Entry> getSelected() {
      return this.selected.stream().map(x$0 -> new PackSelectionModel$SelectedPackEntry(this, x$0));
   }

   private void updateRepoSelectedList() {
      this.repository.setSelected(Lists.reverse(this.selected).stream().map(Pack::getId).collect(ImmutableList.toImmutableList()));
   }

   public void commit() {
      this.updateRepoSelectedList();
      this.output.accept(this.repository);
   }

   public void findNewPacks() {
      this.repository.reload();
      this.selected.retainAll(this.repository.getAvailablePacks());
      this.unselected.clear();
      this.unselected.addAll(this.repository.getAvailablePacks());
      this.unselected.removeAll(this.selected);
   }
}
