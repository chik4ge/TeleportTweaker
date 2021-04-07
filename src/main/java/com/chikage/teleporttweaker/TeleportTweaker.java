package com.chikage.teleporttweaker;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

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

    private SqlService sql;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        PluginContainer plugin = pluginManager.getPlugin("tptweaker").orElse(null);
        CommandSpec tpCommandSpec = CommandSpec.builder()
                .description(Text.of("/tp [対象のプレイヤー] <移動先のプレイヤー> または /tp [対象のプレイヤー] <x> <y> <z> [ワールド]"))
                .permission("tptweaker.command.tp")
                .arguments(
                        GenericArguments.firstParsing(
                                GenericArguments.seq(
                                        GenericArguments.player(Text.of("target")),
                                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
                                ),
                                GenericArguments.seq(
                                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
                                ),
                                GenericArguments.seq(
                                        GenericArguments.optional(GenericArguments.player(Text.of("target"))),
                                        GenericArguments.integer(Text.of("x")),
                                        GenericArguments.integer(Text.of("y")),
                                        GenericArguments.integer(Text.of("z")),
                                        GenericArguments.optional(GenericArguments.string(Text.of("world")))
                                )
                        )
                )
                .executor(new tpCommandExecutor())
                .build();

        CommandSpec setSpec = CommandSpec.builder()
                .permission("tptweaker.command.allowtp.set")
                .arguments(

                )
                .executor(new allowTpSetCommandExexutor())
                .build();

        CommandSpec infoSpec = CommandSpec.builder()
                .permission("tptweaker.command.allowtp.info")
                .arguments(

                )
                .executor(new allowTpInfoCommandExexutor())
                .build();

        CommandSpec allowTpCommandSpec = CommandSpec.builder()
                .description(Text.of("/allowtp set <true|false> [-p]\n" +
                        "-p: 設定を継続的に保存\n" +
                        "/allowtp info [対象のプレイヤー]"))
                .permission("tptweaker.command.allowtp")
                .child(setSpec, "set")
                .child(infoSpec, "info")
                .build();

        if (plugin != null) {
            CommandManager commandManager = Sponge.getCommandManager();
            commandManager.register(plugin, tpCommandSpec, "tp");
            commandManager.register(plugin, allowTpCommandSpec, "allowtp");
        }

        CommentedConfigurationNode root = null;
        try {
            root = configLoader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }

        ConfigurationNode child = root.getNode("child");
        child.setValue(0);

        try {
            configLoader.save(root);
        } catch (IOException e) {
            System.err.println("Unable to save your messages configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }
    }

    public DataSource getDataSource(String jdbcUrl) throws SQLException {
        if (sql == null) {
            sql = Sponge.getServiceManager().provide(SqlService.class).get();
        }
        return sql.getDataSource(jdbcUrl);
    }

    public void myMethodThatQueries() throws SQLException {

    }
}
