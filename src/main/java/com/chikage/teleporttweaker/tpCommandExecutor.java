package com.chikage.teleporttweaker;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class tpCommandExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> optionalPlayer = args.getOne(Text.of("player"));
        Player player;

        if (!optionalPlayer.isPresent()) {
            if (src instanceof Player) {
                player = (Player) src;
            } else {
                src.sendMessage(Text.builder("プレイヤー以外が実行する場合、対象プレイヤーの指定は必須です").color(TextColors.RED).build());
                return CommandResult.empty();
            }
        } else {
            player = optionalPlayer.get();
        }

        Optional<Player> optionalTargetPlayer = args.getOne(Text.of("target"));
        return CommandResult.success();
    }
}
