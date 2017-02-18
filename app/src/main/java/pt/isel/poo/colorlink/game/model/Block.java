package pt.isel.poo.colorlink.game.model;

/**
 * Created by Moreira on 12/11/2016.
 */
public class Block extends Piece {

    private char type;
    public int color;
    Direction dir;


    public Block() {
        type = 'B';
    }

    @Override
    void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public char getype() {
        return 'B';
    }

    @Override
    public Direction getDir() {
        return dir;
    }

    @Override
    public void setDir(Direction dir) {
        this.dir = dir;
    }


    public char getType() {
        return type;
    }


//    public void setType(char type) {
//        this.type = type;
//    }



}
