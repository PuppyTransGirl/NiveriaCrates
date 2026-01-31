package toutouchien.niveriacrates.commands.crates;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import toutouchien.niveriaapi.utils.CommandUtils;
import toutouchien.niveriacrates.menus.EditorMenu;

public class CratesCommand {
    private CratesCommand() {
        throw new IllegalStateException("Command class");
    }

    public static LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("crates")
                .requires(css -> CommandUtils.defaultRequirements(css, "niveriacrates.command.crates"))
                .then(editorCommand())
                .build();
    }

    private static LiteralArgumentBuilder<CommandSourceStack> editorCommand() {
        return Commands.literal("editor")
                .requires(css -> CommandUtils.defaultRequirements(css, "niveriacrates.command.crates.editor", true))
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    new EditorMenu(player).open();
                    return Command.SINGLE_SUCCESS;
                });
    }
}
