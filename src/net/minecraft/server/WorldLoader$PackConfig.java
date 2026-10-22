package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.level.WorldDataConfiguration;

public record WorldLoader$PackConfig(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean safeMode, boolean initMode) {
   public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
      WorldDataConfiguration newPackConfig = MinecraftServer.configurePackRepository(this.packRepository, this.initialDataConfig, this.initMode, this.safeMode);
      List<PackResources> openedPacks = this.packRepository.openAllSelected();
      CloseableResourceManager resources = new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
      return Pair.of(newPackConfig, resources);
   }
}
