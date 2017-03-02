package pt.isel.poo.colorlink.game.controller;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.game.model.Grid;
import pt.isel.poo.colorlink.game.view.PieceView;
import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.TilePanel;

/**
 *  Created by Moreira on 17/02/2017.
 */

public class Game extends AppCompatActivity implements OnTileTouchListener {
    private TilePanel grid;                 // Edit area.

    private TextView textViewTime;
    private static final int _1Minute = 60;
    private long startTime;
    private long endTime, duration;
    private int minutes, seconds;
    private boolean startGame;


    public static Grid modelGame;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_game);

        // obter referencia para a vista
        grid = (TilePanel) findViewById(R.id.panelGame);
        grid.setListener(this);
        // obter a referecia para textview que representa o tempo jogado
        textViewTime = (TextView) findViewById(R.id.time);

        // Criacao do modelo do jogo
        modelGame = new Grid();

        init();
        startGame = true;
    }


    /** Inicia tudo que eh preciso para inciar o jogo */
    private void init(){
        initReadFile();
        grid.setSize(modelGame.COL, modelGame.LINE);
        initGrid();
    }

    /** Chama o metodo para ler o ficheriro de Level1 */
    private void initReadFile(){
        AssetManager assetManager = getAssets();
        try {
            loadLevel("Level1");
        }catch (IOException err){
            Log.e("Game: init() --> ERROR", err.getMessage());
        }
        Log.v("init() " , "  end");
    }


    /** Inicia a parte visual do jogo */
    private void initGrid(){
        Log.v("Game --> initGrid " , " Begin");
        for(int i=0; i < grid.getHeightInTiles() ; i++) {
            for (int j = 0; j < grid.getWidthInTiles() ; j++) {
                grid.setTile(j, i, new PieceView(this, i, j));
            }
        }
    }


    /** A acede e le  o ficheiro  de nivel do jogo */
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
    private void repaintTime() {
        timePlayed();
        if(seconds < 10){
            textViewTime.setText("\t"+minutes+":0"+ seconds);
        }
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
    public boolean onClick(int xTile, int yTile)  {
        if(startGame){startTime = System.nanoTime();   timeUpdate(); startGame = false;} // so vai ser executado uma vez
        grid.setTile(xTile, yTile, new PieceView(this, yTile, xTile, true));

        if(modelGame.isOver()) textViewTime.setText("Wou Win!!!");
        return false;
    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
        modelGame.setPiece( yFrom,  xFrom,  yTo,  xTo);

        PieceView aux = (PieceView) grid.getTile(xTo, yTo);
        grid.setTile(xTo, yTo, grid.getTile(xFrom, yFrom));
        grid.setTile(xFrom, yFrom, aux);
        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {

    }


    @Override
    public void onDragCancel() {}

}
