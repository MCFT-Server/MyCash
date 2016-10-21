package mycash.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.nukkit.Player;
import mycash.cash.Account;
import mycash.database.DataBase;
import mycash.exception.PlayerAlreadyHaveAccountException;

public class CashManager {
	private static CashManager instance;
	
	public CashManager() {
		if (instance == null) instance = this;
	}
	public DataBase getDB() {
		return DataBase.getInstance();
	}
	
	public static CashManager getInstance() {
		return instance;
	}
	
	
	public void initCash(Player player) throws PlayerAlreadyHaveAccountException {
		initCash(player.getName());
	}
	public void initCash(String player) throws PlayerAlreadyHaveAccountException {
		if (hasAccount(player)) {
			throw new PlayerAlreadyHaveAccountException();
		}
		getDB().getDB("cash").set(player.toLowerCase(), getDB().getDB("config").getInt("default-cash", 0));
	}
	
	
	public boolean hasAccount(Player player) {
		return hasAccount(player.getName());
	}
	public boolean hasAccount(String player) {
		player = player.toLowerCase();
		if (getDB().getDB("cash").getInt(player.toLowerCase(), -1) == -1) {
			return false;
		}
		return true;
	}
	
	public Account getAccount(Player player) {
		return new Account(player);
	}
	public Account getAccount(String player) {
		return new Account(player);
	}
	
	/**
	 * 
	 * @param pinNumber 
	 * pinNumber pattern is xxxx-xxxx-xxxx-xxxxxx or xxxx-xxxx-xxxx-xxxx
	 * @return boolean
	 */
	public boolean isRightPinNumber(String pinNumber) {
		return Pattern.matches(".... .... .... ....", pinNumber) ? true : Pattern.matches(".... .... .... ......", pinNumber);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void givePermission(String player) {
		List list = getDB().getDB("permlist").getList("list", new ArrayList<>());
		list.add(player.toLowerCase());
		getDB().getDB("permlist").set("list", list);
	}
	
	@SuppressWarnings("rawtypes")
	public void removePermission(String player) {
		List list = getDB().getDB("permlist").getList("list", new ArrayList<>());
		list.remove(player.toLowerCase());
		getDB().getDB("permlist").set("list", list);
	}
	
	public boolean hasPermission(String player) {
		return getDB().getDB("permlist").getList("list", new ArrayList<>()).contains(player.toLowerCase());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addLog(String player, String ref) {
		getDB().getDB("waitlist").getList(player.toLowerCase(), new ArrayList<>()).forEach(pin -> {
			List list = getDB().getDB("log").getList(player.toLowerCase(), new ArrayList<>());
			list.add(pin + ", " + ref);
			getDB().getDB("log").set(player.toLowerCase(), list);
		});
	}
	
	@SuppressWarnings("rawtypes")
	public List getLog(String player) {
		return getDB().getDB("log").getList(player.toLowerCase(), new ArrayList<>());
	}
}
