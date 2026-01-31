package toutouchien.niveriacrates.crates;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jspecify.annotations.NullMarked;
import toutouchien.niveriacrates.NiveriaCrates;

@NullMarked
public class CrateManager {
    private final NiveriaCrates plugin;
    private final Object2ObjectMap<String, Crate> crates;

    public CrateManager(NiveriaCrates plugin) {
        this.plugin = plugin;

        this.crates = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    }

    public void createCrate(Crate crate) {
        this.crates.put(crate.id(), crate);
    }

    public Object2ObjectMap<String, Crate> crates() {
        return crates;
    }
}
