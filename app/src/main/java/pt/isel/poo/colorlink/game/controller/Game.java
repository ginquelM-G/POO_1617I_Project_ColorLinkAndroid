package pt.isel.poo.colorlink.game.controller;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.editor.PiecePicker;
import pt.isel.poo.colorlink.game.model.Grid;
import pt.isel.poo.colorlink.game.view.PieceView;
import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.TilePanel;

/**
 * Created by Moreira on 17/02/2017.
 */

public class Game extends AppCompatActivity implements OnTileTouchListener {


    private PiecePicker piecePicker;        // Piece type selector.
    private RadioGroup colorSel, actionSel; // Selectors of color and action.
    private TilePanel grid;                 // Edit area.

    public TextView textViewTime;
    private static final int _1Minute = 60;
    static long  startTime;
    private long endTime, duration;
    private int minutes, seconds;


    public static Grid modelGame;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        grid = (TilePanel) findViewById(R.id.panelGame);
        grid.setListener(this);
        textViewTime =(TextView) findViewById(R.id.time);

        modelGame = new Grid();
        init();
        grid.setSize(modelGame.COL, modelGame.LINE);
        initGrid();

        startTime = System.nanoTime();
        timeUpdate();
        Log.e("Game OnCreate() Line "+modelGame.LINE, " Col "+ modelGame.COL );
    }


    /** Chama o metodo para ler o ficheriro de Level1 */
    private void init(){
        AssetManager assetManager = getAssets();
        try {
            loadLevel("Level1");
        }catch (IOException err){
            Log.e("Game: init() --> ERROR", err.getMessage());
        }
        Log.v("init() " , "  end");
    }


    private void initGrid(){
        Log.v("Game --> initGrid " , " Begin");
        for(int i=0; i < modelGame.LINE ; i++) {
            for (int j = 0; j < modelGame.COL ; j++) {
                grid.setTile(j, i, new PieceView(this, i, j));
//                grid.setTile(j, i, new PieceView(this, i, j, false));
//                  grid.setTile(j, i, new EmptyPieceView(this, i, j, false));
            }
        }
    }

    private boolean loadLevel(String filename) throws IOException {
        try{
//            Scanner level = new Scanner(new FileInputStream(filename+".txt"));
            InputStream file = getAssets().open(filename+".txt");

            Scanner level = new Scanner(file);
            modelGame.load(level);
            level.close();

            System.out.println("Read file is Completed!!!");
            return true;
        }
        catch (FileNotFoundException | InputMismatchException e){
            System.out.println("Error loading file "+e.getMessage());
            return false;
        }
    }


    /** Exibe o valor do tempo jogado actualizado */
    public void repaintTime() {
        timePlayed();
        if(seconds < 10) textViewTime.setText("\t"+minutes+":0"+ seconds);
        else textViewTime.setText("\t"+minutes+":"+ seconds);
    }

    /** Calcula o tempo que o utilizador ja gastou no jogo desde do inicio do jogo */
    private void timePlayed(){
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        minutes = 0;
        seconds= (int) (duration/10e8); // converter nanosgundos para segundos

        if(seconds >= _1Minute ){
            minutes = seconds / _1Minute;
            seconds = seconds % _1Minute;
        }
    }

    /** Utiliza a class CountDownTimer para poder actualizar o valor do time */
    private void timeUpdate(){
        new CountDownTimer(Integer.MAX_VALUE, 1000) {

            public void onTick(long millisUntilFinished) {
                repaintTime();
            }
            public void onFinish() {
//                _tv.setText("done!");
            }
        }.start();
    }


    @Override
    public boolean onClick(int xTile, int yTile) throws IllegalAccessException, InstantiationException {
//        Log.e("x "+xTile, " y"+ yTile );
//        Log.e("Type ", "    "+modelGame.pieces[xTile][yTile].getype());
        grid.setTile(xTile, yTile, new PieceView(this, yTile, xTile, true));
        return false;
    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
//        PieceView pv = new PieceView(this, yFrom, xFrom, false);
//        PieceView pv1 = (PieceView) grid.getTile(xFrom, yFrom);
//        grid.setTile(xFrom, yFrom, pv1 );
        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {

    }


    @Override
    public void onDragCancel() {

    }

}
