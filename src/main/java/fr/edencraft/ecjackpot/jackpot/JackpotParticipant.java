package fr.edencraft.ecjackpot.jackpot;

import org.bukkit.OfflinePlayer;

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
