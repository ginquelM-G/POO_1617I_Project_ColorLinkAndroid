package pt.isel.poo.colorlink.editor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.tile.Tile;

/**
 * Created by Moreira on 08/01/2017.
 */

public class PieceView implements Tile {

    private Bitmap img;
    public Bitmap emptyImgage;
    private int color ;
    private boolean fix;


    /**
     * Images of each type of piece
     */
    private final static int[] imgIds = {
            R.drawable.block, R.drawable.corner, R.drawable.empty,
            R.drawable.invert, R.drawable.link, R.drawable.side
    };

 /**   public PieceView(Context ctx){
        //  super(ctx);
        Resources res=null;
        if(ctx != null)
            res = ctx.getResources();
        img = BitmapFactory.decodeResource(res, R.drawable.corner);
    }
  */

    public PieceView(Context ctx, int idx, int color, boolean fix) {
//      super(ctx);
        this.color = color;
        this.fix = fix;
        Resources res=null;
        if(ctx != null)
        res = ctx.getResources();
        //img = new BitmapFactory().decodeResource(res, imgIds[idx]);
        Log.e("PieceView ", "img" + img);
        img = BitmapFactory.decodeResource(res, imgIds[idx]);
//        emptyImgage = BitmapFactory.decodeResource(res, imgIds[2]);
        Log.e("PieceView ", "imgAter" + img);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        //super.draw(canvas, side);
        Paint p = new Paint();
//      p.setColor(Color.BLUE);
        p.setColor(color);
        p.setStrokeWidth(100);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        if(fix){
            p.setColor(Color.BLACK);
            canvas.drawLine(side/3, side/3, side - side/3, side - side/3, p);
            canvas.drawLine(0, side/2, side/2, side/2, p);
        }else {
            canvas.drawRect(30, 30, side - 30, side - 30, p);
//          canvas.drawLine(0, side/2, side/2, side/2, p);
//            if(emptyImgage != null)
//                canvas.drawBitmap(emptyImgage, null, new Rect(0, 0, side, side), p);
//            else
                canvas.drawBitmap(img, null, new Rect(0, 0, side, side), p);
        }
    }

    @Override
    public boolean setSelect(boolean selected) {
        return true;
    }



}
