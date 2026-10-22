package com.mojang.blaze3d.platform;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.macosx.ObjCRuntime;

@Environment(EnvType.CLIENT)
final class MacosUtil$DisabledActions {
   private static final Set<Long> SELECTORS = Set.of(ObjCRuntime.sel_getUid("performClose:"), ObjCRuntime.sel_getUid("closeAll:"));

   private MacosUtil$DisabledActions() {
   }
}
