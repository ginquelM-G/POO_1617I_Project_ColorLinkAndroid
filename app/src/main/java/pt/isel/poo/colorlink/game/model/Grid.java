package pt.isel.poo.colorlink.game.model;


import android.util.Log;

import java.util.HashMap;
import java.util.Scanner;

import static pt.isel.poo.colorlink.game.model.Piece.Direction.*;

/**
 *  Created by Moreira on 12/11/2016.
 */
public class Grid {

    public static int LINE, COL;
    public static Piece[][] pieces;
    static HashMap<Character,Piece> list_cell;

    public Grid(){
        //initCell();
    }

    public Grid(int line, int col){}

    public boolean rotate(int line, int col) {
        if(pieces[line/3][col/3] instanceof Block||  pieces[line/3][col/3] instanceof Empty
                || pieces[line/3][col/3].isBlocked())
            return false;

        Piece.Direction dir = pieces[line/3][col/3].getDir();
        System.out.println("rotate() -> Direction " +dir +" ordinal "+ dir.ordinal());

        switch(dir){
            case UP: pieces[line/3][col/3].setDir(RIGHT); break;
            case RIGHT: pieces[line/3][col/3].setDir(DOWN); break;
            case DOWN: pieces[line/3][col/3].setDir(LEFT); break;
            case LEFT: pieces[line/3][col/3].setDir(UP); break;
        }
        return true;
    }

    /**
     * @param level
     * Lê o ficheiro e criar as peças definindo também o tamanho da Consola/grelha
     */
    public void load(Scanner level) {
        String [] lineCol = level.nextLine().split(" ");
        LINE = Integer.parseInt(lineCol[0]);
        COL = Integer.parseInt(lineCol[2]);
        pieces = new Piece[LINE][COL];
        //int i=0, j=0, auxNextLine =0;

        for(int i=0, j=0,auxNextLine=0;  level.hasNext() ; ){
            String cellDesc = level.next();
            System.out.print(cellDesc + " ");
            int k=0;
            char letra = cellDesc.charAt(k); // letra - Letra representante do Tipo da Celula

            if(pieces[i][j] == null) {
                pieces[i][j] = getCell(letra);
                if(++k < cellDesc.length() && cellDesc.charAt(k) !='+') {
                    pieces[i][j].setColor((cellDesc.charAt(k)) - '0');
                    if(++k < cellDesc.length() ) {
                        For_end:
                        for (Piece.Direction d : Piece.Direction.values()) {
                            if (d.getDirection() == cellDesc.charAt(k)) {
                                pieces[i][j].setDir(d);
                                if (k + 1 == cellDesc.length()) {
                                    break For_end;
                                }
                            }
                        }
                    }
                    if(++k < cellDesc.length()) {
                        pieces[i][j].setBlocked(true);
                    }
                }
            }
            if(++auxNextLine == COL){ // Fim da linha actual, muda o cursor para próxima linha
                System.out.println();
                i++; j=0; auxNextLine=0;
            }
            else j++;
        }
        Log.e("LOAD ","END YAPP");
        System.out.println("END LOAD ");
    }

    public boolean isOver() {
        return false;
    }

    public Piece getCell(char c){
        switch(c){
            case 'E':  return new Empty();
            case 'B':  return new Block();
            case 'S':  return new Side();
            case 'C':  return new Corner();
            case 'I':  return new Invert();
            case 'L':  return new Link();
        }
//        if(list_cell.containsKey(c))
//            return list_cell.get(c);

        return null;
    }

    public void initCell(){
        list_cell = new HashMap<Character,Piece>();
        list_cell.put('E', new Empty());
        list_cell.put('B', new Block());
        list_cell.put('S', new Side());
        list_cell.put('C', new Corner());
        list_cell.put('I', new Invert());
        list_cell.put('L', new Link());
    }
}
