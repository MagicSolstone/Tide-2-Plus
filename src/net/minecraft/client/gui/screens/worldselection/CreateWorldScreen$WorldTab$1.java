package net.minecraft.client.gui.screens.worldselection;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
class CreateWorldScreen$WorldTab$1 extends EditBox {
   CreateWorldScreen$WorldTab$1(final CreateWorldScreen$WorldTab this$1, final Font font, final int width, final int height, final Component narration) {
      Objects.requireNonNull(this$1);
      super(font, width, height, narration);
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen$WorldTab.SEED_EMPTY_HINT);
   }
}
