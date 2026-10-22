package net.minecraft.client.gui.screens.worldselection;

import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record WorldCreationUiState$WorldTypeEntry(@Nullable Holder<WorldPreset> preset) {
   private static final Component CUSTOM_WORLD_DESCRIPTION = Component.translatable("generator.custom");

   public Component describePreset() {
      return Optional.ofNullable(this.preset)
         .flatMap(Holder::unwrapKey)
         .map(key -> Component.translatable(key.identifier().toLanguageKey("generator")))
         .orElse(CUSTOM_WORLD_DESCRIPTION);
   }

   public boolean isAmplified() {
      return Optional.ofNullable(this.preset).flatMap(Holder::unwrapKey).filter(k -> k.equals(WorldPresets.AMPLIFIED)).isPresent();
   }
}
