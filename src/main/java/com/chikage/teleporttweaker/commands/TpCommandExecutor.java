package com.chikage.teleporttweaker.commands;

import com.chikage.teleporttweaker.ConfigManager;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class TpCommandExecutor implements CommandExecutor {
    ConfigManager manager;
    public TpCommandExecutor(ConfigManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> optionalTarget = args.getOne("target");
        Optional<Player> optionalPlayer = args.getOne("player");
        Player target;

        if (!optionalTarget.isPresent()) {
            if (src instanceof Player) {
                target = (Player) src;
            } else {
                src.sendMessage(Text.of(TextColors.RED, "プレイヤー以外が実行する場合、対象プレイヤーの指定は必須です"));
                return CommandResult.empty();
            }
        } else {
            target = optionalTarget.get();
        }

        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            if (manager.getUserData(player.getUniqueId()).canTp()) {
                target.setLocation(player.getLocation());
            } else {
                src.sendMessage(Text.of(TextColors.RED, "指定されたプレイヤーが許可していないため、TPすることができません"));
            }
        } else {
            int x = (int) args.getOne("x").get();
            int y = (int) args.getOne("y").get();
            int z = (int) args.getOne("z").get();
            Optional<Double> xRot = args.getOne("x-rot");
            Optional<Double> yRot = args.getOne("y-rot");
            Optional<String> optionalWorldName = args.getOne("world");
            World world;

            if (optionalWorldName.isPresent()) {
                Optional<World> optionalWorld = Sponge.getServer().getWorld(optionalWorldName.get());
                if (!optionalWorld.isPresent()) {
                    src.sendMessage(Text.of(TextColors.RED, "ワールド名: " + optionalWorldName.get() + "は見つかりませんでした"));
                    return CommandResult.empty();
                } else {
                    world = optionalWorld.get();
                }
            } else {
                world = target.getWorld();
            }
            Location<World> location= new Location<World>(world, x, y, z);
            if (xRot.isPresent() && yRot.isPresent()){
                Vector3d rotation = new Vector3d(xRot.get(), yRot.get(), 0);
                target.setLocationAndRotation(location, rotation);
                target.setHeadRotation(rotation);
            } else {
                target.setLocation(location);
            }
        }
        return CommandResult.success();
    }

    public CommandSpec getSpec() {
        return CommandSpec.builder()
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
                .executor(this)
                .build();
    }
}
