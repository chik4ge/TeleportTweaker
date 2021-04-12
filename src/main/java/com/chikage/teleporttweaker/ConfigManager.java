package com.chikage.teleporttweaker;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.util.UUID;

public class ConfigManager {
    ConfigurationLoader<CommentedConfigurationNode> loader;
    CommentedConfigurationNode root;

    public ConfigManager(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        loader = configLoader;
        try {
            root = configLoader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }
    }

    public PlayerAllowTpData getUserData(UUID UUID){
        if (root != null) {
            ConfigurationNode playerData = root.getNode("players", UUID.toString());
            boolean canTp = playerData.getNode("can-tp").getBoolean(true);
            boolean isParmanent = playerData.getNode("parmanent").getBoolean(false);

            saveConfig();
            return new PlayerAllowTpData(UUID, canTp, isParmanent);
        } else return null;
    }

    public void setUserData(UUID UUID, boolean canTp, boolean isParmanent) {
        if (root != null) {
            ConfigurationNode playerData = root.getNode("players", UUID.toString());
            playerData.getNode("can-tp").setValue(canTp);
            playerData.getNode("parmanent").setValue(isParmanent);

            saveConfig();
        }
    }

    public void saveConfig(){
        try {
            loader.save(root);
        } catch (IOException e) {
            System.err.println("Unable to save your messages configuration! Sorry! " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }
    }
}
