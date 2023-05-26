import java.util.Objects;

enum PositionEventTypes {
    PLACEMENT("ship placement","Error! Wrong ship location! Try again:"),
    SHOOTING("shooting", "Error! You entered the wrong coordinates! Try again:");

    private String desc;
    private String errorText;

    PositionEventTypes(String desc, String errorText) {
        this.desc = desc;
        this.errorText = errorText;
    }

    public String getDesc() {
        return desc;
    }

    public String getErrorText() {
        return errorText;
    }
}

enum ShotResultTypes {
    MISS(false, "You missed!"),
    HIT(false, "You hit a ship!"),
    SANK(false, "You sank a ship!"),
    VICTORY(true, "You sank the last ship. You won. Congratulations!");

    private boolean stop;
    private String text;

    ShotResultTypes(boolean stop, String text) {
        this.stop = stop;
        this.text = text;
    }

    public boolean isStop() {
        return stop;
    }

    public String getText() {
        return text;
    }

}

enum BattlefieldViewRegime {
    FOG,
    ALIEN,
    MY
}

public class Battlefield {
    private Field[][] fields;
    private Ship[] ships;
    private BattlefieldViewRegime regime;

    public Battlefield(int countOfShips) {
        // game fields
        fields = new Field[10][10];

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                fields[i][j] = new Field(Fieldtype.FOG, i, j);
            }
        }

        ships = new Ship[countOfShips];

        regime = BattlefieldViewRegime.MY;
    }

    public BattlefieldViewRegime getRegime() {
        return regime;
    }

    public void setRegime(BattlefieldViewRegime regime) {
        this.regime = regime;
    }

    @Override
    public String toString() {
        String lineBreak = "\n";
        String image = " ";
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0) {
                    if (j == 0) {
                        image += " ";
                    } else {
                        image += " " + j;
                    }
                } else {
                    if (j == 0) {
                        image += String.format(" %c", 64 + i);
                    } else {
                        Fieldtype fieldtype = fields[i - 1][j - 1].getFieldType();
                        char mask = fieldtype.getMask();
                        switch (getRegime()) {
                            case MY : {
                                image += String.format(" %c", mask);
                                break;
                            }
                            case FOG: {
                                image += String.format(" %c", Fieldtype.FOG.getMask());
                                break;
                            }
                            case ALIEN: {
                                image += String.format(" %c", (fieldtype == Fieldtype.HIT || fieldtype == Fieldtype.MISS) ? mask : Fieldtype.FOG.getMask());
                                break;
                            }
                        }
                    }
                }
            }
            image += lineBreak;
        }

        return image;
    }

    boolean checkPosition(Field[] fields) {
        int row;
        int col;
        for (Field field : fields) {
            if (field.getFieldType() == Fieldtype.SHIP) {
                return false;
            }
            row = field.getRow();
            col = field.getCol();
            for (int i = Math.max(0, row - 1); i <= Math.min(9, row + 1); i++) {
                for (int j = Math.max(0, col - 1); j <= Math.min(9, col + 1); j++) {
                    if (getFieldByFieldRowCol(i, j).getFieldType() == Fieldtype.SHIP) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void putShipToPositions(Shiptype shiptype, Field fieldFrom, Field fieldTo) {
        Field fieldBuf;

        if (fieldFrom.getRow() > fieldTo.getRow() || fieldFrom.getCol() > fieldTo.getCol()) {
            fieldBuf = fieldFrom;
            fieldFrom = fieldTo;
            fieldTo = fieldBuf;
        }

        if (fieldFrom.getRow() != fieldTo.getRow() && fieldFrom.getCol() != fieldTo.getCol()) {
            throw new RuntimeException("Error! Wrong ship location! Try again:");
        }

        if (Field.getSectionLength(fieldFrom, fieldTo) != shiptype.getLength()) {
            throw new RuntimeException("Error! Wrong length of the " + shiptype.getDesc() + "! Try again:");
        }

        int lastIndex = 0;

        // check that ships should not cross or touch each other
        for (Ship ship : ships) {
            if (Objects.isNull(ship)) {
                break;
            }
            lastIndex++;
        }

        Field[] fieldsForShip = getAllFieldsFromTo(fieldFrom, fieldTo);

        if (!checkPosition(fieldsForShip)) {
            throw new RuntimeException("Error! You placed it too close to another one. Try again:");
        }

        ships[lastIndex] = new Ship(shiptype, getAllFieldsFromTo(fieldFrom, fieldTo));
    }

    public Field getFieldByFieldRowCol(int fieldRow, int fieldCol) {
        return fields[fieldRow][fieldCol];
    }

    public Field getFieldByPosition(String strPos, PositionEventTypes positionEventTypes) {
        char charRow = strPos.charAt(0);
        int row;
        if (charRow <= 64 || charRow >= 75) {
            throw new RuntimeException(positionEventTypes.getErrorText());
        } else {
            row = charRow - 64;
        }

        int col;
        try {
            col = Integer.parseInt(strPos.substring(1));
        } catch (Exception e) {
            throw new RuntimeException(positionEventTypes.getErrorText());
        }
        if (col <= 0 || col >= 11) {
            throw new RuntimeException(positionEventTypes.getErrorText());
        }

        return getFieldByFieldRowCol(row - 1, col - 1); //fields[row - 1][col - 1];
    }

    public Field[] getAllFieldsFromTo(Field fieldFrom, Field fieldTo) {
        int length = Field.getSectionLength(fieldFrom, fieldTo);
        Field[] fields = new Field[length];
        fields[0] = fieldFrom;
        fields[length - 1] = fieldTo;
        for (int i = 1; i < length - 1; i++) {
            if (fieldFrom.getRow() == fieldTo.getRow()) {
                fields[i] = getFieldByFieldRowCol(fieldFrom.getRow(), fieldFrom.getCol() + i);
            } else {
                fields[i] = getFieldByFieldRowCol(fieldFrom.getRow() + i, fieldFrom.getCol());
            }
        }
        return fields;
    }

    public ShotResultTypes takeShot(Field field) {
        if (field.getFieldType() == Fieldtype.SHIP || field.getFieldType() == Fieldtype.HIT) {
            field.setFieldType(Fieldtype.HIT);
            if (getShipByField(field).isShipSank()) {
                for (Ship ship : ships) {
                    if (!ship.isShipSank()) {
                        return ShotResultTypes.SANK;
                    }
                }
                return ShotResultTypes.VICTORY;
            } else {
                return ShotResultTypes.HIT;
            }
        } else {
            field.setFieldType(Fieldtype.MISS);
            return ShotResultTypes.MISS;
        }
    }

    public Ship getShipByField(Field field) {
        Ship foundShip = null;
        for (Ship ship : ships) {
            if (ship.isShipField(field)) {
                foundShip = ship;
                break;
            }
        }
        return foundShip;
    }
}
