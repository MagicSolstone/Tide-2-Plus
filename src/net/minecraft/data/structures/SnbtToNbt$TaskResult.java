package net.minecraft.data.structures;

import com.google.common.hash.HashCode;

record SnbtToNbt$TaskResult(String name, byte[] payload, HashCode hash) {
}
