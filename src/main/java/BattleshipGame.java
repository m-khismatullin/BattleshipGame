import java.util.Objects;
import java.util.Scanner;

/**
 * Класс для запуска
 */
public class BattleshipGame {
    public static void main(String[] args) {
        // размещаемые корабли
        Shiptype[] shiptypes = {Shiptype.CARRIER, Shiptype.BATTLESHIP, Shiptype.SUBMARINE, Shiptype.CRUISER, Shiptype.DESTROYER};

        Scanner scanner = new Scanner(System.in);

        // создаю игровое поле
        Gameboard gameboard = new Gameboard(new Battlefield(shiptypes.length), new Battlefield(shiptypes.length));
        gameboard.clear();

        Battlefield[] battlefields = new Battlefield[2];
        battlefields[0] = gameboard.getUpperBattlefield();
        battlefields[1] = gameboard.getLowerBattlefield();

        int playerNumber;
        for (int i = 0; i < battlefields.length; i++) {
            Battlefield battlefield = battlefields[i];
            playerNumber = i + 1;

            System.out.println("Player " + playerNumber + ", place your ships on the game field");
            // вывожу поле
            System.out.println(battlefield);


            // размещение кораблей
            boolean stopFlag;
            for (Shiptype shiptype : shiptypes) {
                stopFlag = false;
                while (!stopFlag) {
                    try {
                        System.out.println("Enter the coordinates of the " + shiptype.getDesc() + " (" + shiptype.getLength() + " cells):");

                        battlefield.putShipToPositions(
                                shiptype,
                                battlefield.getFieldByPosition(new String(scanner.next()).trim(), PositionEventTypes.PLACEMENT),
                                battlefield.getFieldByPosition(new String(scanner.next()).trim(), PositionEventTypes.PLACEMENT)
                        );

                        stopFlag = true;
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println(battlefield);
            }

            //if (i == 0) {
                scanner.nextLine();
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();

                gameboard.clear();
            //}
        }

        // внизу при старте должно быть поле первого игрока
        gameboard.swapFields();

        boolean stopFlag = false;
        while (!stopFlag) {
            gameboard.setRegime();
            try {
                System.out.println(gameboard);
                System.out.println("Player " + gameboard.getUpperPlayerNum() + ", it's your turn:");

                Battlefield battlefield = gameboard.getUpperBattlefield();
                ShotResultTypes shotResult = battlefield.takeShot(battlefield.getFieldByPosition(new String(scanner.next()).trim(), PositionEventTypes.SHOOTING));
                System.out.println(shotResult.getText());

                stopFlag = shotResult.isStop();

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

            if (!stopFlag) {
                scanner.nextLine();
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();

                gameboard.clear();
                gameboard.swapFields();
            }
        }

        scanner.close();
    }
}





