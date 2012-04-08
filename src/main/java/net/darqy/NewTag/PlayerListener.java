package net.darqy.NewTag;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerListener implements Listener {
    
    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerChat(PlayerChatEvent e) {
        String p = e.getPlayer().getName();
        if (NewTag.tags.containsKey(p)) {
            String tag = ChatColor.GREEN + NewTag.tags.get(p) + " ";
            String old_format = e.getFormat();
            String new_format = tag + old_format;
            e.setFormat(new_format);
        }
    }
}
