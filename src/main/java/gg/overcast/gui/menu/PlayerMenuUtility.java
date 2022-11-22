package gg.overcast.gui.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Stack;

@SuppressWarnings({"unused", "rawtypes"})
public class PlayerMenuUtility {
    private final Player owner;
    private final HashMap<String, Object> dataMap = new HashMap<>();
    private final Stack<Menu> history = new Stack<>();

    public PlayerMenuUtility(Player player) {
        this.owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    public void setData(String identifier, Object data) {
        this.dataMap.put(identifier, data);
    }

    public void setData(Enum identifier, Object data) {
        this.dataMap.put(
                identifier.toString(), data
        );
    }

    public Object getData(String identifier) {
        return this.dataMap.get(
                identifier
        );
    }

    public Object getData(Enum identifier) {
        return this.dataMap.get(
                identifier.toString()
        );
    }

    public <Type> Type getData(String identifier,
                               Class<Type> classRef
    ) {
        Object object = this.dataMap.get(
                identifier
        );
        if (object == null) return null;
        else return classRef
                .cast(object);
    }

    public <Type> Type getData(Enum identifier,
                               Class<Type> classRef
    ) {
        Object object = this.dataMap.get(
                identifier.toString()
        );
        if (object == null) return null;
        else return classRef
                .cast(object);
    }

    public Menu lastMenu() {
        this.history.pop();
        return this.history.pop();
    }

    public void pushMenu(Menu menu) {
        this.history.push(menu);
    }
}

