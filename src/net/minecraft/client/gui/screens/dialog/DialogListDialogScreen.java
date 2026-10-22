package net.minecraft.client.gui.screens.dialog;

import java.util.Optional;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent$ShowDialog;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.DialogListDialog;
import net.minecraft.server.dialog.action.StaticAction;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DialogListDialogScreen extends ButtonListDialogScreen<DialogListDialog> {
   public DialogListDialogScreen(@Nullable final Screen previousScreen, final DialogListDialog dialog, final DialogConnectionAccess connectionAccess) {
      super(previousScreen, dialog, connectionAccess);
   }

   protected Stream<ActionButton> createListActions(final DialogListDialog data, final DialogConnectionAccess connectionAccess) {
      return data.dialogs().stream().map(subDialog -> createDialogClickAction(data, (Holder<Dialog>)subDialog));
   }

   private static ActionButton createDialogClickAction(final DialogListDialog data, final Holder<Dialog> subDialog) {
      return new ActionButton(
         new CommonButtonData(subDialog.value().common().computeExternalTitle(), data.buttonWidth()),
         Optional.of(new StaticAction(new ClickEvent$ShowDialog(subDialog)))
      );
   }
}
