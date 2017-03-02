package pt.isel.poo.colorlink.editor;

import pt.isel.poo.colorlink.game.model.Piece;

/**
 * Created by Moreira on 15/02/2017.
 */

public  class PieceEditor {

    private int color;
    private int idxImage;
    private int angle = 0;
    private Piece piece;

    public PieceEditor(){}

    public PieceEditor(Piece piece, int idxImage, int color){
        this.piece = piece;
        this.idxImage = idxImage;
        this.color = color;
    }
    public PieceEditor(Piece piece,int angle, int idxImage, int color){
        this.piece = piece;
        this.idxImage = idxImage;
        this.color = color;
        this.angle = angle;
    }

    public PieceEditor(int idxImage, int color, int angle){
        this.idxImage = idxImage;
        this.color = color;
        this.angle = angle;
    }

    public void setAngle(int angle) {   this.angle = angle;    }

    public int getAngle() { return angle;   }

    public void setColor(int color) {   this.color = color; }

    public int getColor() { return color;   }

    public Piece getPiece(){ return  piece;}




    public void setPiece(Piece piece){this.piece = piece;}

    public void setIdxImage(int idxImage) { this.idxImage = idxImage;   }

    public int getIdxImage() {  return idxImage;    }

}
