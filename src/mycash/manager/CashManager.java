package mycash.manager;

import cn.nukkit.Player;
import mycash.Main;
import mycash.database.DataBase;
import mycash.exception.PlayerAlreadyHaveAccountException;
import mycash.exception.PlayerNotHaveAccountException;
import mycash.exception.PlayerNotHaveEnoughCashExeception;

public class CashManager {
	private Main plugin;
	private static CashManager instance;
	
	public CashManager(Main plugin) {
		this.plugin = plugin;
		instance = this;
	}
	public DataBase getDB() {
		return plugin.getDB();
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
		getDB().getDB("cash").set(player, getDB().getDB("config").getInt("default-cash", 0));
	}
	
	public void addCash(Player player, int cash) throws PlayerNotHaveAccountException {
		addCash(player.getName(), cash);
	}
	public void addCash(String player, int cash) throws PlayerNotHaveAccountException {
		player = player.toLowerCase();
		getDB().getDB("cash").set(player, getCash(player) + cash);
	}
	
	public int getCash(Player player) throws PlayerNotHaveAccountException {
		return getCash(player.getName());
	}
	public int getCash(String player) throws PlayerNotHaveAccountException {
		player = player.toLowerCase();
		if (!hasAccount(player)) {
			throw new PlayerNotHaveAccountException();
		}
		return getDB().getDB("cash").getInt(player);
	}
	
	public void reduceCash(Player player, int cash) throws PlayerNotHaveAccountException, PlayerNotHaveEnoughCashExeception {
		reduceCash(player.getName(), cash);
	}
	public void reduceCash(String player, int cash) throws PlayerNotHaveAccountException, PlayerNotHaveEnoughCashExeception {
		player = player.toLowerCase();
		int playercash = getCash(player);
		if (cash > playercash) {
			throw new PlayerNotHaveEnoughCashExeception();
		}
		getDB().getDB("cash").set(player, playercash - cash);
	}
	
	public boolean hasAccount(Player player) {
		return hasAccount(player.getName());
	}
	public boolean hasAccount(String player) {
		player = player.toLowerCase();
		if (getDB().getDB("cash").getInt(player, -1) == -1) {
			return false;
		}
		return true;
	}
}
