package net.darqy.bukkit.newtag;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    
    final NewTag plugin;
    
    public Commands(NewTag instance) {
        this.plugin = instance;
    }
    
    public static final String TAG_TOO_LONG = ChatColor.RED + "Tag can be no longer than %d characters.";
    public static final String TAG_ILLEGAL = ChatColor.RED + "You can not use the phrase \"%s\" in the tag";
    
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (args.length < 1) {
            s.sendMessage("§aNewTag ver.§e " + plugin.getDescription().getVersion());
            return true;
        }
        
        String player;
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                s.sendMessage(ChatColor.YELLOW + "Usage: /" + l + " set [tag]");
                return true;
            }
            String tag = args[1];

            if (args.length >= 3) {
                if (!hasPerm(s, "newtag.set.other", false)) return true;
                Player p = plugin.getServer().getPlayer(args[2]);
                player = (p != null)? p.getName() : args[2];
            } else {
                if (!hasPerm(s, "newtag.set.own", false)) return true;
                player = s.getName();
            }
            
            if (NewTag.alphanumeric_only) {
                tag = tag.replaceAll("[^a-zA-Z0-9&]", "");
            }

            if (NewTag.max_tag_length > 0 && tag.length() > NewTag.max_tag_length) {
                s.sendMessage(String.format(TAG_TOO_LONG, NewTag.max_tag_length));
                return true;
            }
            
            if (!hasPerm(s, "newtag.anytag", true)) {
                String temp = tag.toLowerCase();
                for (final String disallowed : NewTag.dissallowed_tags) {
                    if (temp.contains(disallowed)) {
                        s.sendMessage(String.format(TAG_ILLEGAL, disallowed));
                        return true;
                    }
                }
            }

            plugin.saveTag(player, tag);
            plugin.getLogger().info(s.getName() + " applied tag: \"" + tag + "\" to " + player);
            s.sendMessage("§aApplied tag:§e " + tag + (player.equals(s.getName())? "" : " §ato§e " + player));
        } else if (args[0].equalsIgnoreCase("clear")) {
            if (args.length >= 2) {
                if (!hasPerm(s, "newtag.clear.other", false)) return true;
                Player p = plugin.getServer().getPlayer(args[1]);
                player = (p != null)? p.getName() : args[1];
            } else {
                if (!hasPerm(s, "newtag.clear.own", false)) return true;
                player = s.getName();
            }

            if (plugin.hasTag(player)) {
                plugin.saveTag(player, null);
                plugin.getLogger().info(s.getName() + " removed tag from " + player);
                s.sendMessage("§aRemoved " + (player.equals(s.getName())? "your tag" : player + "'s tag"));
            } else {
                s.sendMessage("§7" + player + " §cnever had a tag");
            }
        } else {
            s.sendMessage(ChatColor.RED + "Invalid argument. Valid: " + ChatColor.YELLOW + "set, clear");
        }
        return true;
    }
        
    public boolean hasPerm(CommandSender s, String node, boolean silent) {
        if (!s.hasPermission(node)) {
            if (!silent) {
                s.sendMessage(ChatColor.RED + "You don't have permission.");
            }
            return false;
        }
        return true;
    }

}
