package net.minecraft.server.packs;

import java.io.InputStream;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;

@FunctionalInterface
public interface PackResources$ResourceOutput extends BiConsumer<Identifier, IoSupplier<InputStream>> {
}
