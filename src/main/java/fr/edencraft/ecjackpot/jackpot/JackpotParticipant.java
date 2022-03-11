package fr.edencraft.ecjackpot.jackpot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import fr.edencraft.ecjackpot.ECJackpot;
import fr.edencraft.ecjackpot.utils.JsonUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JackpotParticipant {

	private final String name;
	private final UUID uuid;
	private Map<Long, Integer> participationLog;

	public JackpotParticipant(OfflinePlayer offlinePlayer) {
		this.name = offlinePlayer.getName();
		this.uuid = offlinePlayer.getUniqueId();
		this.participationLog = new HashMap<>();
		createDataFile();
	}

	public JackpotParticipant(OfflinePlayer offlinePlayer, HashMap<Long, Integer> logs) {
		this(offlinePlayer);
		this.participationLog = logs;
	}

	public boolean isOnline() {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getUuid());

		return offlinePlayer.isOnline();
	}

	private void createDataFile() {
		File dataFile = getDataFile();
		if (!dataFile.exists()) {
			try {
				boolean mkdirs = dataFile.getParentFile().mkdirs();
				boolean created = dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File getDataFile() {
		return new File(ECJackpot.getINSTANCE().getDataFolder().getAbsolutePath() + File.separatorChar + "data"
				+ File.separatorChar + "users" + File.separatorChar + uuid.toString() + ".json");
	}

	private HashMap<String, Object> getData() {
		HashMap<String, Object> dataMap = new HashMap<>();

		File dataFile = getDataFile();
		if (!dataFile.exists()) {
			createDataFile();
		}
		try (FileReader reader = new FileReader(dataFile)) {
			JsonElement jsonElement = JsonParser.parseReader(reader);
			List<String> waitingCommands = new ArrayList<>();
			if (jsonElement.isJsonObject()) {
				JsonArray jsonArray = jsonElement.getAsJsonObject().get("waitingCommands").getAsJsonArray();
				jsonArray.forEach(element -> waitingCommands.add(element.getAsString()));
			}
			dataMap.put("waitingCommands", waitingCommands.toArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataMap;
	}

	/**
	 * @param command command to execute. "{name}" is replaced by {@link Player#getName()}
	 */
	public void executeCommand(String command) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getUuid());

		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replaceAll("\\{name}", offlinePlayer.getName()));
	}

	private void saveData(HashMap<String, Object> data) {
		File dataFile = getDataFile();
		Gson gson = new Gson();
		System.out.println("Saving");

		JsonUtils.writeJsonData(dataFile, gson, data);
	}

	/**
	 * @param command add command to execute later for this {@link JackpotParticipant}.
	 */
	public void addWaitingCommand(String command) {
		HashMap<String, Object> data = getData();
		Object value = data.get("waitingCommands");
		if (value.getClass().isArray()) {
			List<Object> list = new ArrayList<>(List.of(value));
			list.add(command);
			data.put("waitingCommands", list);
			saveData(data);
		} else {
			data.put("waitingCommands", new ArrayList<>(List.of(command)));
			saveData(data);
		}

	}

	/**
	 * @param command remove command that will be executed later for this {@link JackpotParticipant}.
	 */
	public void removeWaitingCommand(String command) {
		HashMap<String, Object> data = getData();
		Object value = data.get("waitingCommands");
		if (value == null) return;
		if (value.getClass().isArray()) {
			List<Object> list = new ArrayList<>(List.of(value));
			list.remove(command);
			data.put("waitingCommands", list);
			saveData(data);
		}
	}

	/**
	 * @return list of all waiting command of this {@link JackpotParticipant}.
	 */
	public List<String> getWaitingCommands() {
		List<String> waitingCommands = new ArrayList<>();

		HashMap<String, Object> data = getData();
		Object value = data.get("waitingCommands");
		if (value != null && value.getClass().isArray()) {
			List<Object> list = new ArrayList<>(List.of(value));
			for (Object obj : list) {
				if (obj.getClass().isInstance(String.class)) {
					waitingCommands.add((String) obj);
				}
			}
		}

		return waitingCommands;
	}

	public int getParticipationAmount() {
		int amount = 0;

		for (int value : this.participationLog.values()) {
			amount += value;
		}

		return amount;
	}

	/**
	 * @return time in millis of last participation of this {@link JackpotParticipant}.
	 */
	public long getLastParticipationMillis() {
		if (this.participationLog.isEmpty()) return 0;

		long lastParticipationTime = 0;
		for (long value : this.participationLog.keySet()) {
			lastParticipationTime = Math.max(value, lastParticipationTime);
		}
		return lastParticipationTime;
	}

	/**
	 * @return amount of the last participation of this {@link JackpotParticipant}.
	 */
	public int getLastParticipationAmount() {
		return this.participationLog.get(getLastParticipationMillis());
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void addParticipation(int amount) {
		participationLog.put(System.currentTimeMillis(), amount);
	}
}
