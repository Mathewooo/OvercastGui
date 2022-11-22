package gg.overcast.gui.menu;

import gg.overcast.gui.exceptions.MenuManagerException;
import gg.overcast.gui.exceptions.MenuManagerNotSetupException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

@SuppressWarnings("unused")
public class MenuManager {
    private static HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap =
            new HashMap<>();
    protected static LinkedHashMap<UUID, BukkitTask> inventoryTasks =
            new LinkedHashMap<>();
    private static boolean isSetup = false;

    public static void cancelInventoryTask(UUID uuid) {
        if (!inventoryTasks.containsKey(uuid))
            return;
        var task = inventoryTasks
                .get(uuid);
        if (task != null) {
            task.cancel();
            inventoryTasks
                    .remove(uuid);
        }
    }
    private static void registerMenuListener(Server server,
                                             Plugin plugin
    ) {
        boolean isAlreadyRegistered = false;
        for (var registeredListener : InventoryClickEvent.getHandlerList()
                .getRegisteredListeners()
        ) {
            if (registeredListener.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }
        if (!isAlreadyRegistered) server.getPluginManager()
                .registerEvents(
                        new MenuListener(), plugin
                );
    }

    public static void setup(Server server, Plugin plugin) {
        registerMenuListener(
                server, plugin
        );
        isSetup = true;
    }

    public static void openMenu(Class<? extends Menu> menuClass, Player player)
            throws MenuManagerException, MenuManagerNotSetupException
    {
        try {
            menuClass.getConstructor(PlayerMenuUtility.class)
                    .newInstance(
                            getPlayerMenuUtility(player)
                    ).open();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException exception)
        {
            exception.printStackTrace();
            throw new MenuManagerException();
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player)
            throws MenuManagerNotSetupException
    {
        if (!isSetup) throw new MenuManagerNotSetupException();
        if (!(playerMenuUtilityMap.containsKey(player))) {
            var playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(
                    player, playerMenuUtility
            );
            return playerMenuUtility;
        } else return playerMenuUtilityMap.get(player);
    }
}
