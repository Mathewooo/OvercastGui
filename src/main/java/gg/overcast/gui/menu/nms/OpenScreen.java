package gg.overcast.gui.menu.nms;

import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpenScreen {
    private static OpenScreen instance;

    public static synchronized OpenScreen getIn() {
        if (instance == null) {
            instance = new OpenScreen();
        }
        return instance;
    }

    public void changeTitle(String title, Player player) {
        final var entityPlayer = (
                (CraftPlayer) player
        ).getHandle();
        var windowId = entityPlayer
                .containerMenu.containerId;
        Component component = Component.empty().append(title);
        var packet = new ClientboundOpenScreenPacket(
                windowId, Inventories.NORMAL.getMenuType(), component
        );
        sendPacket(entityPlayer, packet);
        player.updateInventory();
    }

    private <T extends PacketListener> void sendPacket(
            ServerPlayer player,
            Packet<T> packet
    ) {
        player.connection.send(
                packet
        );
    }
}
