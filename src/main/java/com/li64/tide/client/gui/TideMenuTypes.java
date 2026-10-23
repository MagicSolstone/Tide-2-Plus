package com.li64.tide.client.gui;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.menus.AnglingTableMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.HashMap;

public class TideMenuTypes {
    public static final HashMap<String, MenuType<?>> MENU_TYPES = new HashMap<>();

    public static final MenuType<AnglingTableMenu> ANGLING_TABLE = register("angling_table",
            Tide.PLATFORM.createMenuType(AnglingTableMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final MenuType<com.li64.tide.registries.items.TackleBoxItem.Menu> TACKLE_BOX = register("tackle_box",
            Tide.PLATFORM.createMenuType(com.li64.tide.registries.items.TackleBoxItem.Menu::new, FeatureFlags.DEFAULT_FLAGS));

    public static <T extends AbstractContainerMenu> MenuType<T> register(String key, MenuType<T> menuType) {
        MENU_TYPES.put(key, menuType);
        return Tide.PLATFORM.register(BuiltInRegistries.MENU, Tide.resource(key), menuType);
    }

    public static void init() {}
}
