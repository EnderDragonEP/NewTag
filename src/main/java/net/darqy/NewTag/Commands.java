package net.darqy.NewTag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    
    NewTag plugin;
    
    public Commands(NewTag instance) {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!plugin.hasPerm(s, "newtag.admin")) {
            s.sendMessage("§cYou don't have permission to do that!");
            return true;
        }
        if (args.length < 1) {
            s.sendMessage("§aNewTag ver.§e " + plugin.getDescription().getVersion());
            return true;
        } else {
            if (args.length < 2) return false;
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 3) return false;
                if (args[2].length() > NewTag.max_tag_length) {
                    s.sendMessage("§cTag length is too long, max length is§7 " + NewTag.max_tag_length);
                    return true;
                }
                
                Player p = plugin.getServer().getPlayer(args[1]);
                String pname;
                pname = (p != null)? p.getName() : args[1];
                
                plugin.config_tags.set(pname, args[2]);
                plugin.saveConfig();
                NewTag.tags.put(pname, args[2]);
                plugin.log.info(plugin.prefix + s.getName() + " applied tag " + args[2] + " to " + pname);
                s.sendMessage("§aApplied tag§e " + args[2] + " §ato§e " + pname);
                return true;
            } else if (args[0].equalsIgnoreCase("clear")) {
                Player p = plugin.getServer().getPlayer(args[1]);
                String pname;
                pname = (p != null)? p.getName() : args[1];
                
                if (plugin.config_tags.contains(pname)) {
                    plugin.config_tags.set(pname, null);
                    plugin.saveConfig();
                    NewTag.tags.remove(pname);
                    plugin.log.info(plugin.prefix + s.getName() + " cleared tag from " + pname);
                    s.sendMessage("§aRemoved tag from§e " + pname);
                    return true;
                } else {
                    s.sendMessage("§7" + pname + " §enever had a tag");
                    return true;
                }
            } else {
                s.sendMessage("§cInvalid arguments");
                return true;
            }
        }
    }

}
