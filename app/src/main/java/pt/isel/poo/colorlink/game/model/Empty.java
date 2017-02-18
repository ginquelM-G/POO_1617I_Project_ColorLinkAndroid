package pt.isel.poo.colorlink.game.model;

/**
 * Created by Moreira on 12/11/2016.
 */
public class Empty extends Piece {

    String type;
    final int color = 3;
    Direction dir;

    public Empty(){}

    public Empty(String type, int color, Direction dir){
        super(type, color, dir);

    }

    @Override
    void setColor(int color) {}

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public char getype() {
        return 'E';
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
