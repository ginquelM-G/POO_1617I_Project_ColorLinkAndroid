package pt.isel.poo.colorlink.game.model;

/**
 *  Created by Moreira on 12/11/2016.
 */
public class Corner extends Piece {

    private int color;

    Direction dir;

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
        return 'C';
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
