package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface SignatureUpdater$Output {
   void update(byte[] payload) throws SignatureException;
}
