package fr.edencraft.ecjackpot.jackpot;

import com.google.gson.*;
import fr.edencraft.ecjackpot.ECJackpot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

	// Data
	private final List<JackpotParticipant> participants = new ArrayList<>();
	private UUID lastParticipant = null;
	private int pot = 0;

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

			JackpotCommand jackpotCommand = new JackpotCommand(
					commandName,
					"Use it to open " + this.name + " jackpot",
					"§cUtilisez §e/" + commandName + " §a<info|add>",
					List.of(commandName),
					permission,
					this
			);

			if (ECJackpot.getINSTANCE().getServer().getCommandMap().register(commandName, jackpotCommand)) {
				ECJackpot.getINSTANCE().log(Level.INFO, "The " + commandName + " command has been registered.");
			} else {
				ECJackpot.getINSTANCE().log(Level.WARNING, "The " + commandName + " command hasn't been registered.");
			}

			if (loadData()) {
				ECJackpot.getINSTANCE().log(Level.INFO, "Data successfully loaded for " + this.name + ".");
			} else {
				ECJackpot.getINSTANCE().log(Level.WARNING, "Unable to load data for " + this.name + ".");
			}

			this.jackpotProvider = new JackpotProvider(this);
		}

	}

	public boolean isValid() {
		List<String> validCurrencies = Arrays.asList("ITEM", "MONEY");
		if (!validCurrencies.contains(getCurrencyType())) return false;

		Material material = Material.getMaterial(getMaterial());
		if (getCurrencyType().equalsIgnoreCase("ITEM") && (material == null || material.isAir())) return false;

		return getAmountNeeded() >= 1;
	}

	/**
	 * @return true if successfully loaded else false.
	 */
	private boolean loadData() {
		if (!isValid() || !getDataFile().exists()) return false;

		File dataFile = getDataFile();

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		try {
			JsonElement jsonElement = JsonParser.parseReader(new FileReader(dataFile));
			this.lastParticipant = UUID.fromString(jsonElement.getAsJsonObject().get("lastParticipant").getAsString());
			this.pot = jsonElement.getAsJsonObject().get("pot").getAsInt();
			JsonArray participants = jsonElement.getAsJsonObject().get("participants").getAsJsonArray();

			for (JsonElement participant : participants) {
				UUID uuid = UUID.fromString(participant.getAsJsonObject().get("uuid").getAsString());

				HashMap<Long, Integer> participationLog = new HashMap<>();
				Set<Map.Entry<String, JsonElement>> entries = participant
						.getAsJsonObject()
						.get("participationLog")
						.getAsJsonObject()
						.entrySet();

				for (Map.Entry<String, JsonElement> entry : entries) {
					participationLog.put(
							Long.valueOf(entry.getKey()),
							Integer.valueOf(entry.getValue().toString())
					);
				}

				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				JackpotParticipant jackpotParticipant = new JackpotParticipant(offlinePlayer, participationLog);
				this.participants.add(jackpotParticipant);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}

	public FileConfiguration getFileConfiguration() {
		return ECJackpot.getINSTANCE().getConfigurationManager().getConfigurationFile(jackpotFile.getName());
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

	public File getDataFolder() {
		return new File(
				ECJackpot.getINSTANCE().getDataFolder().getAbsolutePath() + File.separatorChar + "data"
		);
	}

	public File getDataFile() {
		return new File(getDataFolder().getAbsolutePath() + File.separatorChar + name + ".json");
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

	public void saveData() {
		File dataFolder = getDataFolder();

		if (dataFolder.mkdir()) {
			ECJackpot.getINSTANCE().log(
					Level.INFO,
					dataFolder.getName() + " has been created successfully."
			);
		}

		File dataFile = getDataFile();

		try {
			if (dataFile.createNewFile()) {
				ECJackpot.getINSTANCE().log(
						Level.INFO,
						dataFile.getName() + " has been created successfully."
				);
			}
		} catch (IOException ignored) {}

		Map<String, Object> elementsMap = new HashMap<>();
		elementsMap.put("pot", this.pot);
		elementsMap.put("lastParticipant", this.lastParticipant);
		elementsMap.put("participants", this.participants);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		try {
			JsonElement jsonElement = gson.toJsonTree(elementsMap);
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dataFile), StandardCharsets.UTF_8);
			String jsonContent = gson.toJson(jsonElement);

			writer.write(jsonContent);
			writer.flush();
			writer.close();

			ECJackpot.getINSTANCE().log(
					Level.INFO,
					dataFile.getName() + " has been updated successfully."
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addParticipation(OfflinePlayer offlinePlayer, int amount) {
		AtomicReference<JackpotParticipant> participantAtomicReference;
		participantAtomicReference = new AtomicReference<>(null);

		this.participants.forEach(value -> {
			if (value.getUuid().equals(offlinePlayer.getUniqueId())) {
				participantAtomicReference.set(value);
			}
		});

		if (participantAtomicReference.get() == null) {
			JackpotParticipant participant = new JackpotParticipant(offlinePlayer);
			participantAtomicReference.set(participant);
			this.participants.add(participant);
		}

		JackpotParticipant participant = participantAtomicReference.get();
		participant.addParticipation(amount);

		this.lastParticipant = participant.getUuid();
		this.pot += amount;
	}

	public List<JackpotParticipant> getParticipants() {
		return participants;
	}

	public UUID getLastParticipant() {
		return lastParticipant;
	}

	public int getPot() {
		return pot;
	}
}
