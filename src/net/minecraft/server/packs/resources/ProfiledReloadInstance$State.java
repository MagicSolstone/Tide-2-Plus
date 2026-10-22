package net.minecraft.server.packs.resources;

import java.util.concurrent.atomic.AtomicLong;

public record ProfiledReloadInstance$State(String name, AtomicLong preparationNanos, AtomicLong preparationCount, AtomicLong reloadNanos, AtomicLong reloadCount) {
}
