package toutouchien.niveriacrates;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import toutouchien.niveriaapi.lang.Lang;
import toutouchien.niveriaapi.updatechecker.UpdateChecker;
import toutouchien.niveriacrates.commands.crates.CratesCommand;
import toutouchien.niveriacrates.commands.niveriacrates.NiveriaCratesCommand;
import toutouchien.niveriacrates.crates.CrateManager;

import java.util.List;

public class NiveriaCrates extends JavaPlugin {
    private static final int BSTATS_PLUGIN_ID = 29053;
    private static NiveriaCrates instance;

    public static Lang LANG;

    private CrateManager crateManager;

    private Metrics bStats;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            Commands registrar = commands.registrar();
            registrar.register(NiveriaCratesCommand.get());
            registrar.register(CratesCommand.get(), List.of("crate", "ncrates", "ncrate"));
        });

        LANG = Lang.builder(this)
                .addDefaultLanguageFiles("en_US.yml", "fr_FR.yml")
                .build();

        saveDefaultConfig();

        this.crateManager = new CrateManager(this);

        this.bStats = new Metrics(this, BSTATS_PLUGIN_ID);

        new UpdateChecker(this, "3cxoPKIB", "niveriacrates.new_update");
    }

    @Override
    public void onDisable() {
        this.bStats.shutdown();

        getServer().getScheduler().cancelTasks(this);
    }

    public void reload() {
        LANG.reload();
    }

    public CrateManager crateManager() {
        return crateManager;
    }

    public static NiveriaCrates instance() {
        return instance;
    }
}
