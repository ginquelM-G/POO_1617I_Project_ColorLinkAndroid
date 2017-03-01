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

import pt.isel.poo.colorlink.Img;
import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.game.model.Piece;
import pt.isel.poo.tile.Tile;

import static pt.isel.poo.colorlink.game.controller.Game.modelGame;

/**
 *  Created by Moreira on 17/02/2017.
 */

//public class EmptyPieceView extends PieceView {
public class PieceView  implements Tile, Img.Updater {

    private static Paint paintDot, paint;
    private static Img img;
    private Bitmap bitmap;
    private Bitmap imgAux;
    private int color;
    private boolean rotate;
    private int lin, col;
    boolean initDraw;
    public static int angleImg;
    public static Context ctx;
    public static int idx;
    public static char type;

    /**
     * Images of each type of piece
     */
    private final int[] imgIds = {
            R.drawable.block, R.drawable.corner, R.drawable.empty,
            R.drawable.invert, R.drawable.link, R.drawable.side
    };

    private final static int[] colors ={Color.RED, Color.GREEN, Color.BLUE, Color.GRAY};
    public HashMap<Character, Integer> map;


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


    public PieceView(Context ctx, int lin, int col) {
//        super(ctx);
        init();
        initDraw = true;

        this.lin = lin;
        this.col = col;
//        this.rotate = rotate;
        this.color = modelGame.pieces[lin][col].getColor();
        char type = modelGame.pieces[lin][col].getype();

        paint.setColor(colors[color]);

        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        bitmap =  BitmapFactory.decodeResource(res, map.get(type));
        bitmap = getResizedBitmap(bitmap, ((int) (bitmap.getHeight()*0.25)));
        img = new Img(ctx, map.get(type), this);

        Log.e("PieceView-->Const)", "Constrctor of Init -> End");
    }


    public PieceView(Context ctx, int lin, int col, boolean rotate){
//        super(ctx);
        Log.e("PieceView-->Const()", "Begin");
        init();

        this.lin = lin;
        this.col = col;
        this.rotate = rotate;
        this.color = modelGame.pieces[lin][col].getColor();
        this.ctx = ctx;


        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        char type = modelGame.pieces[lin][col].getype();
        this.type = type;


        bitmap =  BitmapFactory.decodeResource(res, map.get(type));
        bitmap = getResizedBitmap(bitmap, ((int) (bitmap.getHeight()*0.25)));
//        img = new Img(ctx, map.get(type) , this);
        Log.e("PieceView--> Const()", "End");
    }


    @Override
    public void draw(Canvas canvas, int side) {
        Log.e("PieceView --> draw()", "Begin");
        Log.v("PieceView --> draw()", " lin "+lin + " col "+ col);
        //super.draw(canvas, side);
        Piece p = modelGame.pieces[lin][col];
        Log.e("draw() DIR ", " "+ p.getDir()+ " ... begin");

        paint.setColor(colors[color]);
        canvas.drawRect(0, 0, side, side, paint);
        if( p.getype() == 'E'){
            Log.i("PieceView --> draw()", "     p.getype() == 'E'");
            if(img == null) img = new Img(ctx, map.get(type), this);
//            img.draw(canvas, side, side, paint);
//            return;
        }
        if (p.getype() != 'B' && !p.isBlocked()) {
            Log.i("PieceView --> draw()", " p.getype() != 'B' && !p.isBlocked()");
            canvas.drawCircle(side / 2, side / 2, 5, paintDot);
        }
        if (rotate && p.getype() != 'B' && !p.isBlocked()) {
            Log.i("PieceView --> draw()", " rotate");
            canvas.drawBitmap(rotateImg(bitmap, modelGame.pieces[lin][col], true), null, new Rect(0, 0, side, side), paint);
        } else {
            Log.i("PieceView --> draw()", " else");
            canvas.drawBitmap(rotateImg(bitmap, modelGame.pieces[lin][col], false), null, new Rect(0, 0, side, side), paint);
            if (p.isBlocked()) {
                paint.setColor(Color.BLACK);
                canvas.drawLine(side / 3, side / 2, side - side / 3, side / 2, paint);
                canvas.drawLine(side / 2, side / 3, side / 2, side - side / 3, paint);
            }
//            }
//        img.draw(canvas, side,side, 0, paint);
        }
        Log.e("draw() DIR ", " "+ p.getDir()+ "....end");
        Log.e("PieceView  --> draw()", "End\n\n");
        rotate = false;
    }


