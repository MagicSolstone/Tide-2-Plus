package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.dialog.Dialog;

public record ClickEvent$ShowDialog(Holder<Dialog> dialog) implements ClickEvent {
   public static final MapCodec<ClickEvent$ShowDialog> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Dialog.CODEC.fieldOf("dialog").forGetter(ClickEvent$ShowDialog::dialog)).apply(i, ClickEvent$ShowDialog::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.SHOW_DIALOG;
   }
}
