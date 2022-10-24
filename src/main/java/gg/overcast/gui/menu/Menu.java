package gg.overcast.gui.menu;

import gg.overcast.gui.colors.ColorTranslator;
import gg.overcast.gui.exceptions.MenuManagerException;
import gg.overcast.gui.exceptions.MenuManagerNotSetupException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class Menu implements InventoryHolder {
    protected PlayerMenuUtility playerMenuUtility;
    protected Player player;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(
            Material.GRAY_STAINED_GLASS_PANE, " "
    );

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
        this.player = playerMenuUtility.getOwner();
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract boolean cancelAllClicks();

    public abstract void handleMenu(InventoryClickEvent event)
            throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        playerMenuUtility.getOwner().openInventory(inventory);
        playerMenuUtility.pushMenu(this);
    }

    public void back() throws MenuManagerException, MenuManagerNotSetupException {
        MenuManager.openMenu(
                playerMenuUtility.lastMenu().getClass(), playerMenuUtility.getOwner()
        );
    }

    protected void reloadItems() {
        for (int index = 0; index < inventory.getSize(); index++)
            inventory.setItem(index, null);
        setMenuItems();
    }

    protected void reload() throws MenuManagerException, MenuManagerNotSetupException {
        player.closeInventory();
        MenuManager.openMenu(this.getClass(), player);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass() {
        for (int index = 0; index < getSlots(); index++)
            if (inventory.getItem(index) == null) inventory.setItem(index, FILLER_GLASS);
    }

    public void setFillerGlass(ItemStack itemStack) {
        for (int index = 0; index < getSlots(); index++)
            if (inventory.getItem(index) == null) inventory.setItem(index, itemStack);
    }

    public ItemStack makeItem(Material material, String displayName, String... lore) {
        return getItemStack(material, displayName, lore);
    }

    public static ItemStack getItemStack(Material material, String displayName, String[] lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.stream(lore)
                .map(
                        ColorTranslator::translateColorCodes
                ).collect(Collectors.toList()));
        item.setItemMeta(itemMeta);
        return item;
    }
}

