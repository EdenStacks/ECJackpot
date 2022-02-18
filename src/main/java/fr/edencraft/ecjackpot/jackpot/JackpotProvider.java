package fr.edencraft.ecjackpot.jackpot;

import fr.edencraft.ecjackpot.ECJackpot;
import fr.edencraft.ecjackpot.utils.ColoredText;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class JackpotProvider implements InventoryProvider {

	private final Jackpot jackpot;

	public JackpotProvider(Jackpot jackpot) {
		this.jackpot = jackpot;
	}

	@Override
	public void init(Player player, InventoryContents contents) {

	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}

	private SmartInventory getSmartInventory() {
		return SmartInventory.builder()
				.id(jackpot.getName())
				.provider(this)
				.size(5, 9)
				.title(new ColoredText(jackpot.getDisplayName()).treat())
				.manager(ECJackpot.getINSTANCE().getInventoryManager())
				.build();
	}

	public void openInv(Player player) {
		getSmartInventory().open(player);
	}

}
