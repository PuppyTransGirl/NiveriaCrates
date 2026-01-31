package toutouchien.niveriacrates.crates;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Crate {
    private final String id;
    private Component name;
    private ItemStack icon;

    public Crate(String id, Component name, ItemStack icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String id() {
        return id;
    }

    public Component name() {
        return name;
    }

    public ItemStack icon() {
        return icon;
    }

    public void name(Component name) {
        this.name = name;
    }

    public void icon(ItemStack icon) {
        this.icon = icon;
    }
}
