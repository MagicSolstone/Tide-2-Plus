package net.minecraft.server;

record SuppressedExceptionCollector$LongEntry(long timestampMs, String location, Class<? extends Throwable> cls, String message) {
}
