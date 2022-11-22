package gg.overcast.gui.menu;

import gg.overcast.gui.exceptions.MenuManagerException;
import gg.overcast.gui.exceptions.MenuManagerNotSetupException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {
    private void notSetUp() {
        Bukkit.getLogger().severe(
                "The menu manager hasn't been configured! " +
                        "Call MenuManager.setup() at the main thread!"
        );
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        var holder = event.getInventory()
                .getHolder();
        if (holder instanceof Menu menu) {
            if (event.getCurrentItem() == null) return;
            if (menu.cancelAllClicks() || event.isShiftClick()) {
                event.setCancelled(true);
            }
            try {
                menu.handleMenu(event);
            } catch (MenuManagerNotSetupException menuManagerNotSetupException)
            { notSetUp(); }
            catch (MenuManagerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onMenuClick(InventoryCloseEvent event) {
        var holder = event.getInventory()
                .getHolder();
        if (holder instanceof Menu menu) {
            MenuManager.cancelInventoryTask(
                    event.getPlayer().getUniqueId()
            );
            try {
                menu.handleClosedMenu(event);
            } catch (MenuManagerNotSetupException menuManagerNotSetupException)
            { notSetUp(); }
            catch (MenuManagerException exception) {
                exception.printStackTrace();
            }
        }
    }
}

