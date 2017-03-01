package pt.isel.poo.colorlink.editor;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import pt.isel.poo.colorlink.R;
import pt.isel.poo.colorlink.game.model.Block;
import pt.isel.poo.colorlink.game.model.Corner;
import pt.isel.poo.colorlink.game.model.Empty;
import pt.isel.poo.colorlink.game.model.Grid;
import pt.isel.poo.colorlink.game.model.Invert;
import pt.isel.poo.colorlink.game.model.Link;
import pt.isel.poo.colorlink.game.model.Piece;
import pt.isel.poo.colorlink.game.model.Side;
import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.TilePanel;

import static pt.isel.poo.colorlink.editor.EditorModel.piecesEd;

/**
 * Activity that implements the ColorLink Game level editor
 */
public class EditorActivity extends AppCompatActivity  implements OnTileTouchListener{
    private PiecePicker piecePicker;        // Piece type selector.
    private RadioGroup colorSel, actionSel; // Selectors of color and action.
    private TilePanel grid;                 // Edit area.

    public static Grid modelEditor;
    public EditorView pp;
    public static EditorModel model;
    public boolean makeAMove;
    int xFromOnClick, yFromOnClick;


    /**
     * Initialization of all activity components.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        piecePicker = (PiecePicker) findViewById(R.id.select);
        piecePicker.setSelected(0);
        initColorSelector();
        initActionSelector();
        grid = (TilePanel) findViewById(R.id.panel);
        grid.setListener(this);

        modelEditor = new Grid();

        model = new EditorModel(grid.getHeightInTiles(), grid.getWidthInTiles());

        init();
//        model.pieceEditor[][] = new PieceEditor()
//        model.setPieces(model.pieceEditor,1,1, new PieceEditor());
//        grid.setSize(5,6);
//        grid.setTile(1,4, new EditorView(this));
        initGridToWithEmptyPiece();
        Log.e("EditorAc ", "size "+ grid.getHeightInTiles());

    }

    /**
     * Loads the options menu in XML
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    /**
     * Called whenever an item in options menu is selected.
     * @param item The item selected
     * @return true if the item is consumed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                return true;
            //case R.id.load: load(); return true;
            //case R.id.create: create(); return true;
        }
        return false;
    }

    // Last name of file used in save dialog.
    private String lastName = null;

    /**
     * Opens a dialog window to ask for the name of the file to be saved.<br/>
     * The "save" button starts saving.<br/>
     * The "cancel" button aborts the operation.
     */
    private void save() {
        final EditText edit = new EditText(this);
        if (lastName != null) edit.setText(lastName);
        new AlertDialog.Builder(this)
                .setTitle(R.string.save)
                .setView(edit)
                .setMessage(R.string.file_name)
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveToFile(lastName = edit.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * Save edited area to a text file
     * @param fileName The file name
     */
    private void saveToFile(String fileName) {
        try {
            OutputStream os = openFileOutput(fileName, Activity.MODE_PRIVATE);
            PrintWriter out = new PrintWriter(os);
            //saveToStream(out);
            out.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "error on saveToFile", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Starts the color picker.<br/>
     * The colors of the pieceEditor displayed in selector change according to the chosen color.
     */
    private void initColorSelector() {
        colorSel = (RadioGroup) findViewById(R.id.selectColor);
        colorSel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton item = (RadioButton) colorSel.findViewById(id);
                if (item.isChecked())
                    piecePicker.setColor(item.getCurrentTextColor());
            }
        });
        colorSel.check(R.id.red);

    }

    /**
     * Starts the action picker.<br/>
     * Each touch on the edit area reacts according to the selected action.
     */
    private void initActionSelector() {
        actionSel = (RadioGroup) findViewById(R.id.selectAction);
        actionSel.check(R.id.insert);
    }

    private void initGridToWithEmptyPiece(){
        final int emptyIdx = 2;
        Log.e("initGridToWithEmptyP", "grid.getWidthInTiles() "+ grid.getWidthInTiles());
        int line = grid.getHeightInTiles();
        int col = grid.getWidthInTiles();

        EditorView p =  new EditorView(this, emptyIdx, true, false, false, false);

        for(int i=0; i < line; i++ ) {
            for (int j = 0; j < col; j++) {
                grid.setTile(i, j, p);
            }
        }
        Log.e("initGridToWithEmptyP", " end");

    }


    private boolean  fix =false;



    @Override
    public boolean onClick(int xTile, int yTile) throws IllegalAccessException, InstantiationException {
        Log.e("onClick-EditorActivity", "xTile = " +xTile + " yTile " +yTile);
        int checkedRadioButtonId = actionSel.getCheckedRadioButtonId();


        if(checkedRadioButtonId == R.id.insert ) {
            insert(yTile, xTile); // lin col
        }
        else if(checkedRadioButtonId == R.id.rotate) {

            rotate(yTile, xTile);
        }
        else if(checkedRadioButtonId == R.id.move){
            move(yTile, xTile);
        }
        else if(checkedRadioButtonId == R.id.fix) { fix(yTile, xTile);}

        if(makeAMove && checkedRadioButtonId != R.id.move ) makeAMove =false;

//        grid.setListener(null);
        showTiles(piecesEd);
        return false;
    }

    public void insert(int xTile, int yTile){
        Log.i("onClick()", "insert");
        Piece piece = mapIdxImgType.get(piecePicker.getSelected());
        PieceEditor p = new PieceEditor(piece, piecePicker.getSelected(), piecePicker.getColor());
        piecesEd[xTile][yTile] = p;
        grid.setTile(yTile, xTile, new EditorView(this, p, xTile, yTile, true, false, false, false)); //trocar a ordem de x e y
    }
    public void rotate(int xTile, int yTile){
        Log.i("onClick()", "rotate");
        if(piecesEd[xTile][yTile] != null){
            PieceEditor p = piecesEd[xTile][yTile];
            System.out.println("\t>>>>>>>>>>>>>> \n\nEditorActivity: rotate() --> type = "
                    + p.getPiece().getype()+ " p.getAngle "+p.getAngle() + "<<<<<<<<<<<<<<<<<<<<<<<");
//            grid.setTile(xTile, yTile, new EditorView(this, p.getIdxImage(), p.getColor(), false, true, false, false));
            grid.setTile(yTile, xTile, new EditorView(this, p, xTile, yTile, false, true, false, false));
//            EditorView ev = (EditorView) grid.getTile(xTile,yTile);
//            grid.setTile(xTile, yTile, ev);
        }else{
            actionSel.check(R.id.insert);
            insert(xTile, yTile);
//            Piece piece = mapIdxImgType.get(piecePicker.getSelected());
//            PieceEditor p = new PieceEditor(piece, piecePicker.getSelected(), piecePicker.getColor());
//            piecesEd[xTile][yTile] = p;
//            grid.setTile(xTile, yTile, new EditorView(this, p.getIdxImage(),  true, false, false, false));
        }
    }
    public void move(int xTile, int yTile){
        if(makeAMove) {
            Log.v("->>> onClick makeAmove", "MOVE");
            if(piecesEd[xFromOnClick][yFromOnClick] != null || piecesEd[xTile][yTile] != null ){
                if(piecesEd[xFromOnClick][yFromOnClick] != null && piecesEd[xTile][yTile] != null){
                    PieceEditor aux = piecesEd[xTile][yTile];
                    piecesEd[xTile][yTile] = piecesEd[xFromOnClick][yFromOnClick];
                    piecesEd[xFromOnClick][yFromOnClick] = aux;
                }
                else if (piecesEd[xFromOnClick][yFromOnClick] != null) {
                    piecesEd[xTile][yTile] = piecesEd[xFromOnClick][yFromOnClick];
                    piecesEd[xFromOnClick][yFromOnClick] = null;
                }else{ //if (pieces[xTile][yTile] != null) {
                    piecesEd[xFromOnClick][yFromOnClick] = piecesEd[xTile][yTile];
                    piecesEd[xTile][yTile] = null;
                }
                EditorView aux = (EditorView) grid.getTile(yFromOnClick, xFromOnClick);
                grid.setTile(yFromOnClick, xFromOnClick,  grid.getTile(yTile, xTile)); // move para nova posicao
                grid.setTile(yTile, xTile, aux); // limpa a antiga posicao
//                grid.setTile(xFromOnClick, yFromOnClick, grid.getTile(xTile, yTile)); // move para nova posicao
//                grid.setTile(xTile, yTile, aux); // limpa a antiga posicao
            }
            makeAMove = false;
        }
        else{
            xFromOnClick = xTile;
            yFromOnClick = yTile;
            makeAMove = true;
        }
    }
    public void fix(int xTile, int yTile){
        PieceEditor pe = piecesEd[xTile][yTile];
        if (pe != null) {
            Log.e("EA-checkedRadioButtonId", "R.id.fix");
//          grid.setTile(xTile, yTile, new EditorView(this, piecePicker.getSelected(), piecePicker.getColor(), false, false, false, true));
//           grid.setTile(xTile, yTile, new EditorView(this, model.pieceEditor[xTile][yTile], true, false));
            grid.setTile(yTile, xTile, new EditorView(this, pe, xTile, yTile, false, false, false, true));
        }

//        Piece piece = mapIdxImgType.get(piecePicker.getSelected());
//        PieceEditor p = new PieceEditor(piece, piecePicker.getSelected(), piecePicker.getColor());
//        piecesEd[xTile][yTile] = p;

    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
//        final int empty = 2;
//        if(actionSel.getCheckedRadioButtonId() == R.id.move) {
//            Log.e("onDrag", "xFrom "+ xFrom+" yFrom "+yFrom);
//            grid.setTile(xFrom, yFrom, new EditorView(this, empty, piecePicker.getColor(), fix, false, 0));
////            grid.setTile(xTo, yTo, new EditorView(this, piecePicker.getSelected(), piecePicker.getColor(), fix));
//            return true;
//        }

        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {

//        if(actionSel.getCheckedRadioButtonId() == R.id.move && tileSelect ){
//            Log.e("onDragEnd", "x "+ x+" y "+y);
////            grid.setTile(x, y, new EditorView(this, piecePicker.getSelected(),
////                    piecePicker.getColor(), fix, false, pp.angleRotation));
////
////
////            grid.setTile(xFromOnClick, yFromOnClick,
////                    new EditorView(this, 2, piecePicker.getColor(), fix, false, 0));
//
//            tileSelect = false;
//        }
    }

    @Override
    public void onDragCancel() {

    }

    public void showTiles(PieceEditor [][] p){
        for (int i=0; i < 6; i++) {
            for (int j = 0; j < 6; j++){
                if(p[i][j] != null){
                    System.out.print(" ## ");
                }
                else System.out.print(" __ ");
            }
            System.out.println();
        }
    }


    public static HashMap<Integer, Character> mapImgType;
    public static HashMap<Integer, Piece> mapIdxImgType;
    public static HashMap<Piece, Integer> mapPieceIdxImg;


    public void init() {
        if (mapIdxImgType == null) {
            mapIdxImgType = new HashMap<>();
            mapIdxImgType.put(0, new Block());
            mapIdxImgType.put(1, new Corner());
            mapIdxImgType.put(2, new Empty());
            mapIdxImgType.put(3, new Invert());
            mapIdxImgType.put(4, new Link());
            mapIdxImgType.put(5, new Side());
        }
        if (mapIdxImgType == null) {
            mapPieceIdxImg = new HashMap<>();
            mapPieceIdxImg.put(new Block(), 0);
            mapPieceIdxImg.put(new Corner(), 1);
            mapPieceIdxImg.put(new Empty(), 2);
            mapPieceIdxImg.put(new Invert(), 3);
            mapPieceIdxImg.put(new Link(), 4);
            mapPieceIdxImg.put(new Side(), 5);
        }

        if (mapImgType == null) {
            mapImgType = new HashMap<>();
            mapImgType.put(0, 'B');
            mapImgType.put(1, 'C');
            mapImgType.put(2, 'E');
            mapImgType.put(3, 'I');
            mapImgType.put(4, 'L');
            mapImgType.put(5, 'S');
        }
//        if (mapImgType == null) {
//            mapImgType = new HashMap<>();
//            mapImgType.put(R.drawable.block, 'B');
//            mapImgType.put(R.drawable.corner, 'C');
//            mapImgType.put(R.drawable.empty, 'E');
//            mapImgType.put(R.drawable.invert, 'I');
//            mapImgType.put(R.drawable.link, 'L');
//            mapImgType.put(R.drawable.side, 'S');
//        }
    }
}