    public void drawInit(Canvas canvas, int side, Bitmap img){
        Log.e("EmptyPieceVie", " drawInit() Begin side"+ side);
//        bitmap = getResizedBitmap(bitmap, side);
        paint.setColor(colors[color]);
        canvas.drawRect(0, 0, side, side, paint);

        Piece p = modelGame.pieces[lin][col];
        Log.e("DIR ", " "+ p.getDir());
        if(p.getype() != 'B' && !p.isBlocked()){ canvas.drawCircle(side/2, side/2, 5, paintDot);}

        if(rotate && p.getype() != 'B' && !p.isBlocked() ) {
            canvas.drawBitmap(rotateImg(img, modelGame.pieces[lin][col], true), null, new Rect(0, 0, side, side), paint);
        }
        else{
            canvas.drawBitmap(rotateImg(img, modelGame.pieces[lin][col], false), null, new Rect(0, 0, side, side), paint);
            if(p.isBlocked()){
                paint.setColor(Color.BLACK);
                canvas.drawLine(side/3, side/2, side - side/3, side/2, paint);
                canvas.drawLine(side/2, side/3, side/2, side - side/3, paint);
            }
        }
        Log.e("DIR ", " "+ p.getDir());
        Log.e("EmptyPieceView drawInit", "End");
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    @Override
    public boolean setSelect(boolean selected) {
        return true;
    }




    private int rotateImg(Piece p, boolean rotate){
        int angleAux=0;
        System.out.println("\n"+p.getype() + " --> "+p.getDir()+"\n");
        if(p != null )
            if(p.getDir() == null) p.setDir(Piece.Direction.UP);
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

        return angleAux;
    }




    private Bitmap rotateImg(Bitmap img, Piece p, boolean rotate){
        int angleAux =0;
        if(img == null)Log.e("error ","null image");
        System.out.println("\nInitial rotateImg(): "+p.getype() + " --> "+p.getDir()+"\n ");
        if(p != null && p.getDir() !=  null) {
            switch (p.getDir()) {
                case DOWN:
                    angleAux = 180;
                    break;
                case LEFT:
                    angleAux = 270;
                    break;
                case UP:
                    angleAux = 360;
                    break; //360 or 0
                case RIGHT:
                    angleAux = 90;
                    break;
            }
            if (rotate) {
                angleAux = calculateTheRotationAngle(angleAux);
                switch (p.getDir()) {
                    case DOWN:
                        p.setDir(Piece.Direction.LEFT);
                        break;
                    case LEFT:
                        p.setDir(Piece.Direction.UP);
                        break;
                    case UP:
                        p.setDir(Piece.Direction.RIGHT);
                        break;
                    case RIGHT:
                        p.setDir(Piece.Direction.DOWN);
                        break;
                }
            }
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angleAux);


        Bitmap rotateBitmap = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, true);
        if(PieceView.img != null){
            System.out.println("\nimg != null \n\n");
            angleImg = angleAux;
        }
//       rotateBitmap = bitmap;
        return rotateBitmap;
    }


    public int calculateTheRotationAngle(int angleRotation){
        if( angleRotation == 360 ) angleRotation = 90;
        else angleRotation += 90;
//            this.angleRotation = angleRotation;
        return angleRotation;
    }

    @Override
    public void updateImage(Img img) {

    }
}
