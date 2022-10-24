package gg.overcast.gui.menu;

import gg.overcast.gui.exceptions.MenuManagerException;
import gg.overcast.gui.exceptions.MenuManagerNotSetupException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@SuppressWarnings("unused")
public class MenuManager {
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static boolean isSetup = false;

    private static void registerMenuListener(Server server, Plugin plugin) {
        boolean isAlreadyRegistered = false;
        for (RegisteredListener registeredListener : InventoryClickEvent.getHandlerList()
                .getRegisteredListeners()
        ) {
            if (registeredListener.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }
        if (!isAlreadyRegistered) server.getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    public static void setup(Server server, Plugin plugin) {
        registerMenuListener(
                server, plugin
        );
        isSetup = true;
    }

    public static void openMenu(Class<? extends Menu> menuClass, Player player)
            throws MenuManagerException, MenuManagerNotSetupException {
        try {
            menuClass.getConstructor(PlayerMenuUtility.class)
                    .newInstance(
                            getPlayerMenuUtility(player)
                    ).open();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException exception) {
            exception.printStackTrace();
            throw new MenuManagerException();
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) throws MenuManagerNotSetupException {
        if (!isSetup) throw new MenuManagerNotSetupException();
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(player))) {
            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);
            return playerMenuUtility;
        } else return playerMenuUtilityMap.get(player);
    }
}
