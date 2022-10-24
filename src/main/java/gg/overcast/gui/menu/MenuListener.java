package gg.overcast.gui.menu;

import gg.overcast.gui.exceptions.MenuManagerException;
import gg.overcast.gui.exceptions.MenuManagerNotSetupException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

@SuppressWarnings("unused")
public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory()
                .getHolder();
        if (holder instanceof Menu menu) {
            if (event.getCurrentItem() == null) return;
            if (menu.cancelAllClicks() || event.isShiftClick()) {
                event.setCancelled(true);
            }
            try {
                menu.handleMenu(event);
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                Bukkit.getLogger().info(
                        ChatColor.RED +
                                "The menu manager hasn't been configured! " +
                                "Call MenuManager.setup() at main thread."
                );
            } catch (MenuManagerException exception) {
                exception.printStackTrace();
            }
        }
    }
}

