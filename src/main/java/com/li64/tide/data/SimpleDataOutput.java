package com.li64.tide.data;

import com.li64.tide.Tide;
import net.minecraft.resources.Identifier;

public interface SimpleDataOutput<T> {
    default void accept(String path, T data) {
        accept(Tide.resource(path), data);
    }

    void accept(Identifier path, T data);
}
