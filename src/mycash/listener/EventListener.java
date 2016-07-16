package mycash.listener;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import mycash.Main;
import mycash.cash.Account;
import mycash.exception.PlayerAlreadyHaveAccountException;
import mycash.manager.CashManager;

public class EventListener implements Listener {
	private Main plugin;
	public EventListener(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equals(getMessage("commands-cash"))) {
			if (args.length < 1) { // 명령어 아무것도 입력 안할시 usage 전송
				alert(sender, getMessage("commands-cash-usage"));
				return true;
			}
			if (args[0].toLowerCase().equals(getMessage("commands-mycash"))) { // 내캐쉬 명령어 입력시 현재 플레이어가 보유중인 캐쉬 전송
				message(sender, getMessage("show-cash").replace("%cash", String.valueOf(new Account((Player) sender).getCash())));
				return true;
			} else if (args[0].toLowerCase().equals(getMessage("commands-charge"))) { //충전 명령어 입력시
				String pinNumber = String.join("-", popArray(args));
				if (!CashManager.getInstance().isRightPinNumber(pinNumber)) {
					alert(sender, getMessage("not-right-pin").replace("%pin", pinNumber));
					return true;
				}
				
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T[] popArray(T[] array) {
		List<T> newArray = new ArrayList<>();
		for (int i = 1; i < array.length; i++) {
			newArray.add(array[i]);
		}
		return (T[]) newArray.toArray();
	}
	
	public Main getPlugin() {
		return plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		try {
			CashManager.getInstance().initCash(event.getPlayer());
		} catch (PlayerAlreadyHaveAccountException e) {
			e.printStackTrace();
		}
	}
	
	private String getMessage(String msg) {
		return plugin.getDB().get(msg);
	}
	private void message(CommandSender player, String msg) {
		plugin.getDB().message(player, msg);
	}
	private void alert(CommandSender player, String msg) {
		plugin.getDB().alert(player, msg);
	}
}
