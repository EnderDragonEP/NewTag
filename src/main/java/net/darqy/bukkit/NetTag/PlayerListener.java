package net.darqy.bukkit.NetTag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String p = e.getPlayer().getName();
        if (NewTag.tags.containsKey(p)) {
            String tag = "§" + NewTag.tag_prefix_color + NewTag.tags.get(p).replaceAll("(?i)&([a-fk-or0-9])", "\u00A7$1") + "§r ";
            String old_format = e.getFormat();
            String new_format = tag + old_format;
            e.setFormat(new_format);
        }
    }
}
