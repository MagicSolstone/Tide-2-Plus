package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum CompareOp {
   ALWAYS_PASS,
   LESS_THAN,
   LESS_THAN_OR_EQUAL,
   EQUAL,
   NOT_EQUAL,
   GREATER_THAN_OR_EQUAL,
   GREATER_THAN,
   NEVER_PASS;
}
