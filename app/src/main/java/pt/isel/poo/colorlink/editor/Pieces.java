package pt.isel.poo.colorlink.editor;

import android.graphics.Bitmap;

/**
 * Created by Moreira on 15/02/2017.
 */

public class Pieces {

    private int color;
    private int idxImage;
    private int angle = 0;
    private Bitmap image;

    public Pieces(){}

    public Pieces(int idxImage, int color){
        this.idxImage = idxImage;
        this.color = color;
    }

    public Pieces(int idxImage, int color, int angle){
        this.idxImage = idxImage;
        this.color = color;
        this.angle = angle;
    }

    public void setAngle(int angle) {   this.angle = angle;    }

    public int getAngle() { return angle;   }

    public void setColor(int color) {   this.color = color; }

    public int getColor() { return color;   }

    public void setIdxImage(int idxImage) { this.idxImage = idxImage;   }

    public int getIdxImage() {  return idxImage;    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
