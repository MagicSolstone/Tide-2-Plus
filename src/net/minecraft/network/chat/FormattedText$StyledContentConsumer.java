package net.minecraft.network.chat;

import java.util.Optional;

public interface FormattedText$StyledContentConsumer<T> {
   Optional<T> accept(final Style style, final String contents);
}
