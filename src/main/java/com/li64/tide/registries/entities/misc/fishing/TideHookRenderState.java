package com.li64.tide.registries.entities.misc.fishing;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

/** One frame's worth of a Tide fishing hook: where the line goes, its colour, and how the hook and bobber look. */
public class TideHookRenderState extends EntityRenderState {
    public boolean hasOwner;
    public Vec3 lineOriginOffset = Vec3.ZERO;
    public int lineColor = 0xFF000000;
    public float lineWidth = 1f;
    public float initialYaw;
    public Identifier hookTexture;
    public Identifier bobberTexture;
    public boolean bobberIsItem;
    public final ItemStackRenderState bobberItem = new ItemStackRenderState();
}
