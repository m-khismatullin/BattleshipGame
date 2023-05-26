/**
 * Перечисление для типов судов
 */
enum Shiptype {
    CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    DESTROYER(2, "Destroyer");

    private int length;
    private String desc;

    Shiptype(int length, String desc) {
        this.length = length;
        this.desc = desc;
    }

    public int getLength() {
        return length;
    }

    public String getDesc() {
        return desc;
    }
}

/**
 * Корабль
 */
public class Ship {
    private Shiptype shiptype;
    private Field[] fields;

    public Ship(Shiptype shiptype, Field[] fields) {
        /*
        // проверять на соответствие штатной длины корабля и массива переданных полей лучше здесь по ООПBa?!
        if (shiptype.getLength() != Field.getSectionLength(fields[0], fields[fields.length - 1])) {
            throw new RuntimeException("Error! Wrong length of the " + shiptype.getDesc() + "! Try again:");
        }
         */

        this.shiptype = shiptype;
        this.fields = fields;

        for (Field field : fields) {
            field.setFieldType(Fieldtype.SHIP);
        }
    }

    public boolean isShipField(Field field) {
        for (Field shipField : fields) {
            if (shipField == field) {
                return true;
            }
        }
        return false;
    }

    public boolean isShipSank() {
        for (Field field : fields) {
            if (field.getFieldType() != Fieldtype.HIT) {
                return false;
            }
        }
        return true;
    }
}
