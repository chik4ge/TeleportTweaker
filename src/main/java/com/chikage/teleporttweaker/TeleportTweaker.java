package com.chikage.teleporttweaker;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "tptweaker",
        name = "Teleporttweaker"
)
public class TeleportTweaker {

    @Inject
    private Logger logger;

    @Inject
    private PluginManager pluginManager;

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

        CommandSpec allowTpCommandSpec = CommandSpec.builder()
                .description(Text.of("/allowtp set <true|false> [-p]\n" +
                        "-p: 設定を継続的に保存\n" +
                        "/allowtp info [対象のプレイヤー]"))
                .permission("tptweaker.command.allowtp")
                .arguments(

                )
                .executor(new allowTpCommandExexutor())
                .build();

        if (plugin != null) {
            CommandManager commandManager = Sponge.getCommandManager();
            commandManager.register(plugin, tpCommandSpec, "tp");
            commandManager.register(plugin, allowTpCommandSpec, "allowtp");
        }
    }
}
