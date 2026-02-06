package toutouchien.niveriacrates.menus;

import io.papermc.paper.registry.keys.DataComponentTypeKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import toutouchien.niveriaapi.menu.Menu;
import toutouchien.niveriaapi.menu.MenuContext;
import toutouchien.niveriaapi.menu.component.MenuComponent;
import toutouchien.niveriaapi.menu.component.interactive.Button;
import toutouchien.niveriaapi.menu.component.layout.Grid;
import toutouchien.niveriaapi.utils.ItemBuilder;
import toutouchien.niveriacrates.crates.menus.CratesMenu;

import static toutouchien.niveriacrates.NiveriaCrates.LANG;

public class EditorMenu extends Menu {
    /**
     * Constructs a new Menu for the specified player.
     *
     * @param player the player who will interact with this menu
     * @throws NullPointerException if player is null
     */
    public EditorMenu(@NotNull Player player) {
        super(player);
    }

    /**
     * Returns the title component for this menu's inventory.
     * <p>
     * This method must be implemented by subclasses to define the menu's title.
     *
     * @return the title component displayed at the top of the inventory
     */
    @Override
    protected @NotNull Component title() {
        return LANG.get("niveriacrates.menu.editor.title");
    }

    /**
     * Creates and returns the root component for this menu.
     * <p>
     * This method must be implemented by subclasses to define the menu's layout
     * and components.
     *
     * @param context the menu context for component interaction
     * @return the root component that defines the menu's structure
     */
    @SuppressWarnings({"UnstableApiUsage", "java:S2637"})
    @Override
    protected @NotNull MenuComponent root(@NotNull MenuContext context) {
        Button cratesEditor = Button.create()
                .item(ItemBuilder.of(Material.TRIAL_SPAWNER)
                        .name(LANG.get("niveriacrates.menu.editor.crates.name"))
                        .lore(LANG.getList("niveriacrates.menu.editor.crates.lore"))
                        .hide(Registry.DATA_COMPONENT_TYPE.get(DataComponentTypeKeys.BLOCK_ENTITY_DATA))
                        .build()
                )
                .onClick(event -> new CratesMenu(event.player(), event.context()).open())
                .build();

        Button keysEditor = Button.create()
                .item(ItemBuilder.of(Material.TRIAL_KEY)
                        .name(LANG.get("niveriacrates.menu.editor.keys_editor.name"))
                        .lore(LANG.getList("niveriacrates.menu.editor.keys_editor.lore"))
                        .build()
                )
                .build();

        return Grid.create()
                .size(9, 3)
                .add(context, 11, cratesEditor)
                .add(context, 15, keysEditor)
                .build();
    }
}
