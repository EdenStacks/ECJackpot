package fr.edencraft.ecjackpot.jackpot;

import fr.edencraft.ecjackpot.ECJackpot;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Jackpot {

	private final File jackpotFile;

	private final String displayName;
	private final String name;
	private final String commandName;
	private final String currencyType;
	private final String material;
	private final int amountNeeded;
	private final String rewardCommand;
	private final List<String> jackpotInformationLore;
	private final List<String> lastParticipantLore;
	private final List<String> bestParticipantLore;
	private final List<String> listParticipantLore;
	private final List<String> jackpotRulesLore;
	private final List<String> jackpotRewardLore;

	private JackpotProvider jackpotProvider;


	/**
	 * Build a {@link Jackpot} instance using a {@link File} in YAML format.
	 *
	 * @param jackpotFile file in YAML format with all jackpot information.
	 */
	public Jackpot(File jackpotFile) {
		this.jackpotFile = jackpotFile;
		FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(jackpotFile);

		this.displayName = fileConfiguration.getString("display-name");
		this.name = jackpotFile.getName().replace(".yml", "");
		this.commandName = fileConfiguration.getString("menu.command-name");
		this.currencyType = fileConfiguration.getString("currency-type");
		this.material = fileConfiguration.getString("material");
		this.amountNeeded = fileConfiguration.getInt("amount-needed");
		this.rewardCommand = fileConfiguration.getString("reward-command");

		this.jackpotInformationLore = new ArrayList<>();
		try {
			this.jackpotInformationLore.addAll(Arrays
					.asList(Objects.requireNonNull(fileConfiguration.getString("menu.jackpot-information"))
					.split("\n")));
		} catch (NullPointerException e) {
			ECJackpot.getINSTANCE().log(Level.WARNING, "menu.jackpot-information not set for " + this.name);
		}

		this.lastParticipantLore = new ArrayList<>();
		try {
			this.lastParticipantLore.addAll(Arrays
					.asList(Objects.requireNonNull(fileConfiguration.getString("menu.last-participant"))
							.split("\n")));
		} catch (NullPointerException e) {
			ECJackpot.getINSTANCE().log(Level.WARNING, "menu.last-participant not set for " + this.name);
		}

		this.bestParticipantLore = new ArrayList<>();
		try {
			this.bestParticipantLore.addAll(Arrays
					.asList(Objects.requireNonNull(fileConfiguration.getString("menu.best-participant"))
							.split("\n")));
		} catch (NullPointerException e) {
			ECJackpot.getINSTANCE().log(Level.WARNING, "menu.best-participant not set for " + this.name);
		}

		this.listParticipantLore = new ArrayList<>();
		try {
			this.listParticipantLore.addAll(Arrays
					.asList(Objects.requireNonNull(fileConfiguration.getString("menu.list-participant"))
							.split("\n")));
		} catch (NullPointerException e) {
			ECJackpot.getINSTANCE().log(Level.WARNING, "menu.list-participant not set for " + this.name);
		}

		this.jackpotRulesLore = new ArrayList<>();
		try {
			this.jackpotRulesLore.addAll(Arrays
					.asList(Objects.requireNonNull(fileConfiguration.getString("menu.jackpot-rules"))
							.split("\n")));
		} catch (NullPointerException e) {
			ECJackpot.getINSTANCE().log(Level.WARNING, "menu.jackpot-rules not set for " + this.name);
		}

		this.jackpotRewardLore = new ArrayList<>();
		try {
			this.jackpotRewardLore.addAll(Arrays
					.asList(Objects.requireNonNull(fileConfiguration.getString("menu.jackpot-reward"))
							.split("\n")));
		} catch (NullPointerException e) {
			ECJackpot.getINSTANCE().log(Level.WARNING, "menu.jackpot-reward not set for " + this.name);
		}

		if (isValid() && commandName != null) {
			Permission permission = new Permission("ecjackpot.command." + commandName);
			permission.setDefault(PermissionDefault.OP);
			permission.setDescription("Used to open " + name);

			ECJackpot.getINSTANCE().getServer().getCommandMap().register(commandName, new JackpotCommand(
					commandName,
					"Use it to open " + this.name + " jackpot",
					"§cUtilisez §e/" + commandName,
					List.of(commandName),
					permission,
					this
			));

			this.jackpotProvider = new JackpotProvider(this);
		}

	}

	public boolean isValid() {
		List<String> validCurrencies = Arrays.asList("ITEM", "MONEY");
		if (!validCurrencies.contains(getCurrencyType())) return false;

		Material material = Material.getMaterial(getMaterial());
		if (getCurrencyType().equalsIgnoreCase("ITEM") && (material == null || material.isAir())) return false;

		if (getAmountNeeded() < 1) return false;

		return true;
	}

	public JackpotProvider getJackpotProvider() {
		return jackpotProvider;
	}

	public File getJackpotFile() {
		return jackpotFile;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getName() {
		return name;
	}

	public String getCommandName() {
		return commandName;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public String getMaterial() {
		return material;
	}

	public int getAmountNeeded() {
		return amountNeeded;
	}

	public String getRewardCommand() {
		return rewardCommand;
	}

	public List<String> getJackpotInformationLore() {
		return jackpotInformationLore;
	}

	public List<String> getLastParticipantLore() {
		return lastParticipantLore;
	}

	public List<String> getBestParticipantLore() {
		return bestParticipantLore;
	}

	public List<String> getListParticipantLore() {
		return listParticipantLore;
	}

	public List<String> getJackpotRulesLore() {
		return jackpotRulesLore;
	}

	public List<String> getJackpotRewardLore() {
		return jackpotRewardLore;
	}

	@Override
	public String toString() {
		return "Jackpot{" +
				"jackpotFile=" + jackpotFile +
				", displayName='" + displayName + '\'' +
				", name='" + name + '\'' +
				", commandName='" + commandName + '\'' +
				", currencyType='" + currencyType + '\'' +
				", material='" + material + '\'' +
				", amountNeeded=" + amountNeeded +
				", rewardCommand='" + rewardCommand + '\'' +
				", jackpotInformationLore=" + jackpotInformationLore +
				", lastParticipantLore=" + lastParticipantLore +
				", bestParticipantLore=" + bestParticipantLore +
				", listParticipantLore=" + listParticipantLore +
				", jackpotRulesLore=" + jackpotRulesLore +
				", jackpotRewardLore=" + jackpotRewardLore +
				'}';
	}

}
