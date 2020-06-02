package hr.best.aibg2018.demo;

import hr.best.aibg2018.logic.entites.Player;
import hr.best.aibg2018.logic.game.GameException;
import hr.best.aibg2018.logic.map.Map;
import hr.best.aibg2018.logic.map.ItemType;

import java.nio.file.Paths;
import java.util.Scanner;

import hr.best.aibg2018.logic.game.Game;

public class DemoGame {

	public static void main(String[] args) {
		Map map = new Map(Paths.get("./maps/test/mapConfig6.txt"));
		Player p1 = new Player(9, 9, 1);
		Player p2 = new Player(10, 10, 2);
		p1.addMorphItem(ItemType.FIRE);
		p2.addMorphItem(ItemType.WATER);

		Game game = new Game(map, p1, p2);

		System.out.println("--------------------------");

		System.out.println("Mapa je:\n" + map.toString());

		System.out.println("--------------------------");

		System.out.println("Game kaze da je ovakva:\n" + game.toString());

		System.out.println("--------------------------");

		Scanner sc = new Scanner(System.in);
		int playerFocus = 1;
		System.out.println("Igra igrač " + playerFocus);
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			input = input.trim();
			if (input.equals("exit")) {
				System.out.println("Goodbye");
				break;
			}

			try {
				game.doAction(input, playerFocus);
			} catch (GameException e) {
				System.out.println(e.getMessage());
			}

			playerFocus = playerFocus == 1 ? 2 : 1;
			System.out.println(game.toString());

			if (game.winner != null) {
				if (game.winner == 0) {
					System.out.println("It is a draw!");
				} else {
					System.out.println("CONGRATULATIONS, player " + game.winner + " wins!");
				}
				break;

			}

			System.out.println("player one type: " + game.getPlayer1().getType());
			System.out.println("player one health: " + game.getPlayer1().getHealth());
			System.out.println("player one lives: " + game.getPlayer1().getLives());
			System.out.println("player one morph items: " + game.getPlayer1().getMorphItems());
			System.out.println("player two type: " + game.getPlayer2().getType());
			System.out.println("player two health: " + game.getPlayer2().getHealth());
			System.out.println("player two lives: " + game.getPlayer2().getLives());
			System.out.println("player two morph items: " + game.getPlayer2().getMorphItems());
			System.out.println("\n\n--------------------------");
			System.out.println("Potez: " + game.getTurn());
			System.out.println("Igra igrač " + playerFocus);
		}
		sc.close();
	}

}
