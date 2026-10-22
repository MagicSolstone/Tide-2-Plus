package net.minecraft.util.filefix.operations;

import net.minecraft.util.filefix.access.FileAccessProvider;

@FunctionalInterface
public interface ModifyContent$FileAccessFunction {
   ModifyContent$FixFunction make(final FileAccessProvider fileAccessProvider);
}
