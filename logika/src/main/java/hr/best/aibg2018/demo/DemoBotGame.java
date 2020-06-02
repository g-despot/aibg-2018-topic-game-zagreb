package hr.best.aibg2018.demo;

import hr.best.aibg2018.bots.NotSoRandomBot;
import hr.best.aibg2018.logic.game.GameException;
import hr.best.aibg2018.logic.game.Game;
import hr.best.aibg2018.logic.map.Map;
import hr.best.aibg2018.logic.entites.Player;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DemoBotGame {

	public static void main(String[] args) {
		Map map = new Map(Paths.get("./maps/test/mapConfig6.txt"));
		Player p1 = new Player(4, 4, 1);
		Player p2 = new Player(5, 5, 2);

		Game game = new Game(map, p1, p2);

		System.out.println("--------------------------");

		System.out.println("Mapa je:\n" + map.toString());

		System.out.println("--------------------------");

		System.out.println("Game kaze da je ovakva:\n" + game.toString());

		System.out.println("--------------------------");

		int gameLength = 0;
		int playerFocus = 1;
		List<NotSoRandomBot> bots = Arrays.asList(new NotSoRandomBot(1, 1), new NotSoRandomBot(1, 2));
		System.out.println("Igra igrač " + playerFocus);
		while (true) {
			gameLength++;
			String input = bots.get(game.getNextPlayer().getId() - 1).act("");
			input = input.trim();

			try {
				game.doAction(input, game.getNextPlayer().getId());
			} catch (GameException e) {
				System.out.println(e.getMessage());
				break;
			}

			System.out.println(game.toString());

			int winner = 0;
			if (game.getPlayer1().getHealth() <= 0) {
				winner = 2;
			} else if (game.getPlayer2().getHealth() <= 0) {
				winner = 1;
			}

			if (winner != 0) {
				System.out.println("CONGRATULATIONS, player " + winner + " wins!");
				System.out.println("Game ended " + gameLength + " turns.");
				break;

			}

			System.out.println("player one type: " + game.getPlayer1().getType());
			System.out.println("player one health: " + game.getPlayer1().getHealth());
			System.out.println("player two type: " + game.getPlayer2().getType());
			System.out.println("player two health: " + game.getPlayer2().getHealth());
			System.out.println("\n\n--------------------------");
			System.out.println("Igra igrač " + playerFocus);
		}
	}

}
