/**
 * Собственно игровое пространство
 */
public class Gameboard {
    private Battlefield upperBattlefield;
    private Battlefield lowerBattlefield;
    private int upperPlayerNum;
    private static final String fieldsDelimiter = "---------------------";

    public Gameboard(Battlefield upperBattlefield, Battlefield lowerBattlefield) {
        this.upperBattlefield = upperBattlefield;
        this.lowerBattlefield = lowerBattlefield;
        this.upperPlayerNum = 1;
    }

    public void setRegime() {
        upperBattlefield.setRegime(BattlefieldViewRegime.ALIEN);
        lowerBattlefield.setRegime(BattlefieldViewRegime.MY);
    }

    public void swapFields() {
        upperPlayerNum = upperPlayerNum == 1 ? 2 : 1;
        Battlefield field = upperBattlefield;
        upperBattlefield = lowerBattlefield;
        lowerBattlefield = field;
        setRegime();
    }

    public void clear() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public String toString() {
        return "%s\n%s\n%s\n".formatted(upperBattlefield.toString().trim(), fieldsDelimiter, lowerBattlefield.toString().trim());
    }

    public Battlefield getUpperBattlefield() {
        return upperBattlefield;
    }

    public Battlefield getLowerBattlefield() {
        return lowerBattlefield;
    }

    public int getUpperPlayerNum() {
        return upperPlayerNum;
    }
}
