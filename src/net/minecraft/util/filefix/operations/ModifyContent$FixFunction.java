package net.minecraft.util.filefix.operations;

import java.io.IOException;
import net.minecraft.util.worldupdate.UpgradeProgress;

@FunctionalInterface
public interface ModifyContent$FixFunction {
   void run(final UpgradeProgress upgradeProgress) throws IOException;
}
