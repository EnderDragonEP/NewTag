package net.darqy.bukkit.NetTag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    
    final NewTag plugin;
    
    public Commands(NewTag instance) {
        this.plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (args.length < 1) {
            s.sendMessage("§aNewTag ver.§e " + plugin.getDescription().getVersion());
            return true;
        } else {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 2) return false;
                
                String pname;
                String tag = args[1];
                
                if (args.length >= 3) {
                    if (!plugin.hasPerm(s, "newtag.set.other")) {
                        s.sendMessage("§cYou don't have permission to do that!");
                        return true;
                    }
                    Player p = plugin.getServer().getPlayer(args[2]);
                    pname = (p != null)? p.getName() : args[2];
                } else {
                    if (!plugin.hasPerm(s, "newtag.set.own")) {
                        s.sendMessage("§cYou don't have permission to do that!");
                        return true;
                    }
                    pname = ((Player) s).getName();
                }
                    
                if (NewTag.enforce_max_tag_length && tag.length() > NewTag.max_tag_length) {
                    s.sendMessage("§cTag length is too long, max length is§7 " + NewTag.max_tag_length);
                    return true;
                }
                
                plugin.config_tags.set(pname, tag);
                plugin.saveConfig();
                NewTag.tags.put(pname, tag);
                plugin.log.info(plugin.prefix + s.getName() + " applied tag " + tag + " to " + pname);
                
                s.sendMessage("§aApplied tag§e " + tag + " §ato§e " + pname);
                return true;
                
            } else if (args[0].equalsIgnoreCase("clear")) {
                String pname;
                if (args.length >= 2) {
                    if (!plugin.hasPerm(s, "newtag.clear.other")) {
                        s.sendMessage("§cYou don't have permission to do that!");
                        return true;
                    }
                    Player p = plugin.getServer().getPlayer(args[1]);
                    pname = (p != null)? p.getName() : args[1];
                } else {
                    if (!plugin.hasPerm(s, "newtag.clear.own")) {
                        s.sendMessage("§cYou don't have permission to do that!");
                        return true;
                    }
                    pname = ((Player) s).getName();
                }
                
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
