package fr.edencraft.ecjackpot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.edencraft.ecjackpot.jackpot.Jackpot;
import fr.edencraft.ecjackpot.manager.ConfigurationManager;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class ECJackpot extends JavaPlugin {

	private static ECJackpot INSTANCE;

	private ConfigurationManager configurationManager;
	private InventoryManager inventoryManager;

	private List<Jackpot> jackpots;

	@Override
	public void onEnable() {
		long delay = System.currentTimeMillis();

		INSTANCE = this;

		this.configurationManager = new ConfigurationManager(this);
		this.configurationManager.setupFiles();

		this.inventoryManager = new InventoryManager(this);
		this.inventoryManager.init();

		this.jackpots = loadJackpots();

		log(Level.INFO, "ECJackpot enabled. (took " + (System.currentTimeMillis() - delay) + "ms)");

	}

	private List<Jackpot> loadJackpots() {
		File jackpotFolder = new File(getDataFolder().getAbsolutePath() + File.separatorChar + "jackpots");
		List<Jackpot> jackpots = new ArrayList<>();

		if (jackpotFolder.listFiles() != null) {
			File[] files = jackpotFolder.listFiles();
			assert files != null;

			Arrays.stream(files).forEach(file -> {
				if (file.getName().endsWith(".yml")) {
					Jackpot jackpot = new Jackpot(file);
					if (!jackpot.isValid()) {
						log(Level.WARNING, "Jackpot file " + file.getName() + " is not valid.");
						return;
					}
					jackpots.add(jackpot);
					configurationManager.getFilesMap().put(file, YamlConfiguration.loadConfiguration(file));
					log(Level.INFO, "Jackpot " + jackpot.getName() + " has been loaded successfully" +
							" from " + file.getName());
				}
			});
		}

		return jackpots;
	}

	private void saveJackpots() {
		jackpots.forEach(Jackpot::saveData);
	}

	@Override
	public void onDisable() {
		this.configurationManager.saveFiles();
		this.saveJackpots();
	}

	public void log(Level level, String message) {
		switch (level.getName()) {
			default -> Bukkit.getLogger()
					.log(level, "[" + getPlugin(ECJackpot.class).getName() + "] " + message);
			case "INFO" -> Bukkit.getLogger()
					.log(level, "§a[" + getPlugin(ECJackpot.class).getName() + "] " + message);
			case "WARNING" -> Bukkit.getLogger()
					.log(level, "§e[" + getPlugin(ECJackpot.class).getName() + "] " + message);
			case "SEVERE" -> Bukkit.getLogger()
					.log(level, "§c[" + getPlugin(ECJackpot.class).getName() + "] " + message);
		}
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
