package net.minecraft.client.data.models.blockstates;

import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.client.renderer.block.dispatch.multipart.Selector;

@Environment(EnvType.CLIENT)
record MultiPartGenerator$Entry(Optional<Condition> condition, MultiVariant variants) {
   public Selector toUnbaked() {
      return new Selector(this.condition, this.variants.toUnbaked());
   }
}
