package net.darqy.bukkit.newtag;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {
    
    private NewTag plugin;
    
    public PlayerListener(NewTag instance) {
        this.plugin = instance;
    }
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String player = e.getPlayer().getName();
        if (plugin.hasTag(player)) {
            String tag = NewTag.tag_format;
            if (NewTag.allow_tag_colors) {
                tag = tag.replace("%tag%", plugin.getTag(player)); // replace placeholder
                tag = ChatColor.translateAlternateColorCodes('&', tag); // colorize everything
            } else {
                tag = ChatColor.translateAlternateColorCodes('&', tag); // colorize format only
                tag = tag.replace("%tag%", plugin.getTag(player)); // replace placeholder
            }
            e.setFormat(tag + e.getFormat()); // prepend tag to original format
        }
    }
    
}
