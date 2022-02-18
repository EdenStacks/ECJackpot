package fr.edencraft.ecjackpot;

import fr.edencraft.ecjackpot.jackpot.Jackpot;
import fr.edencraft.ecjackpot.manager.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public final class ECJackpot extends JavaPlugin {

	public static ECJackpot INSTANCE;

	private ConfigurationManager configurationManager;

	@Override
	public void onEnable() {
		long delay = System.currentTimeMillis();

		INSTANCE = this;

		this.configurationManager = new ConfigurationManager(this);
		this.configurationManager.setupFiles();

		loadJackpots();

		log(Level.INFO, "ECJackpot enabled. (took " + (System.currentTimeMillis() - delay) + "ms)");

	}

	private void loadJackpots() {
		File jackpotFolder = new File(getDataFolder().getAbsolutePath() + File.separatorChar + "jackpots");

		try {
			Arrays.stream(Objects.requireNonNull(jackpotFolder.listFiles())).forEach(file -> {
				if (file.getName().endsWith(".yml")) {
					System.out.println(new Jackpot(file));
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
}
