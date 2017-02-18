package pt.isel.poo.colorlink.editor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.tile.Tile;

/**
 *  Created by Moreira on 08/01/2017.
 */

public class EditorView implements Tile {

    private Bitmap img;
    public Bitmap emptyImgage;
    public Bitmap imageAfterRotate;
    public int color ;
    public int idx;
    private boolean insert;
    private boolean fix;
    private boolean rotate;
    private boolean move;
    public int angleRotation = 0;
    public boolean isSelected;
    Pieces pieces;


    /**
     * Images of each type of piece
     */
    private final static int[] imgIds = {
            R.drawable.block, R.drawable.corner, R.drawable.empty,
            R.drawable.invert, R.drawable.link, R.drawable.side
    };

    /**   public EditorView(Context ctx){
     //  super(ctx);
     Resources res=null;
     if(ctx != null)
     res = ctx.getResources();
     img = BitmapFactory.decodeResource(res, R.drawable.corner);
     }
     */

    public EditorView(Context ctx, Pieces p, boolean fix, boolean rotate) {
        this.pieces = p;
//     this.idx = p.getIdxImage();
        this.color = p.getColor();
        this.fix = fix;
        this.rotate = rotate;

        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        img = (pieces.getImage() == null)? BitmapFactory.decodeResource(res, imgIds[p.getIdxImage()]): p.getImage();
    }

    public EditorView(Context ctx, int idx, int color, boolean insert, boolean rotate, boolean move, boolean fix) {
//      super(ctx);
        this.idx = idx;
        this.color = color;

        this.insert = insert;
        this.rotate =rotate;
        this.move = move;
        this.fix = fix;

//        this.angleRotation = angleRotation;

//        setAngle(angleRotation, rotate);

        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        //img = new BitmapFactory().decodeResource(res, imgIds[idx]);
        Log.e("EditorView ", "img" + img);
        img = BitmapFactory.decodeResource(res, imgIds[idx]);
//        emptyImgage = BitmapFactory.decodeResource(res, imgIds[2]);
        Log.e("EditorView ", "imgAter" + img);
    }




    @Override
    public void draw(Canvas canvas, int side) {
        Log.e("EditorViewNew  draw()", "->->-> Inicio");
        //super.draw(canvas, side);
        Paint p = new Paint();
        Paint paintDot = new Paint();
        paintDot.setColor(Color.WHITE);
//        p.setColor(pieces.getColor());
        p.setColor(color);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawRect(0, 0, side, side, p);

        if(fix) drawWithFixSelected(canvas, side, p);

        else{
            Log.e("EditorView -> draw() " , " !fix" );

            if(pieces != null && pieces.getIdxImage() != 0)  canvas.drawCircle(side/2, side/2, 5, paintDot);
            if(pieces != null && pieces.getImage() == null){ pieces.setImage(img);}

            if(rotate) {    drawWithRotateSelected(canvas, side, p); }
//            else if(move){}
            if(insert || move) {
//                canvas.drawBitmap(pieces.getImage(), null, new Rect(0, 0, side, side), p);
                img = (pieces != null)? pieces.getImage() :  img;
                canvas.drawBitmap(img, null, new Rect(0, 0, side, side), p);
            }
            imageAfterRotate = (pieces != null)? pieces.getImage() :  img;
        }
    }

    @Override
    public boolean setSelect(boolean selected) {
        this.isSelected = selected;

        return isSelected;
//        return true;
    }

    private Bitmap rotateImg(Bitmap img, Pieces p){
        if(rotate) {
            Log.e("Mth rotateImg: ", "Before " + p.getAngle());
            int angle = calculateTheRotationAngle(p.getAngle());
            p.setAngle(angle);
            Matrix matrix = new Matrix();
            Log.e("Mth rotateImg:  ", "Ater " + p.getAngle());
            matrix.postRotate(p.getAngle());

            Bitmap rotateBitmap = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                    img.getHeight(), matrix, true);

            return rotateBitmap;
        }

        return img;
    }


    private Bitmap rotateImg(Bitmap img, Pieces p, boolean rotate){
        if(rotate) {
            int angle = calculateTheRotationAngle(p.getAngle());
            p.setAngle(angle);

            Matrix matrix = new Matrix();
            matrix.postRotate(p.getAngle());

            Bitmap rotateBitmap = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                    img.getHeight(), matrix, true);

            return rotateBitmap;
        }

        return img;
    }



    public int calculateTheRotationAngle(int angleRotation){
        if( angleRotation == 360 ) angleRotation = 90;
        else angleRotation += 90;
//            this.angleRotation = angleRotation;

        return angleRotation;
    }


    public void drawInsertSelected(){}
    public void drawWithRotateSelected(Canvas canvas, int side, Paint p){
        pieces.setImage(rotateImg(img, pieces));
        canvas.drawBitmap(pieces.getImage(), null, new Rect(0, 0, side, side), p);
}
    public void drawMoveSelected(){}

    public void drawWithFixSelected(Canvas canvas, int side, Paint p){
        Log.e("EditorView -> draw " , " fix "+ fix);

        int colorSave = p.getColor();
        p.setColor(Color.BLACK);
        canvas.drawLine(side/3, side/2, side - side/3, side/2, p);
        canvas.drawLine(side/2, side/3, side/2, side - side/3, p);

        p.setColor(colorSave);

        canvas.drawBitmap(rotateImg(img, pieces), null, new Rect(0, 0, side, side), p);
//        canvas.drawBitmap(img, null, new Rect(0, 0, side, side), p);
    }
}
