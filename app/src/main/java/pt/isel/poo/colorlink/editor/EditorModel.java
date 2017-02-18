package pt.isel.poo.colorlink.editor;

import android.graphics.Bitmap;


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

    public Pieces pieces [][];
    public Pieces p;



    public EditorModel(int line, int col){
        pieces = new Pieces[line][col];
    }

//    public EditorModel(Pieces p){}
//
//    public EditorModel(){}
//
//
//    public void setAngle(int angle){
//        this.angle = angle;
//    }
//
//    public int getAngle(){return angle;}
//
//    public void setPieces(Pieces pieces) {
//        this.p = pieces;
//    }
//
//    public Pieces getPieces() {
//        return p;
//    }
//
//    public void setPieces(Pieces[][] pieces, int i, int j, Pieces p) {
//        pieces[i][j] = p;
//        //this.pieces = pieces;
//    }

}
