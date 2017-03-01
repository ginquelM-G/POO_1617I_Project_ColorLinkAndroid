package pt.isel.poo.colorlink.editor;

import android.graphics.Bitmap;

import pt.isel.poo.colorlink.game.model.Block;
import pt.isel.poo.colorlink.game.model.Corner;
import pt.isel.poo.colorlink.game.model.Empty;
import pt.isel.poo.colorlink.game.model.Invert;
import pt.isel.poo.colorlink.game.model.Link;
import pt.isel.poo.colorlink.game.model.Piece;
import pt.isel.poo.colorlink.game.model.Side;


/**
 *  Created by Moreira on 02/02/2017.
 */

public class EditorModel {
    private Bitmap img;
    public Bitmap emptyImgage;
    private int color ;
    private boolean fix;
    private boolean rotate;
    private int angle = 0;
    public static int LINE, COL;

    public PieceEditor p;
    public static PieceEditor piecesEd [][];
    public Piece piece [][];

    public EditorModel(int line, int col){
        this.LINE = line;
        this.COL = col;

        piece = new Piece[line][col];
        piecesEd = new PieceEditor[line][col];
    }


    public Piece getCell(char c){
        switch(c){
            case 'E':  return new Empty();
            case 'B':  return new Block();
            case 'S':  return new Side();
            case 'C':  return new Corner();
            case 'I':  return new Invert();
            case 'L':  return new Link();
        }
//        if(list_cell.containsKey(c))
//            return list_cell.get(c);

        return null;
    }
}
