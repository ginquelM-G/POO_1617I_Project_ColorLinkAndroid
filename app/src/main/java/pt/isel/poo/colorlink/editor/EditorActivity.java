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

import pt.isel.poo.colorlink.R;
import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.TilePanel;

/**
 * Activity that implements the ColorLink Game level editor
 */
public class EditorActivity extends AppCompatActivity  implements OnTileTouchListener{
    private PiecePicker piecePicker;        // Piece type selector.
    private RadioGroup colorSel, actionSel; // Selectors of color and action.
    private TilePanel grid;                 // Edit area.

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


        model = new EditorModel(grid.getHeightInTiles(), grid.getWidthInTiles());

//        model.pieces[][] = new Pieces()
//        model.setPieces(model.pieces,1,1, new Pieces());

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
     * The colors of the pieces displayed in selector change according to the chosen color.
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

        EditorView p =  new EditorView(this, emptyIdx, piecePicker.getColor(), true, false, false, false);

        for(int i=0; i < line; i++ ) {
            for (int j = 0; j < col; j++) {
                grid.setTile(i, j, p);
            }
        }
        p.emptyImgage = null;
        Log.e("initGridToWithEmptyP", " end");

    }


    private boolean  fix =false;



    @Override
    public boolean onClick(int xTile, int yTile) throws IllegalAccessException, InstantiationException {
      //  fix = (actionSel!= null)?actionSel.getCheckedRadioButtonId() == R.id.fix:false;
        Log.e("onClick-EditorActivity", "xTile = " +xTile + " yTile " +yTile);
        int checkedRadioButtonId = actionSel.getCheckedRadioButtonId();

        if(checkedRadioButtonId == R.id.insert ) {
            model.pieces[xTile][yTile] = new Pieces(piecePicker.getSelected(), piecePicker.getColor());
         //   grid.setTile(xTile, yTile, new EditorView(this, model.pieces[xTile][yTile] , fix, false));
        }
        else if(checkedRadioButtonId == R.id.rotate) {
            System.out.println("\n\nEditorActivity --> OnClick: rotateIsChecked\n\n" );
            if(model.pieces[xTile][yTile] != null){
                Log.e("pieces[xTile][yTile]", "!= null");
                EditorView ev = new EditorView(this, model.pieces[xTile][yTile] , fix, true);
                grid.setTile(xTile, yTile, ev);
                model.pieces[xTile][yTile].setImage(ev.imageAfterRotate);
            }else{
                actionSel.check(R.id.insert);
                model.pieces[xTile][yTile] = new Pieces(piecePicker.getSelected(), piecePicker.getColor());
                grid.setTile(xTile, yTile, new EditorView(this, model.pieces[xTile][yTile] , fix, false));
            }
        }
        else if(checkedRadioButtonId == R.id.move){
            if(makeAMove){
                if(model.pieces[xFromOnClick][yFromOnClick] != null ){
                    final int emptyIdx = 2;
                    model.pieces[xTile][yTile] = model.pieces[xFromOnClick][yFromOnClick];
                    model.pieces[xFromOnClick][yFromOnClick] =  null;

//                    grid.setTile(xFromOnClick, yFromOnClick, new EditorView(this, emptyIdx, piecePicker.getColor(), fix, false, true));
                    grid.setTile(xFromOnClick, yFromOnClick, new EditorView(this, emptyIdx, piecePicker.getColor(), false, false, true, false));
                    grid.setTile(xTile, yTile, new EditorView(this, model.pieces[xTile][yTile], fix, false));
                }

                else if(model.pieces[xTile][yTile] != null) {
                    final int emptyIdx = 2;
                    model.pieces[xFromOnClick][yFromOnClick] =  model.pieces[xTile][yTile];
                    model.pieces[xTile][yTile] = null;

                    grid.setTile(xTile, yTile, new EditorView(this, emptyIdx, piecePicker.getColor(), false, false, true, false));
                    grid.setTile(xFromOnClick, yFromOnClick, new EditorView(this, model.pieces[xFromOnClick][yFromOnClick], fix, false));
                }
                makeAMove = false;
            }
            else{
                xFromOnClick = xTile;
                yFromOnClick = yTile;
                makeAMove = true;
            }

        }
        else if(checkedRadioButtonId == R.id.fix) {
            if (model.pieces[xTile][yTile] != null) {
                Log.e("pieces[xTile][yTile]", "!= null");
                grid.setTile(xTile, yTile, new EditorView(this, model.pieces[xTile][yTile], true, false));
            }
        }

//        grid.setListener(null);
        showTiles(model.pieces);
        return true;
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

    public void showTiles(Pieces [][] p){
        for (int i=0; i < 6; i++) {
            for (int j = 0; j < 6; j++){
                if(p[j][i] != null){
                    System.out.print(" ## ");
                }
                else System.out.print(" __ ");
            }
            System.out.println();
        }
    }
}
