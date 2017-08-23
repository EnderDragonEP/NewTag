package net.darqy.bukkit.newtag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    
    private final NewTag plugin;
    
    public PlayerListener(NewTag instance) {
        this.plugin = instance;
    }
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String playerTag = plugin.getTag(e.getPlayer());
        
        if (NewTag.tag_placeholder != null && !NewTag.tag_placeholder.isEmpty()) {
            // find the placeholder in the message and replace it with the tag
            e.setFormat(e.getFormat().replace(NewTag.tag_placeholder, playerTag == null? "" : plugin.formatChatTag(playerTag)));
        } else {
            if (playerTag == null) {
                return;
            }
            
            // prepend the tag to the beginning of the message
            e.setFormat(plugin.formatChatTag(playerTag) + e.getFormat());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!NewTag.enable_tab_list_tag) {
            return;
        }
        
        String playerTag = plugin.getTag(e.getPlayer());
        if (playerTag == null) {
            return;
        }
        
        String format = plugin.formatTabListTag(playerTag);
        String listName = format + e.getPlayer().getPlayerListName();
        
        // truncate the result to max 16 characters long
        //listName = listName.substring(0, Math.min(15, listName.length()));
        
        // update the tab list name for this player
        e.getPlayer().setPlayerListName(listName);
    }
    
}
