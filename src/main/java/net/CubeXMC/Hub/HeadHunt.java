package net.CubeXMC.Hub;

import java.util.ArrayList;

public class HeadHunt {

	private final String name;
	private final ArrayList<String> collectedSkulls = new ArrayList<>();
	private final ArrayList<String> achievements = new ArrayList<>();
	//private int Coins;

	public HeadHunt(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> collectedSkulls() {
		return this.collectedSkulls;
	}

	public boolean collectSkull(String name) {
		if (!collectedSkulls.contains(name)) {
			collectedSkulls.add(name);

			return true;
		}
		return false;
	}

	public ArrayList<String> getAchievements() {
		return achievements;
	}

	public void addAche(String achie) {
		if (!achievements.contains(achie)) {
			achievements.add(achie);
		}
	}

	/*public int getCoins() {
		return Coins;
	}

	public void setCoins(int coins) {
		Coins = coins;
	}*/

}