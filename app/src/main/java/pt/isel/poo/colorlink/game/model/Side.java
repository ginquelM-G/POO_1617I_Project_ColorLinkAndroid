package pt.isel.poo.colorlink.game.model;

/**
 * Created by Moreira on 12/11/2016.
 */
public class Side extends Piece {
    public int color;
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
        return 'S';
    }


    @Override
    public Direction getDir() {
        return dir;
    }

    @Override
    public void setDir(Direction dir) {
        this.dir = dir;
    }
}
