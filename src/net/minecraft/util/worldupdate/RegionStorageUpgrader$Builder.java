package net.minecraft.util.worldupdate;

import com.mojang.datafixers.DataFixer;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import org.jspecify.annotations.Nullable;

public class RegionStorageUpgrader$Builder {
   private final DataFixer dataFixer;
   @Nullable
   private DataFixTypes dataFixType;
   @Nullable
   private String type;
   @Nullable
   private String folderName;
   private int defaultVersion = -1;
   private boolean recreateRegionFiles;
   private UpgradeProgress upgradeProgress = new UpgradeProgress$Noop();
   @Nullable
   private CompoundTag dataFixContextTag;
   private Int2ObjectAVLTreeMap<RegionStorageUpgrader$TagModifier> tagModifiers = new Int2ObjectAVLTreeMap();

   public RegionStorageUpgrader$Builder(final DataFixer dataFixer) {
      this.dataFixer = dataFixer;
   }

   public RegionStorageUpgrader$Builder setDataFixType(final DataFixTypes dataFixType) {
      this.dataFixType = dataFixType;
      return this;
   }

   public RegionStorageUpgrader$Builder setTypeAndFolderName(final String name) {
      return this.setType(name).setFolderName(name);
   }

   public RegionStorageUpgrader$Builder setType(final String type) {
      this.type = type;
      return this;
   }

   public RegionStorageUpgrader$Builder setFolderName(final String folderName) {
      this.folderName = folderName;
      return this;
   }

   public RegionStorageUpgrader$Builder setDefaultVersion(final int defaultVersion) {
      this.defaultVersion = defaultVersion;
      return this;
   }

   public RegionStorageUpgrader$Builder setRecreateRegionFiles(final boolean recreateRegionFiles) {
      this.recreateRegionFiles = recreateRegionFiles;
      return this;
   }

   public RegionStorageUpgrader$Builder trackProgress(final UpgradeProgress upgradeProgress) {
      this.upgradeProgress = upgradeProgress;
      return this;
   }

   public RegionStorageUpgrader$Builder setDataFixContextTag(@Nullable final CompoundTag dataFixContextTag) {
      this.dataFixContextTag = dataFixContextTag;
      return this;
   }

   public RegionStorageUpgrader$Builder addTagModifier(final int version, final RegionStorageUpgrader$TagModifier tagModifier) {
      if (this.tagModifiers.containsKey(version)) {
         throw new IllegalStateException("Can't add two fixers for the same data version");
      } else {
         this.tagModifiers.put(version, tagModifier);
         return this;
      }
   }

   private RegionStorageUpgrader$Builder setTagModifiers(final Int2ObjectAVLTreeMap<RegionStorageUpgrader$TagModifier> tagModifiers) {
      this.tagModifiers = tagModifiers;
      return this;
   }

   public RegionStorageUpgrader$Builder copy() {
      return new RegionStorageUpgrader$Builder(this.dataFixer)
         .setDataFixType(this.dataFixType)
         .setType(this.type)
         .setFolderName(this.folderName)
         .setDefaultVersion(this.defaultVersion)
         .setRecreateRegionFiles(this.recreateRegionFiles)
         .trackProgress(this.upgradeProgress)
         .setDataFixContextTag(this.dataFixContextTag)
         .setTagModifiers(this.tagModifiers.clone());
   }

   public RegionStorageUpgrader build(final int previousCopiesFileAmounts) {
      return new RegionStorageUpgrader(
         this.dataFixer,
         Objects.requireNonNull(this.dataFixType),
         Objects.requireNonNull(this.type),
         Objects.requireNonNull(this.folderName),
         this.defaultVersion,
         this.recreateRegionFiles,
         this.upgradeProgress,
         previousCopiesFileAmounts,
         this.dataFixContextTag,
         this.tagModifiers
      );
   }
}
