package toutouchien.niveriacrates.crates.menus;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import toutouchien.niveriaapi.NiveriaAPI;
import toutouchien.niveriaapi.menu.Menu;
import toutouchien.niveriaapi.menu.MenuContext;
import toutouchien.niveriaapi.menu.component.MenuComponent;
import toutouchien.niveriaapi.menu.component.container.Paginator;
import toutouchien.niveriaapi.menu.component.interactive.Button;
import toutouchien.niveriaapi.menu.component.layout.Grid;
import toutouchien.niveriaapi.utils.ItemBuilder;
import toutouchien.niveriaapi.utils.StringUtils;
import toutouchien.niveriaapi.utils.Task;
import toutouchien.niveriaapi.utils.TimeUtils;
import toutouchien.niveriacrates.NiveriaCrates;
import toutouchien.niveriacrates.crates.Crate;
import toutouchien.niveriacrates.crates.CrateManager;
import toutouchien.niveriacrates.utils.CrateUtils;

import java.util.concurrent.TimeUnit;

import static toutouchien.niveriacrates.NiveriaCrates.LANG;

public class CratesMenu extends Menu {
    /**
     * Constructs a new Menu for the specified player.
     *
     * @param player the player who will interact with this menu
     * @throws NullPointerException if player is null
     */
    public CratesMenu(@NotNull Player player) {
        super(player);
    }

    /**
     * Constructs a new Menu with the specified player and context.
     *
     * @param player  the player who will interact with this menu
     * @param context the menu context for component interaction
     * @throws NullPointerException if player or context is null
     */
    public CratesMenu(@NotNull Player player, @NotNull MenuContext context) {
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
    protected @NotNull Component title() {
        return LANG.get("niveriacrates.menu.crates.title");
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
    protected @NotNull MenuComponent root(@NotNull MenuContext context) {
        CrateManager crateManager = NiveriaCrates.instance().crateManager();
        Button backButton = Button.create()
                .item(ItemBuilder.of(Material.ARROW)
                        .name(LANG.get("niveriacrates.menu.crates.back.name"))
                        .lore(LANG.getList("niveriacrates.menu.crates.back.lore"))
                        .build()
                )
                .onClick(event -> event.context().previousMenu().open())
                .build();

        Button addButton = Button.create()
                .item(ItemBuilder.of(Material.PLAYER_HEAD)
                        .renamableName(LANG.get("niveriacrates.menu.crates.new.name"))
                        .lore(LANG.getList("niveriacrates.menu.crates.new.lore"))
                        .headTexture("http://textures.minecraft.net/texture/5250b3cce76635ef4c7a88b2c597bd2749868d78f5afa566157c2612ae4120")
                        .build()
                )
                .onClick(event -> {
                    Title title = Title.title(
                            LANG.get("niveriacrates.menu.crates.new.title_sent.title"),
                            LANG.get("niveriacrates.menu.crates.new.title_sent.subtitle"),
                            0,
                            (int) TimeUtils.ticks(1, TimeUnit.DAYS),
                            0
                    );

                    Player player = event.player();

                    player.showTitle(title);
                    player.closeInventory();

                    NiveriaAPI.instance().chatInputManager().requestInput(player, s -> {
                        boolean allowed = CrateUtils.idAllowed(s);
                        if (!allowed) {
                            LANG.sendMessage(player, "niveriacrates.menu.crates.new.invalid_id");
                            player.resetTitle();
                            return;
                        }

                        Crate newCrate = new Crate(s, Component.text(StringUtils.capitalize(s)), ItemStack.of(Material.CHEST));
                        crateManager.createCrate(newCrate);

                        player.resetTitle();
                        Task.sync(this::open, NiveriaCrates.instance());
                    });
                })
                .build();

        ObjectList<MenuComponent> crates = generateCrateItems(crateManager.crates().values());

        Paginator paginator = Paginator.create()
                .size(7, 3)
                .addAll(context, crates)
                .backItem(ItemBuilder.of(Material.ARROW)
                        .name(LANG.get("niveriacrates.menu.crates.previous_page.name"))
                        .lore(LANG.getList("niveriacrates.menu.crates.previous_page.lore"))
                        .build()
                )
                .nextItem(ItemBuilder.of(Material.ARROW)
                        .name(LANG.get("niveriacrates.menu.crates.next_page.name"))
                        .lore(LANG.getList("niveriacrates.menu.crates.next_page.lore"))
                        .build()
                )
                .build();

        return Grid.create()
                .size(9, 6)
                .add(context, 10, paginator)
                .add(context, 45, paginator.backButton())
                .add(context, 49, backButton)
                .add(context, 51, addButton)
                .add(context, 53, paginator.nextButton())
                .build();
    }

    private ObjectList<MenuComponent> generateCrateItems(ObjectCollection<Crate> crates) {
        ObjectList<MenuComponent> crateButtons = new ObjectArrayList<>();
        for (Crate crate : crates) {
            crateButtons.add(Button.create()
                    .item(ItemBuilder.of(crate.icon())
                            .name(crate.name())
                            .build()
                    )
                    .onClick(event -> {
                        MenuContext ctx = event.context();
                        ctx.set("crate_id", crate.id());

                        new CrateEditorMenu(event.player(), ctx).open();
                    })
                    .build()
            );
        }

        return crateButtons;
    }
}
