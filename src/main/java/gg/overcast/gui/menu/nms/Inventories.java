package gg.overcast.gui.menu.nms;

import net.minecraft.world.inventory.MenuType;

@SuppressWarnings("rawtypes")
enum Inventories {
    NORMAL(9 * 4, MenuType.GENERIC_9x4);
    private final int size;
    private final MenuType menuType;

    Inventories(int size, MenuType menuType) {
        this.size = size;
        this.menuType = menuType;
    }

    public int getSize() {
        return size;
    }

    public MenuType getMenuType() {
        return menuType;
    }
}
