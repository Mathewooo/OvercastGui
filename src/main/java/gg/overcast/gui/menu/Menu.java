package gg.overcast.gui.menu;

import gg.overcast.gui.colors.ColorTranslator;
import gg.overcast.gui.exceptions.MenuManagerException;
import gg.overcast.gui.exceptions.MenuManagerNotSetupException;
import gg.overcast.gui.menu.nms.OpenScreen;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static gg.overcast.gui.menu.MenuManager.inventoryTasks;

@SuppressWarnings("unused")
public abstract class Menu implements InventoryHolder {
    protected PlayerMenuUtility playerMenuUtility;
    protected Player player;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(
            Material.GRAY_STAINED_GLASS_PANE, ""
    );

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
        this.player = playerMenuUtility.getOwner();
    }

    public abstract JavaPlugin getMainInstance();

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract boolean cancelAllClicks();

    public abstract void handleMenu(InventoryClickEvent event)
            throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void handleClosedMenu(InventoryCloseEvent event)
            throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(
                this, getSlots(), getMenuName()
        );
        this.setMenuItems();
        playerMenuUtility.getOwner().openInventory(inventory);
        playerMenuUtility.pushMenu(this);
    }

    protected void reload()
            throws MenuManagerException, MenuManagerNotSetupException
    {
        player.closeInventory();
        MenuManager.openMenu(this.getClass(), player);
    }

    public void back()
            throws MenuManagerException, MenuManagerNotSetupException
    {
        MenuManager.openMenu(
                playerMenuUtility.lastMenu().getClass(),
                playerMenuUtility.getOwner()
        );
    }

    protected void reloadItems() {
        for (int index = 0; index < inventory.getSize(); index++) {
            inventory.setItem(index, null);
        }
        setMenuItems();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFiller(ItemStack itemStack) {
        for (int index = 0; index < getSlots(); index++) {
            if (inventory.getItem(index)
                    == null
            ) {
                inventory.setItem(
                        index, itemStack
                );
            }
        }
    }

    public void titleLoop(long delay,
                          String... titles
    ) {
        AtomicInteger index = new AtomicInteger(0);
        final var length = titles.length;
        var task = Bukkit.getScheduler().runTaskTimerAsynchronously(
                getMainInstance(), () -> {
                    if (index.get() == length)
                        index.set(0);
                    if (index.get() < length) {
                        OpenScreen.getIn().changeTitle(
                                titles[index.get()], player
                        );
                        index.addAndGet(1);
                    }
                }, delay, delay
        );
        inventoryTasks.put(
                player.getUniqueId(), task
        );
    }

    public ItemStack makeItem(Material material,
                              String displayName,
                              String... lore
    ) {
        var item = new ItemStack(material);
        var itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.stream(lore)
                .map(
                        ColorTranslator::translateColorCodes
                ).collect(Collectors.toList()));
        item.setItemMeta(itemMeta);
        return item;
    }
}

