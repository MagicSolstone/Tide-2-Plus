package net.minecraft.client.gui.screens.telemetry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
record TelemetryEventWidget$Content(Layout container, Component narration) {
}
