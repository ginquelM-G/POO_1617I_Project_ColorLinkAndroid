package pt.isel.poo.colorlink.editor;

import java.util.HashMap;

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
    private int LINE, COL;

    public PieceEditor p;
    public static PieceEditor piecesEd [][];


    public Piece pieces [][];

    public EditorModel(int line, int col){
        this.LINE = line;
        this.COL = col;

        pieces = new Piece[line][col];
        piecesEd = new PieceEditor[line][col];
        if(mapIdxImgType == null) init();
    }

    int getLINE(){return LINE;}
    int getCOL(){ return COL;}

    public void setPiece(int x, int y, int idxOfPiece, boolean newOrNull){
        if(newOrNull) pieces[x][y] = mapIdxImgType.get(idxOfPiece);
    }
/*
    public Piece getCell(char c){
        switch(c){
            case 'E':  return new Empty();
            case 'B':  return new Block();
            case 'S':  return new Side();
            case 'C':  return new Corner();
            case 'I':  return new Invert();
            case 'L':  return new Link();
        }
        return null;
    }
*/
    public static HashMap<Integer, Piece> mapIdxImgType;


    private void init() {
        if (mapIdxImgType == null) {
            mapIdxImgType = new HashMap<>();
            mapIdxImgType.put(0, new Block());
            mapIdxImgType.put(1, new Corner());
            mapIdxImgType.put(2, new Empty());
            mapIdxImgType.put(3, new Invert());
            mapIdxImgType.put(4, new Link());
            mapIdxImgType.put(5, new Side());
        }
    }
}
