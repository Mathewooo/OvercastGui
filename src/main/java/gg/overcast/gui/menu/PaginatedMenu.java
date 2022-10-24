package gg.overcast.gui.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public abstract class PaginatedMenu extends Menu {
    protected List<Object> data;
    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    private final int[] borderIndexes = new int[]{17, 18, 26, 27, 35, 36};

    protected PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public abstract List<?> getData();

    public abstract void loopCode(Object object);

    @Nullable
    public abstract HashMap<Integer, ItemStack> getCustomMenuBorderItems();

    protected void addMenuBorder() {
        inventory.setItem(48, makeItem(
                Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"
        ));
        inventory.setItem(49, makeItem(
                Material.BARRIER, ChatColor.DARK_RED + "Close"
        ));
        inventory.setItem(50, makeItem(
                Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"
        ));
        for (int index = 0; index < 10; index++) {
            if (inventory.getItem(index) == null)
                inventory.setItem(
                        index, super.FILLER_GLASS
                );
        }
        for (int i : borderIndexes) inventory.setItem(
                i, super.FILLER_GLASS
        );
        for (int index = 44; index < 54; index++) {
            if (inventory.getItem(index) == null)
                inventory.setItem(
                        index, super.FILLER_GLASS
                );
        }
        if (getCustomMenuBorderItems() != null) {
            getCustomMenuBorderItems()
                    .forEach(
                            inventory::setItem
                    );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setMenuItems() {
        addMenuBorder();
        List<Object> data = (List<Object>) getData();
        if (data != null && !data.isEmpty()) {
            for (int index = 0; index < getMaxItemsPerPage(); index++) {
                index = getMaxItemsPerPage() * page + index;
                Bukkit.getLogger().info(String.valueOf(index));
                if (index >= data.size()) break;
                if (data.get(index) != null)
                    loopCode(
                            data.get(index)
                    );
            }
        }
    }

    public boolean prevPage() {
        if (page == 0) return false;
        else {
            page = page - 1;
            reloadItems();
            return true;
        }
    }

    public boolean nextPage() {
        if (!((index + 1) >= getData().size())) {
            page = page + 1;
            reloadItems();
            return true;
        } else return false;
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}

