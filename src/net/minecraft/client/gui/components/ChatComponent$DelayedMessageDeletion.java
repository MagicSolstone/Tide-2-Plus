package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.MessageSignature;

@Environment(EnvType.CLIENT)
record ChatComponent$DelayedMessageDeletion(MessageSignature signature, int deletableAfter) {
}
