package fr.edencraft.ecjackpot.jackpot;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JackpotCommand extends Command {

	private final Jackpot jackpot;
	private final Set<String> subCommand = Set.of("info", "add");

	protected JackpotCommand(@NotNull String name,
							 @NotNull String description,
							 @NotNull String usageMessage,
							 @NotNull List<String> aliases,
							 @NotNull Permission permission,
							 @NotNull Jackpot jackpot) {
		super(name);
		this.setPermission(permission.getName());
		this.setAliases(aliases);
		this.setUsage(usageMessage);
		this.setDescription(description);
		this.jackpot = jackpot;
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("§cLa commande est reservé aux joueurs.");
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("zizi")) {
			ComponentBuilder componentBuilder = new ComponentBuilder("Ahah très drôle gros con.");
			BaseComponent component = componentBuilder.getComponent(0);
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§6Baisse les yeux !")));
			player.spigot().sendMessage(component);
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(usageMessage);
			return true;
		}

		if (subCommand.contains(args[0])) {
			switch (args[0]) {
				case "info":
					return subOpen(player, commandLabel, substractFirstArg(args));
				case "add":
					return subAdd(player, commandLabel, substractFirstArg(args));
			}
		} else {
			player.sendMessage(usageMessage);
			return true;
		}
		return false;
	}

	private String[] substractFirstArg(String[] args) {
		List<String> subArgs = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
		Object[] newArgs = subArgs.toArray();
		return subArgs.toArray(new String[0]);
	}

	private boolean isDigit(String str) {
		for (char ch : str.toCharArray()) {
			if (!Character.isDigit(ch)) return false;
		}
		return true;
	}

	public boolean subOpen(@NotNull Player player, @NotNull String commandLabel, @NotNull String[] args) {
		jackpot.getJackpotProvider().openInv(player);
		return true;
	}

	public boolean subAdd(@NotNull Player player, @NotNull String commandLabel, @NotNull String[] args) {
		if (args.length != 1) {
			player.sendMessage("§cUtilisez §e/" + commandLabel + " §aadd §d<montant>");
			return true;
		}

		if (!isDigit(args[0])) {
			player.sendMessage("§cVous devez saisir un nombre.");
			return true;
		}

		int amount = Integer.parseInt(args[0]);

		if (amount == 0) {
			player.sendMessage("§cVous devez saisir un nombre plus grand.");
			return true;
		}

		String currency;
		if (jackpot.getCurrencyType().equalsIgnoreCase("ITEM")) {
			currency = " " + jackpot.getMaterial();
			Material material = Material.getMaterial(jackpot.getMaterial());

			if (amount > countMaterialInPlayerInventory(player.getInventory(), material)) {
				player.sendMessage("§cVous n'avais pas assez de§e" + currency);
				return true;
			}

			if (amount > jackpot.getAmountMissing()) {
				player.sendMessage("§cVous allez faire déborder la cagnotte ! Il manque §e" + jackpot.getAmountMissing() +
						" §cpour remplir le pot.");
				return true;
			}

			int supressedCount = removeFromPlayerInventory(
					player.getInventory(),
					material,
					amount
			);

			player.sendMessage("§c" + supressedCount + "§6" + currency + " §eont été retiré de votre inventaire.");

		} else {
			currency = "$";
		}

		player.sendMessage( "§aAjout de §e" + amount + currency);
		jackpot.addParticipation(player, amount);
		return true;
	}

	/**
	 * @param playerInventory inventory of the player.
	 * @param material material to count.
	 * @return amount of a material in the {@link PlayerInventory}.
	 */
	private int countMaterialInPlayerInventory(PlayerInventory playerInventory, Material material) {
		int count = 0;
		for (ItemStack itemStack : playerInventory.getContents()) {
			if (itemStack != null && itemStack.getType().equals(material)) {
				count += itemStack.getAmount();
			}
		}

		return count;
	}

	/**
	 * This method remove the material while amount to remove is not reached.
	 * If no more material in {@link PlayerInventory} it does not throw error.
	 *
	 * @param playerInventory inventory of the player.
	 * @param material material to remove.
	 * @param amount amount to remove.
	 * @return number of supressed item.
	 */
	private int removeFromPlayerInventory(PlayerInventory playerInventory, Material material, int amount) {
		int supressedCount = 0;

		for (ItemStack itemStack : playerInventory.getContents()) {
			if (itemStack != null && itemStack.getType().equals(material)) {

				int itemAmount = itemStack.getAmount();
				if (itemAmount >= amount) {
					itemStack.setAmount(itemAmount - amount);
					supressedCount += amount;
					break;
				} else {
					itemStack.setAmount(0);
					supressedCount += itemAmount;
				}

				amount -= itemAmount;
			}
		}

		return supressedCount;
	}
}
