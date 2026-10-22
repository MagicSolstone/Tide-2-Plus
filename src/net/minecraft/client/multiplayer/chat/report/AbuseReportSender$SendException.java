package net.minecraft.client.multiplayer.chat.report;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ThrowingComponent;

@Environment(EnvType.CLIENT)
public class AbuseReportSender$SendException extends ThrowingComponent {
   public AbuseReportSender$SendException(final Component component, final Throwable cause) {
      super(component, cause);
   }
}
