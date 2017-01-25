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
//        grid.setSize(5,6);
//        grid.setTile(1,4, new PieceView(this));
        initGridToWithEmptyPiece();
//        Log.v("EditorAc ", "size "+ grid.getHeightInTiles());

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
//        for(int i=0; i < grid.getHeightInTiles(); i++ ) {
//            for (int j = 0; j < grid.getWidthInTiles(); j++) {
//            //   grid.setTile(i, j, new PieceView(this, 2));
//            }
//        }
        Log.e("initGridToWithEmptyP", " end");
    }


    private boolean  fix = actionSel.getCheckedRadioButtonId() == R.id.fix;

    @Override
    public boolean onClick(int xTile, int yTile) throws IllegalAccessException, InstantiationException {
        Log.e("onClick-EditorActivity", "xTile = " +xTile + " yTile " +yTile);
        grid.setTile(xTile, yTile, new PieceView(this, piecePicker.getSelected(),
                                                    piecePicker.getColor(), fix ));
//        grid.setTile(xTile, yTile, new PieceView(this));
        //grid.setListener(null);
        return false;
    }

    @Override
    public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
        final int empty = 2;
        if(actionSel.getCheckedRadioButtonId() == R.id.move) {
            grid.setTile(xFrom, yFrom, new PieceView(this, empty, piecePicker.getColor(), fix));
            grid.setTile(xTo, yTo, new PieceView(this, piecePicker.getSelected(), piecePicker.getColor(), fix));
        }

        return false;
    }

    @Override
    public void onDragEnd(int x, int y) {

    }

    @Override
    public void onDragCancel() {

    }
}
