package fr.edencraft.ecjackpot;

import fr.edencraft.ecjackpot.jackpot.Jackpot;
import fr.edencraft.ecjackpot.manager.ConfigurationManager;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public final class ECJackpot extends JavaPlugin {

	public static ECJackpot INSTANCE;

	private ConfigurationManager configurationManager;
	private InventoryManager inventoryManager;

	private final List<Jackpot> jackpots = new ArrayList<>();

	@Override
	public void onEnable() {
		long delay = System.currentTimeMillis();

		INSTANCE = this;

		this.configurationManager = new ConfigurationManager(this);
		this.configurationManager.setupFiles();

		this.inventoryManager = new InventoryManager(this);
		this.inventoryManager.init();

		loadJackpots();

		log(Level.INFO, "ECJackpot enabled. (took " + (System.currentTimeMillis() - delay) + "ms)");

	}

	private void loadJackpots() {
		File jackpotFolder = new File(getDataFolder().getAbsolutePath() + File.separatorChar + "jackpots");

		try {
			Arrays.stream(Objects.requireNonNull(jackpotFolder.listFiles())).forEach(file -> {
				if (file.getName().endsWith(".yml")) {
					Jackpot jackpot = new Jackpot(file);
					if (!jackpot.isValid()) {
						log(Level.WARNING, "Jackpot file " + file.getName() + " is not valid.");
						return;
					}
					jackpots.add(jackpot);
					log(Level.INFO, "Jackpot " + jackpot.getName() + " has been loaded successfully" +
							" from " + file.getName());
				}
			});
		} catch (NullPointerException e) {
			log(Level.INFO, "No jackpot file found.");
		}

	}

	@Override
	public void onDisable() {
		this.configurationManager.saveFiles();
	}

	public void log(Level level, String message) {
		Bukkit.getLogger().log(level, "[" + getPlugin(ECJackpot.class).getName() + "] " + message);
	}

	public static ECJackpot getINSTANCE() {
		return INSTANCE;
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public List<Jackpot> getJackpots() {
		return jackpots;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}
}
