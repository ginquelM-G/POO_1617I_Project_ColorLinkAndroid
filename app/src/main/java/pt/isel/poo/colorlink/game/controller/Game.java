package pt.isel.poo.colorlink.game.controller;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.editor.PiecePicker;
import pt.isel.poo.colorlink.game.model.Grid;
import pt.isel.poo.colorlink.game.view.EmptyPieceView;
import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.TilePanel;

/**
 * Created by Moreira on 17/02/2017.
 */

public class Game extends AppCompatActivity implements OnTileTouchListener {


    private PiecePicker piecePicker;        // Piece type selector.
    private RadioGroup colorSel, actionSel; // Selectors of color and action.
    private TilePanel grid;                 // Edit area.


    public static Grid modelGame;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        grid = (TilePanel) findViewById(R.id.panelGame);
        grid.setListener(this);

        modelGame = new Grid();

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        grid.setSize(modelGame.LINE, modelGame.COL);
        grid.setSize(modelGame.COL, modelGame.LINE);
        initGrid();

        Log.e("Line "+modelGame.LINE, " Col "+ modelGame.COL );
        System.out.println("hello hello");

//        grid.setSize(modelGame.LINE, modelGame.COL);
//        model = new EditorModel(grid.getHeightInTiles(), grid.getWidthInTiles());

//        model.pieces[][] = new Pieces()
//        model.setPieces(model.pieces,1,1, new Pieces());
//        Log.e("GAME  ", "size ");

//        grid.setTile(1,4, new EditorView(this));
//        initGridToWithEmptyPiece();
        ;
//        Log.e("GAME  ", "size "+ grid.getHeightInTiles());

    }



    private void init() throws IOException {
        AssetManager assetManager = getAssets();
        loadLevel("Level1");
//        if(!loadLevel("Level1")) return;

//
        Log.e("init() " , "  end");
    }

    private void initGrid(){
        Log.e("initGrid " , " Begin");

        for(int i=0; i < modelGame.LINE  -1 ; i++) {
            for (int j = 0; j < modelGame.COL ; j++) {
                System.gc();
                grid.setTile(j, i, new EmptyPieceView(this, modelGame.pieces[i][j].getype() , modelGame.pieces[i][j].getColor()));
//                grid.setTile(i, j, new EmptyPieceView(this, 'S' , 2));
            }
            System.gc();
        }
    }

    private boolean loadLevel(String filename) throws IOException {
        try{
//            Scanner level = new Scanner(new FileInputStream(filename+".txt"));
            InputStream file = getAssets().open(filename+".txt");

            Scanner level = new Scanner(file);
            modelGame.load(level);
            level.close();

            System.out.println("Read Complete!!!");
            return true;

        }
        catch (FileNotFoundException | InputMismatchException e){
            System.out.println("Error loading file "+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onClick(int xTile, int yTile) throws IllegalAccessException, InstantiationException {

//        Log.e("x "+xTile, " y"+ yTile );
//        Log.e("Type ", "    "+modelGame.pieces[xTile][yTile].getype());
        grid.setTile(xTile, yTile, new EmptyPieceView(this, modelGame,
                modelGame.pieces[yTile][xTile].getype(), modelGame.pieces[yTile][xTile].getColor(), true));
//        grid.setTile(xTile, yTile, new EmptyPieceView(this, modelGame,
//                modelGame.pieces[yTile][xTile].getype(), modelGame.pieces[yTile][xTile].getColor(), true));
        return false;
    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {

    }

    @Override
    public void onDragCancel() {

    }
}
