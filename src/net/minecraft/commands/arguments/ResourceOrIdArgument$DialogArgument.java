package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.dialog.Dialog;

public class ResourceOrIdArgument$DialogArgument extends ResourceOrIdArgument<Dialog> {
   protected ResourceOrIdArgument$DialogArgument(final CommandBuildContext context) {
      super(context, Registries.DIALOG, Dialog.DIRECT_CODEC);
   }
}
