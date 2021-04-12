package com.chikage.teleporttweaker.commands.allowTp;

import com.chikage.teleporttweaker.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class AllowTpCommandExecutor implements CommandExecutor {
    ConfigManager manager;
    public AllowTpCommandExecutor(ConfigManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.success();
    }

    public CommandSpec getSpec() {
        return CommandSpec.builder()
                .description(Text.of("/allowTp set <true|false> [-p]\n" +
                        "-p: 設定を継続的に保存\n" +
                        "/allowTp info [対象のプレイヤー]"))
                .child(new AllowTpSetCommandExexutor(manager).getSpec(), "set")
                .child(new AllowTpInfoCommandExexutor(manager).getSpec(), "info")
                .executor(this)
                .build();
    }
}
