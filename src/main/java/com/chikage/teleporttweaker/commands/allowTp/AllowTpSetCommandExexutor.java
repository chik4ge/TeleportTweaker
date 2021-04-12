package com.chikage.teleporttweaker.commands.allowTp;

import com.chikage.teleporttweaker.ConfigManager;
import jdk.nashorn.internal.runtime.options.Option;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class AllowTpSetCommandExexutor implements CommandExecutor {
    ConfigManager manager;
    public AllowTpSetCommandExexutor(ConfigManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        boolean isParmanent = args.hasAny("p");
        Optional<Boolean> canTp = args.getOne("can-tp");

        if (src instanceof Player) {
            manager.setUserData(((Player) src).getUniqueId(), canTp.get(), isParmanent);
        } else {
            src.sendMessage(Text.of("このコマンドを実行するにはプレイヤーである必要があります"));
            return CommandResult.empty();
        }
        src.sendMessage(Text.of("TP許可設定を" + (canTp.get() ? "Yes" : "No") + "に設定しました"));

        return CommandResult.success();
    }

    public CommandSpec getSpec() {
        return CommandSpec.builder()
                .permission("tptweaker.command.allowTp.set")
                .arguments(
                        GenericArguments.flags().flag("p").buildWith(
                                GenericArguments.bool(Text.of("can-tp"))
                        )
                )
                .executor(this)
                .build();
    }
}
