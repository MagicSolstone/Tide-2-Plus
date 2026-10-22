package net.minecraft.util;

import java.security.Key;

interface Crypt$ByteArrayToKeyFunction<T extends Key> {
   T apply(final byte[] input) throws CryptException;
}
