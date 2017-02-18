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
import pt.isel.poo.colorlink.game.model.Piece;
import pt.isel.poo.tile.Tile;

import static pt.isel.poo.colorlink.game.controller.Game.modelGame;

/**
 *  Created by Moreira on 17/02/2017.
 */

//public class EmptyPieceView extends PieceView {
public class EmptyPieceView implements Tile {

    private static Paint paintDot, paint;
    private Bitmap img;
    private int color;
    private boolean rotate;
    private int lin, col;

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

            paintDot = new Paint();
            paintDot.setColor(Color.WHITE);

            paint = new Paint();
            paint.setColor(colors[color]);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);

        }
    }



    public EmptyPieceView(){}
    public EmptyPieceView(Context ctx,  int lin, int col, boolean rotate){
//        this(ctx, modelGame.pieces[lin][col].getype(), modelGame.pieces[lin][col].getColor());
        init();
        this.lin = lin;
        this.col = col;
        this.rotate = rotate;
        this.color = modelGame.pieces[lin][col].getColor();
//        paint.setColor(colors[color]);

        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        char type = modelGame.pieces[lin][col].getype();
        img =  BitmapFactory.decodeResource(res, map.get(type));
//
//        Log.e("Const EmptyPieceView()", " lin "+lin + " col "+ col);
//        Log.e("EmptyPieceView()", " img " +img.toString());
    }


    @Override
    public void draw(Canvas canvas, int side) {
        Log.e("EmptyPieceView  draw()", "Begin");
        Log.v("EmptyPieceView-> draw()", " lin "+lin + " col "+ col);
        //super.draw(canvas, side);

        paint.setColor(colors[color]);
        canvas.drawRect(0, 0, side, side, paint);
        Piece p = modelGame.pieces[lin][col];
        if(rotate && p.getype() != 'B' && !p.isBlocked() )
            canvas.drawBitmap(rotateImg(img, modelGame.pieces[lin][col],true ), null, new Rect(0, 0, side, side), paint);
        else{
            canvas.drawBitmap(rotateImg(img, modelGame.pieces[lin][col], false), null, new Rect(0, 0, side, side), paint);
            if(p.getype() != 'B' && !p.isBlocked()){ canvas.drawCircle(side/2, side/2, 5, paintDot);}
            if(p.isBlocked()){
                paint.setColor(Color.BLACK);
                canvas.drawLine(side/3, side/2, side - side/3, side/2, paint);
                canvas.drawLine(side/2, side/3, side/2, side - side/3, paint);
            }
        }
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }




    private Bitmap rotateImg(Bitmap img, Piece p, boolean rotate){

        int angleAux =0;
        System.out.println("\n"+p.getype() + " --> "+p.getDir()+"\n");
        if(p != null && p.getDir() !=  null)
            switch ( p.getDir()) {
                case DOWN:
                    angleAux = 180;   p.setDir(Piece.Direction.DOWN);     break;
                case LEFT:
                    angleAux = 270;  p.setDir(Piece.Direction.LEFT);     break;
                case UP:
                    angleAux = 360; p.setDir(Piece.Direction.UP);       break;
                case RIGHT:
                    angleAux = 90; p.setDir(Piece.Direction.RIGHT);    break;
                default: angleAux = 0;
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
