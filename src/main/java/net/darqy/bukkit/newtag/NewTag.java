package net.darqy.bukkit.newtag;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class NewTag extends JavaPlugin {
    
    private File tagConfigFile;
    private File configFile;

    private YamlConfiguration tagConfig = null;
    private YamlConfiguration config = null;
        
    private static HashMap<String, String> tags = new HashMap<String, String>();
        
    public static String tag_format = "&a[&f%tag%&a]&r ";
    public static int max_tag_length = 0;
    public static boolean allow_tag_colors = true;
    public static List<String> dissallowed_tags = Arrays.asList("admin, mod");
    public static boolean alphanumeric_only = true;
    
    @Override
    public void onEnable() {
        tagConfigFile = new File(getDataFolder(), "tags.yml");
        configFile = new File(getDataFolder(), "config.yml");
        
        loadConfig();
        
        getCommand("newtag").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
    
    private void loadConfig() {
        config = getConfiguration();
        
        boolean edited = false;
        if (!config.contains("tag_format")) {
            config.set("tag_format", tag_format);
            edited = true;
        }
        if (!config.contains("max_tag_length")) {
            config.set("max_tag_length", max_tag_length);
            edited = true;
        }
        if (!config.contains("allow_tag_colors")) {
            config.set("allow_tag_colors", allow_tag_colors);
            edited = true;
        }
        if (!config.contains("disallowed_tags")) {
            config.set("disallowed_tags", dissallowed_tags);
            edited = true;
        }
        if (!config.contains("alphanumeric_only")) {
            config.set("alphanumeric_only", alphanumeric_only);
            edited = true;
        }
        
        tag_format = config.getString("tag_format");
        max_tag_length = config.getInt("max_tag_length");
        allow_tag_colors = config.getBoolean("allow_tag_colors");
        dissallowed_tags = config.getStringList("disallowed_tags");
        alphanumeric_only = config.getBoolean("alphanumeric_only");
        
        if (config.contains("tags")) { // old config, move tags to new file
            getTagConfig().set("tags", config.getConfigurationSection("tags"));
            saveTagConfig();
            config.set("tags", null);
            edited = true;
        }
        
        // load existing tags
        ConfigurationSection sect = getTagsSection();
        for (String player : sect.getKeys(false)) {
            tags.put(player, sect.getString(player));
        }
        
        if (edited) {
            try {
                config.save(configFile);
            } catch (IOException ex) {
                getLogger().log(Level.WARNING, "Could not save config.yml", ex);
            }
        }
        
        getLogger().info("Configurations loaded successfully!");
    }
    
    private YamlConfiguration getConfiguration() {
        if (config == null) {
            if (!configFile.exists()) {
                this.saveResource("config.yml", true);
            }
            config = YamlConfiguration.loadConfiguration(configFile);
        }
        return config;
    }
    
    private YamlConfiguration getTagConfig() {
        if (tagConfig == null) {
            if (!tagConfigFile.exists()) {
                this.saveResource("tags.yml", true);
            }
            tagConfig = YamlConfiguration.loadConfiguration(tagConfigFile);
        }
        return tagConfig;
    }
    
    private ConfigurationSection getTagsSection() {
        ConfigurationSection sect = getTagConfig().getConfigurationSection("tags");
        if (sect == null) {
            sect = getTagConfig().createSection("tags");
        }
        return sect;
    }
    
    private void saveTagConfig() {
        try {
            tagConfig.save(tagConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save tags.yml", ex);
        }
    }
    
    public void saveTag(String player, String tag) {
        ConfigurationSection sect = getTagsSection();
        if (tag == null || tag.isEmpty()) {
            sect.set(player, null);
            tags.remove(player);
        } else {
            sect.set(player, tag);
            tags.put(player, tag);
        }
        saveTagConfig();
    }
    
    public boolean hasTag(String player) {
        return tags.containsKey(player);
    }
    
    public String getTag(String player) {
        return tags.get(player);
    }

}
