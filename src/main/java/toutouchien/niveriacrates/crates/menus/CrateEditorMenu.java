package toutouchien.niveriacrates.crates.menus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import toutouchien.niveriaapi.NiveriaAPI;
import toutouchien.niveriaapi.lang.Lang;
import toutouchien.niveriaapi.menu.Menu;
import toutouchien.niveriaapi.menu.MenuContext;
import toutouchien.niveriaapi.menu.component.MenuComponent;
import toutouchien.niveriaapi.menu.component.interactive.Button;
import toutouchien.niveriaapi.menu.component.interactive.DoubleDropButton;
import toutouchien.niveriaapi.menu.component.layout.Grid;
import toutouchien.niveriaapi.utils.ItemBuilder;
import toutouchien.niveriaapi.utils.Task;
import toutouchien.niveriaapi.utils.TimeUtils;
import toutouchien.niveriacrates.NiveriaCrates;
import toutouchien.niveriacrates.crates.Crate;
import toutouchien.niveriacrates.crates.CrateManager;

import java.util.concurrent.TimeUnit;

import static toutouchien.niveriacrates.NiveriaCrates.LANG;

@NullMarked
public class CrateEditorMenu extends Menu {
    /**
     * Constructs a new Menu with the specified player and context.
     *
     * @param player  the player who will interact with this menu
     * @param context the menu context for component interaction
     * @throws NullPointerException if player or context is null
     */
    protected CrateEditorMenu(Player player, MenuContext context) {
        super(player, context);
    }

    /**
     * Returns the title component for this menu's inventory.
     * <p>
     * This method must be implemented by subclasses to define the menu's title.
     *
     * @return the title component displayed at the top of the inventory
     */
    @Override
    protected Component title() {
        return LANG.get("niveriacrates.menu.edit_crate.title",
                Lang.unparsedPlaceholder("niveriacrates_crate_id", (String) this.context.get("crate_id"))
        );
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
    @Override
    protected MenuComponent root(MenuContext context) {
        CrateManager crateManager = NiveriaCrates.instance().crateManager();
        String crateID = (String) context.get("crate_id");
        Crate crate = crateManager.crate(crateID);

        DoubleDropButton deleteButton = deleteButton();
        Button backButton = backButton();

        Button renameButton = renameButton(crate);

        return Grid.create()
                .size(9, 6)
                .add(context, 11, renameButton)
                .add(context, 49, backButton)
                .add(context, 53, deleteButton)
                .build();
    }

    private Button renameButton(Crate crate) {
        return Button.create()
                .item(ItemBuilder.of(Material.WRITABLE_BOOK)
                        .renamableName(LANG.get("niveriacrates.menu.edit_crate.rename.name"))
                        .lore(LANG.getList("niveriacrates.menu.edit_crate.rename.lore",
                                Lang.componentPlaceholder("niveriacrates_crate_name", crate.name())
                        ))
                        .build()
                )
                .onClick(event -> {
                    Title title = Title.title(
                            LANG.get("niveriacrates.menu.edit_crate.rename.title_sent.title"),
                            LANG.get("niveriacrates.menu.edit_crate.rename.title_sent.subtitle"),
                            0,
                            (int) TimeUtils.ticks(1, TimeUnit.DAYS),
                            0
                    );

                    Player player = event.player();

                    player.showTitle(title);
                    player.closeInventory();

                    NiveriaAPI.instance().chatInputManager().requestInput(player, s -> {
                        crate.name(MiniMessage.miniMessage().deserialize(s));

                        // Remove the previous menu (this menu)
                        event.context().previousMenu();

                        player.resetTitle();
                        Task.sync(this::reopen, NiveriaCrates.instance());
                    });
                })
                .build();
    }

    private static DoubleDropButton deleteButton() {
        return DoubleDropButton.create()
                .item(ItemBuilder.of(Material.BARRIER)
                        .name(LANG.get("niveriacrates.menu.edit_crate.delete.name"))
                        .lore(LANG.getList("niveriacrates.menu.edit_crate.delete.lore"))
                        .build()
                )
                .dropItem(ItemBuilder.of(Material.BARRIER)
                        .name(LANG.get("niveriacrates.menu.edit_crate.delete_confirm.name"))
                        .lore(LANG.getList("niveriacrates.menu.edit_crate.delete_confirm.lore"))
                        .build()
                )
                .onDoubleDrop(event -> {
                    MenuContext ctx = event.context();
                    String crateID = (String) ctx.get("crate_id");
                    NiveriaCrates.instance().crateManager().deleteCrate(crateID);

                    // Remove the previous menu (this menu)
                    ctx.previousMenu();

                    new CratesMenu(event.player(), ctx).open();
                })
                .build();
    }

    private static Button backButton() {
        return Button.create()
                .item(ItemBuilder.of(Material.ARROW)
                        .name(LANG.get("niveriacrates.menu.edit_crate.back.name"))
                        .lore(LANG.getList("niveriacrates.menu.edit_crate.back.lore"))
                        .build()
                )
                .onClick(event -> event.context().previousMenu().open())
                .build();
    }
}
