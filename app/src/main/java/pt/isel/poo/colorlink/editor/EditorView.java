package pt.isel.poo.colorlink.editor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.HashMap;

import pt.isel.poo.colorlink.Img;
import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.game.model.Piece;
import pt.isel.poo.tile.Tile;

import static pt.isel.poo.colorlink.editor.EditorModel.piecesEd;

/**
 *  Created by Moreira on 08/01/2017.
 */

public class EditorView implements Tile, Img.Updater {

    private Bitmap bitmap;
    private Img img;
    public int color ;
    public int idx;
    public int lin;
    public int col;
    private boolean insert, rotate, move, fix;
    boolean reWrite =false;
    public int angleRotation = 0;
    public boolean isSelected;
    PieceEditor pieceEditor;
    Piece piece;
    Paint paintDot;
    Paint p;



    /**
     * Images of each type of piece
     */
    private final static int[] imgIds = {
            R.drawable.block, R.drawable.corner, R.drawable.empty,
            R.drawable.invert, R.drawable.link, R.drawable.side
    };

    public static HashMap<Character, Integer> mapTypeImg;
    public static HashMap<Integer, Character> mapImgType;


    public void init(){
        if(p== null) {
            p = new Paint();
            paintDot = new Paint();
            paintDot.setColor(Color.WHITE);
            p.setColor(color);
            p.setStrokeWidth(5);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        if(mapImgType == null) {
            mapImgType = new HashMap<>();
            mapImgType.put(R.drawable.block,'B');
            mapImgType.put(R.drawable.corner,'C');
            mapImgType.put(R.drawable.empty,'E');
            mapImgType.put(R.drawable.invert,'I');
            mapImgType.put(R.drawable.link,'L');
            mapImgType.put(R.drawable.side,'S');
//        if(mapTypeImg == null) {
//            mapTypeImg = new HashMap<>();
//            mapTypeImg.put('B', R.drawable.block);
//            mapTypeImg.put('C', R.drawable.corner);
//            mapTypeImg.put('E', R.drawable.empty);
//            mapTypeImg.put('I', R.drawable.invert);
//            mapTypeImg.put('L', R.drawable.link);
//            mapTypeImg.put('S', R.drawable.side);

//            paintDot = new Paint();
//            paintDot.setColor(Color.WHITE);
//
//            paint = new Paint();
//            paint.setColor(colors[color]);
//            paint.setStrokeWidth(5);
//            paint.setStyle(Paint.Style.FILL_AND_STROKE);

        }
    }


    public EditorView(Context ctx, int idxImg, int color, boolean rotate, boolean fix) {
        this.color = color;
        this.fix = fix;
        this.rotate = rotate;

        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
//        bitmap = (pieceEditor.getImage() == null)? BitmapFactory.decodeResource(res, imgIds[p.]): p.getImage();
        Log.v("imgIds[idxImg] ", " id "+ idxImg + "->" +imgIds[idxImg]);
        bitmap =  BitmapFactory.decodeResource(res, imgIds[idxImg]);
        img = new Img(ctx, imgIds[idxImg] , this);
    }



    public EditorView(Context ctx, int idx, boolean insert, boolean rotate, boolean move, boolean fix) {
//      super(ctx);
        init();
        this.idx = idx;
        this.color = Color.GRAY;
        this.insert = insert;
        this.rotate =rotate;
        this.move = move;
        this.fix = fix;
        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();

        bitmap = BitmapFactory.decodeResource(res, imgIds[idx]);
        img = new Img(ctx, imgIds[idx] , this);
        reWrite = true;
        Log.e("EditorView Const()", "End");
    }

    public EditorView(Context ctx, PieceEditor pieceEditor, int lin, int col, boolean insert, boolean rotate, boolean move, boolean fix) {
//      super(ctx);
        init();
        this.pieceEditor = pieceEditor;
        this.lin = lin;
        this.col = col;
        this.idx = pieceEditor.getIdxImage();
        this.color = pieceEditor.getColor();
        this.insert = insert;
        this.rotate = rotate;
        this.move = move;
        this.fix = fix;
        Resources res = null;
        if (ctx != null)
            res = ctx.getResources();

        bitmap = BitmapFactory.decodeResource(res, imgIds[idx]);
        img = new Img(ctx, imgIds[idx], this);
        reWrite = true;
        Log.v("EditorView Constr()", "DIR-> "+ pieceEditor.getPiece().getDir());
        Log.e("EditorView Const()", "End");
    }




    @Override
    public void draw(Canvas canvas, int side) {
        Log.e("EditorViewNew  draw()", "->->-> Inicio");
        Log.v("EditorView -> draw() " , " insert = "+ insert + " rotate="+rotate+ " move= "+move+" fix= "+fix);

        p.setColor(color);
        if(pieceEditor != null && pieceEditor.getIdxImage() != 0) {
            canvas.drawCircle(side / 2, side / 2, 5, paintDot);
        }
        if (insert || move ) {drawInsertSelected(canvas,side,  p);}
        else if(rotate) drawWithRotateSelected(canvas, side, p);
        else if(fix) drawWithFixSelected(canvas, side, p);
        rotate = false;
        pieceEditor = null;
    }
//

    @Override
    public boolean setSelect(boolean selected) {
        this.isSelected = selected;
        return isSelected;
//        return true;
    }


    public void drawInsertSelected(Canvas canvas, int side, Paint p){
        Log.v("EditorView -> draw() " , " insert = "+ insert + " lin "+lin+ " col "+col);
        if(reWrite) {
            canvas.drawRect(0, 0, side, side, p);
            img.draw(canvas, side, side, p);
            reWrite = false;
        }
//      canvas.drawBitmap(bitmap, null, new Rect(0, 0, side, side), p);
    }


    public void drawWithRotateSelected(Canvas canvas, int side, Paint p){
        Log.e(" EditorView -->", "drawWithRotateSelected()");
        canvas.drawRect(0, 0, side, side, p);

        if(piecesEd[lin][col] != null){
            Log.v(" EditorView -->", " piecesEd[lin][col] " + piecesEd[lin][col].toString());
            if(piecesEd[lin][col].getPiece() != null) {
                Log.v(" EditorView -->", " piecesEd[lin][col].getPiece() " + piecesEd[lin][col].getPiece());
//              updateAngle(piecesEd[lin][col].getPiece(), true);
                updateAngle(piecesEd[lin][col].getPiece(), true);
                if(img != null)
                    img.draw(canvas, side, side, piecesEd[lin][col].getAngle(), p);
            }
        }
        rotate = false;
        insert = false;
    }

    public void drawMoveSelected(){}


    public void drawWithFixSelected(Canvas canvas, int side, Paint p){
        Log.v("EditorView->drawWithFix" , " fix "+ fix);
        canvas.drawRect(0, 0, side, side, p);
        int colorSave = p.getColor();
        p.setColor(Color.BLACK);
        img.draw(canvas, side,side,p); // Redesenha a imagem que ja estava
        canvas.drawLine(side/3, side/2, side - side/3, side/2, p);
        canvas.drawLine(side/2, side/3, side/2, side - side/3, p);
        p.setColor(colorSave);
    }


    private int currentAngleOfPiece(Piece p){
        int angleAux =0;
        if (p != null) {
            if (p.getDir() == null) {
                p.setDir(Piece.Direction.UP);
            }
            switch (p.getDir()) {
                case DOWN:
                    angleAux = 180;
                    break;
                case LEFT:
                    angleAux = 270;
                    break;
                case UP:
                    angleAux = 0;
                    break; //360 or 0
                case RIGHT:
                    angleAux = 90;
                    break;
            }
        }
        return angleAux;
    }

    private int updateAngle(Piece p, boolean rotate) {
        int angleAux = currentAngleOfPiece(p);
        System.out.println("\nupdateAngle(): " + p.getype() + " --> " + p.getDir() + "\n ");

        if (rotate) {
            angleAux = calculateTheRotationAngle(angleAux);
            switch (p.getDir()) {
                case DOWN:  p.setDir(Piece.Direction.LEFT);   break;
                case LEFT:  p.setDir(Piece.Direction.UP);     break;
                case UP:    p.setDir(Piece.Direction.RIGHT);    break;
                case RIGHT: p.setDir(Piece.Direction.DOWN);  break;
            }
        }

        piecesEd[lin][col].setPiece(p);
        piecesEd[lin][col].setAngle(angleAux);
        return angleAux;
    }

    public int calculateTheRotationAngle(int angleRotation){
        if( angleRotation == 360 ) angleRotation = 90;
        else angleRotation += 90;
//            this.angleRotation = angleRotation;

        System.out.println("calculateTheRotationAngle return angle = "+angleRotation);
        return angleRotation;
    }

    @Override
    public void updateImage(Img img) {}

}
