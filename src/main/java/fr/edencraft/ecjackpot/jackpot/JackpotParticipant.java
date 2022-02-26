package fr.edencraft.ecjackpot.jackpot;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JackpotParticipant {

	private final String name;
	private final UUID uuid;
	private final Map<Long, Integer> participationLog;

	public JackpotParticipant(OfflinePlayer offlinePlayer) {
		this.name = offlinePlayer.getName();
		this.uuid = offlinePlayer.getUniqueId();
		this.participationLog = new HashMap<>();
	}

	public JackpotParticipant(OfflinePlayer offlinePlayer, HashMap<Long, Integer> logs) {
		this.name = offlinePlayer.getName();
		this.uuid = offlinePlayer.getUniqueId();
		this.participationLog = logs;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Map<Long, Integer> getParticipationLog() {
		return participationLog;
	}

	public void addParticipation(int amount) {
		participationLog.put(System.currentTimeMillis(), amount);
	}
}
