enum Fieldtype {
    FOG ('~', "the fog of war"),
    SHIP ('O', "a cell with your ship"),
    HIT ('X', "the ship was hit"),
    MISS ('M', "a miss");

    private final char mask;
    private final String desc;

    Fieldtype(char mask, String desc) {
        this.mask = mask;
        this.desc = desc;
    }

    public char getMask() {
        return mask;
    }
}

public class Field {
    private Fieldtype fieldType;
    private int row;
    private int col;

    public Field(Fieldtype fieldType, int row, int col) {
        this.fieldType = fieldType;
        this.row = row;
        this.col = col;
    }

    public void setFieldType(Fieldtype fieldType) {
        this.fieldType = fieldType;
    }

    public Fieldtype getFieldType() {
        return this.fieldType;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col){
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public static int getDistance(Field from, Field to) {
        return Math.abs(from.getCol() - to.getCol()) + Math.abs(from.getRow() - to.getRow());
    }

    public static int getSectionLength(Field from, Field to){
        return Math.abs(from.getCol() - to.getCol()) + Math.abs(from.getRow() - to.getRow()) + 1;
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldType=" + fieldType.getMask() +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}

