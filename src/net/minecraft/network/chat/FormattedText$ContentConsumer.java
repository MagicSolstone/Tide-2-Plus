package net.minecraft.network.chat;

import java.util.Optional;

public interface FormattedText$ContentConsumer<T> {
   Optional<T> accept(final String contents);
}
