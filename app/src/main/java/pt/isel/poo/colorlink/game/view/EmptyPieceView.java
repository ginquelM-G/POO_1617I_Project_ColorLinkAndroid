package pt.isel.poo.colorlink.game.view;

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

import java.util.HashMap;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.game.model.Grid;
import pt.isel.poo.colorlink.game.model.Piece;
import pt.isel.poo.tile.Tile;

import static pt.isel.poo.colorlink.game.controller.Game.modelGame;

/**
 * Created by Moreira on 17/02/2017.
 */

//public class EmptyPieceView extends PieceView {
public class EmptyPieceView implements Tile {

    Bitmap img;
    int color;
    boolean rotate;
    Grid model;
    int lin, col;

    /**
     * Images of each type of piece
     */
    private final static int[] imgIds = {
            R.drawable.block, R.drawable.corner, R.drawable.empty,
            R.drawable.invert, R.drawable.link, R.drawable.side
    };

    private final static int[] colors ={Color.RED, Color.GREEN, Color.BLUE};
    public static HashMap<Character, Integer> map;

    public void init(){
        if(map == null) {
            map = new HashMap<>();
            map.put('B', R.drawable.block);
            map.put('C', R.drawable.corner);
            map.put('E', R.drawable.empty);
            map.put('I', R.drawable.invert);
            map.put('L', R.drawable.link);
            map.put('S', R.drawable.side);
        }
    }

    public EmptyPieceView(Context ctx, Grid model, char type, int color, boolean rotate){
        this(ctx, type, color);
        this.rotate =rotate;

    }

    public EmptyPieceView(Context ctx, char type, int color){
        init();
        this.color = color;
        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        img =  BitmapFactory.decodeResource(res, map.get(type));
//        img =  BitmapFactory.decodeResource(res, imgIds[map.get(type)]);
//        img = (pieces.getImage() == null)? BitmapFactory.decodeResource(res, imgIds[p.getIdxImage()]): p.getImage();
    }

    @Override
    public void draw(Canvas canvas, int side) {
        Log.e("EmptyPieceView  draw()", "Begin");
        //super.draw(canvas, side);
        Paint p = new Paint();
        Paint paintDot = new Paint();
        paintDot.setColor(Color.WHITE);
//        p.setColor(pieces.getColor());
        p.setColor(colors[color]);
//        p.setColor(Color.RED);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawRect(0, 0, side, side, p);
        if(rotate) canvas.drawBitmap(rotateImg(img, modelGame.pieces[lin][col],true ), null, new Rect(0, 0, side, side), p);
        else canvas.drawBitmap(rotateImg(img, modelGame.pieces[lin][col], false), null, new Rect(0, 0, side, side), p);

//                canvas.drawBitmap(pieces.getImage(), null, new Rect(0, 0, side, side), p);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
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


    private Bitmap rotateImg(Bitmap img, Piece p, boolean rotate){

        int angleAux =0;
        System.out.println("\n"+p.getype() + " --> "+p.getDir()+"\n");
        switch (p.getDir()) {
            case DOWN:
                angleAux = 180;   p.setDir(Piece.Direction.DOWN);     break;
            case LEFT:
                angleAux = 90;  p.setDir(Piece.Direction.LEFT);     break;
            case UP:
                angleAux = 360; p.setDir(Piece.Direction.UP);       break;
            case RIGHT:
                angleAux = 270; p.setDir(Piece.Direction.RIGHT);    break;
        }

        if(rotate) {
            angleAux = calculateTheRotationAngle(angleAux);
            switch (p.getDir()) {
                case DOWN:  p.setDir(Piece.Direction.LEFT);     break;
                case LEFT:  p.setDir(Piece.Direction.UP);       break;
                case UP:    p.setDir(Piece.Direction.RIGHT);    break;
                case RIGHT: p.setDir(Piece.Direction.DOWN);     break;
            }
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angleAux);

        Bitmap rotateBitmap = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, true);

        return rotateBitmap;
//        return img;
    }


    public int calculateTheRotationAngle(int angleRotation){
        if( angleRotation == 360 ) angleRotation = 90;
        else angleRotation += 90;
//            this.angleRotation = angleRotation;

        return angleRotation;
    }




    public class Pieces {

        private int color;
        private int idxImage;
        private int angle = 0;
        private Bitmap image;

        public Pieces() {
        }

        public Pieces(int idxImage, int color) {
            this.idxImage = idxImage;
            this.color = color;
        }

        public Pieces(int idxImage, int color, int angle) {
            this.idxImage = idxImage;
            this.color = color;
            this.angle = angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        public int getAngle() {
            return angle;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setIdxImage(int idxImage) {
            this.idxImage = idxImage;
        }

        public int getIdxImage() {
            return idxImage;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public Bitmap getImage() {
            return image;
        }

    }
}
