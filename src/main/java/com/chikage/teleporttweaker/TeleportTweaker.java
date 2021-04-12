package com.chikage.teleporttweaker;

import com.chikage.teleporttweaker.commands.TpCommandExecutor;
import com.chikage.teleporttweaker.commands.allowTp.AllowTpCommandExecutor;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.nio.file.Path;
import java.util.UUID;

@Plugin(
        id = "tptweaker",
        name = "Teleporttweaker",
        version = "1.0",
        description = "add some features to /tp"
)
public class TeleportTweaker {

    @Inject
    private Logger logger;

    @Inject
    private PluginManager pluginManager;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    ConfigManager configManager;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        PluginContainer plugin = pluginManager.getPlugin("tptweaker").orElse(null);

//        config setup
        configManager = new ConfigManager(configLoader);

//        commands setup
        if (plugin != null) {
            CommandManager commandManager = Sponge.getCommandManager();
            commandManager.register(plugin, new TpCommandExecutor(configManager).getSpec(), "tp");
            commandManager.register(plugin, new AllowTpCommandExecutor(configManager).getSpec(), "allowtp");
        }
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        if (configManager != null) {
            configManager.getUserData(player.getUniqueId());
        }
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        Player player = event.getTargetEntity();
        UUID UUID = player.getUniqueId();
        if (configManager != null) {
            PlayerAllowTpData data = configManager.getUserData(UUID);
            if(!data.isParmanent()) {
                configManager.setUserData(UUID, true, false);
            }
        }
    }
}
