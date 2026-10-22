package com.mojang.realmsclient.dto;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum WorldTemplate$WorldTemplateType {
   WORLD_TEMPLATE,
   MINIGAME,
   ADVENTUREMAP,
   EXPERIENCE,
   INSPIRATION;
}
