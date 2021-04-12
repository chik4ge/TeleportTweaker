package com.chikage.teleporttweaker.commands.allowTp;

import com.chikage.teleporttweaker.ConfigManager;
import com.chikage.teleporttweaker.PlayerAllowTpData;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class AllowTpInfoCommandExexutor implements CommandExecutor {
    ConfigManager manager;
    public AllowTpInfoCommandExexutor(ConfigManager manager){
        super();
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> optionalPlayer = args.getOne("player");
        Player target;

        if (!optionalPlayer.isPresent()) {
            if (src instanceof Player) {
                target = (Player) src;
            } else {
                src.sendMessage(Text.of(TextColors.RED, "プレイヤー以外が実行する場合、対象プレイヤーの指定は必須です"));
                return CommandResult.empty();
            }
        } else {
            target = optionalPlayer.get();
        }

        PlayerAllowTpData data = manager.getUserData(target.getUniqueId());
        src.sendMessage(Text.of("Player: " + target.getName() + "\n" +
                "TP許可: " + (data.canTp() ? "Yes" : "No") + "\n" +
                "リログ後の設定: " + (data.isParmanent() ? "継続" : "リセット")));
        return CommandResult.success();
    }

    public CommandSpec getSpec() {
        return CommandSpec.builder()
                .permission("tptweaker.command.allowTp.info")
                .arguments(
                        GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
                )
                .executor(this)
                .build();
    }
}
