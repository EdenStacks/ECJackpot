package fr.edencraft.ecjackpot.jackpot;

import fr.edencraft.ecjackpot.ECJackpot;
import fr.edencraft.ecjackpot.utils.ColoredText;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class JackpotProvider implements InventoryProvider {

	private final Jackpot jackpot;

	public JackpotProvider(Jackpot jackpot) {
		this.jackpot = jackpot;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		setDecorationContent(player, contents);
		setConfigurableContent(player, contents);
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		setConfigurableContent(player, contents);
	}

	/**
	 * Put static item into the inventory.
	 *
	 * @param player player that opened the inventory.
	 * @param contents {@link InventoryContents} if the inventory.
	 */
	private void setDecorationContent(Player player, InventoryContents contents) {
		Map<SlotPos, Material> stainedGlassPaneMap= new HashMap<>()
		{{
			put(new SlotPos(0, 1), Material.PINK_STAINED_GLASS_PANE);
			put(new SlotPos(0, 7), Material.PINK_STAINED_GLASS_PANE);
			put(new SlotPos(1, 0), Material.PINK_STAINED_GLASS_PANE);
			put(new SlotPos(1, 8), Material.PINK_STAINED_GLASS_PANE);
			put(new SlotPos(2, 1), Material.PINK_STAINED_GLASS_PANE);
			put(new SlotPos(2, 7), Material.PINK_STAINED_GLASS_PANE);

			put(new SlotPos(0, 0), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(0, 2), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(0, 6), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(0, 8), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(1, 2), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(1, 6), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(2, 0), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(2, 2), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(2, 6), Material.YELLOW_STAINED_GLASS_PANE);
			put(new SlotPos(2, 8), Material.YELLOW_STAINED_GLASS_PANE);

			put(new SlotPos(0, 3), Material.BLACK_STAINED_GLASS_PANE);
			put(new SlotPos(0, 5), Material.BLACK_STAINED_GLASS_PANE);
			put(new SlotPos(1, 3), Material.BLACK_STAINED_GLASS_PANE);
			put(new SlotPos(1, 4), Material.BLACK_STAINED_GLASS_PANE);
			put(new SlotPos(1, 5), Material.BLACK_STAINED_GLASS_PANE);
		}};
		stainedGlassPaneMap.forEach((slotPos, material) -> {
			ItemStack itemStack = new ItemStack(material);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.displayName(Component.newline().content(""));
			itemStack.setItemMeta(itemMeta);

			contents.set(slotPos, ClickableItem.empty(itemStack));
		});
	}

	private void setConfigurableContent(Player player, InventoryContents contents) {
		int state = contents.property("state", 0);
		contents.setProperty("state", state + 1);
		if (state % 10 != 0) return;

		FileConfiguration fileConfiguration = jackpot.getFileConfiguration();

		ItemStack jackpotPot = new ItemStack(Material.CAULDRON);
		ItemMeta itemMeta = jackpotPot.getItemMeta();
		List<String> lore = List.of(new ColoredText(
				fileConfiguration.getString("menu.jackpot-information")).treat().split("\n")
		);
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(new ColoredText(jackpot.getDisplayName()).treat());
		jackpotPot.setItemMeta(itemMeta);
		ClickableItem clickableJackpotPot = ClickableItem.of(jackpotPot, inventoryClickEvent -> {
			player.sendMessage("Clicked");
		});

		ItemStack jackpotLastParticipant = new ItemStack(Material.PLAYER_HEAD);
		itemMeta = jackpotLastParticipant.getItemMeta();
		lore = List.of(new ColoredText(
				fileConfiguration.getString("menu.last-participant")).treat().split("\n")
		);
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(new ColoredText("&3Dernier participant: ").treat());
		jackpotLastParticipant.setItemMeta(itemMeta);
		ClickableItem clickableLastParticipant = ClickableItem.of(jackpotLastParticipant, inventoryClickEvent -> {
			player.sendMessage("Clicked");
		});

		ItemStack jackpotBestParticipant = new ItemStack(Material.PLAYER_HEAD);
		itemMeta = jackpotBestParticipant.getItemMeta();
		lore = List.of(new ColoredText(
				fileConfiguration.getString("menu.best-participant")).treat().split("\n")
		);
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(new ColoredText("&3Plus grand donateur: ").treat());
		jackpotBestParticipant.setItemMeta(itemMeta);
		ClickableItem clickableBestParticipant = ClickableItem.of(jackpotBestParticipant, inventoryClickEvent -> {
			player.sendMessage("Clicked");
		});

		ItemStack jackpotListParticipant = new ItemStack(Material.PAPER);
		itemMeta = jackpotListParticipant.getItemMeta();
		lore = List.of(new ColoredText(
				fileConfiguration.getString("menu.list-participant")).treat().split("\n")
		);
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(new ColoredText("&3Liste de participants: ").treat());
		jackpotListParticipant.setItemMeta(itemMeta);
		ClickableItem clickableListParticipant = ClickableItem.of(jackpotListParticipant, inventoryClickEvent -> {
			player.sendMessage("Clicked");
		});

		ItemStack jackpotRules = new ItemStack(Material.BOOK);
		itemMeta = jackpotRules.getItemMeta();
		lore = List.of(new ColoredText(
				fileConfiguration.getString("menu.jackpot-rules")).treat().split("\n")
		);
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(new ColoredText("&3Règles du jackpot: ").treat());
		jackpotRules.setItemMeta(itemMeta);
		ClickableItem clickableRules = ClickableItem.of(jackpotRules, inventoryClickEvent -> {
			player.sendMessage("Clicked");
		});

		ItemStack jackpotReward = new ItemStack(Material.BOOK);
		itemMeta = jackpotReward.getItemMeta();
		lore = List.of(new ColoredText(
				fileConfiguration.getString("menu.jackpot-reward")).treat().split("\n")
		);
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(new ColoredText("&3Règles du jackpot: ").treat());
		jackpotReward.setItemMeta(itemMeta);
		ClickableItem clickableReward = ClickableItem.of(jackpotReward, inventoryClickEvent -> {
			player.sendMessage("Clicked");
		});

		contents.set(new SlotPos(0, 4), clickableJackpotPot);
		contents.set(new SlotPos(2, 3), clickableLastParticipant);
		contents.set(new SlotPos(2, 5), clickableBestParticipant);
		contents.set(new SlotPos(2, 4), clickableListParticipant);
		contents.set(new SlotPos(1, 1), clickableRules);
		contents.set(new SlotPos(1, 7), clickableReward);
	}

	private SmartInventory getSmartInventory() {
		return SmartInventory.builder()
				.id(jackpot.getName())
				.provider(this)
				.size(3, 9)
				.title(new ColoredText(jackpot.getDisplayName()).treat())
				.manager(ECJackpot.getINSTANCE().getInventoryManager())
				.build();
	}

	public void openInv(Player player) {
		getSmartInventory().open(player);
	}

}
