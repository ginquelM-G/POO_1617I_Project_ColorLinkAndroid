package pt.isel.poo.colorlink.game.model;


import android.util.Log;

import java.util.HashMap;
import java.util.Scanner;

import static pt.isel.poo.colorlink.game.model.Piece.Direction.DOWN;
import static pt.isel.poo.colorlink.game.model.Piece.Direction.LEFT;
import static pt.isel.poo.colorlink.game.model.Piece.Direction.RIGHT;
import static pt.isel.poo.colorlink.game.model.Piece.Direction.UP;

/**
 *  Created by Moreira on 12/11/2016.
 */
public class Grid {

    public static int LINE, COL;
    private Piece[][] pieces;
    private static HashMap<Integer, Piece> mapIdxImgType;

    public Grid(){
        init();
    }

    /** Passando x e y eh retorna a 'Piece' na posicao x e y*/
    public Piece getPiece(int x, int y){
        return pieces[x][y];
    }

    /** Este metodo eh usado para trocas duas 'Piece' */
    public void setPiece(int xFrom, int yFrom, int xTo, int yTo){
        Piece aux = pieces[xTo][yTo];
        pieces[xTo][yTo] = pieces[xFrom][yFrom];
        pieces[xFrom][yFrom] = aux;

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

    public void showDir(){
        for(int i=0; i < LINE; i++) {
            for (int j = 0; j < COL; j++) {
//                if (pieces[i][j].getype() == 'E' || pieces[i][j].getype() == 'B')
//                    System.out.print("____ ");
//                else
//                    System.out.print(pieces[i][j].getDir() + "   ");

            }
            System.out.println();
        }
    }

    public boolean isOver() {
        showDir();
        for(int i =0; i < LINE; i++ ){
            for(int j=0; j < COL ; j++){
                boolean res = true;
                if(pieces[i][j].getype() == 'E' || pieces[i][j].getype() == 'B') continue;

                Piece.Direction dir = pieces[i][j].getDir();

//                Log.e("######## -->>> isOver", "Dir = " +dir);
                if(pieces[i][j].getype() == 'C' && !adjacentOfCorner(i, j)){  res = false;}
                if(pieces[i][j].getype() == 'S' && !adjacentOfSide(i, j)){ res = false; }
                if(adjacentOfSide(i, j)) res = true;

                if(!res) return false;
            }
        }
        return true;
    }

    private boolean adjacentOfCorner(int i, int j){
        Piece.Direction dir = pieces[i][j].getDir();

        if(dir == LEFT){
            if(j - 1 >= 0 && pieces[i][j-1].getype() != 'S' && pieces[i][j-1].getDir() != UP ) return false;
            if(i - 1 >= 0 && pieces[i-1][j].getype() != 'S' && pieces[i-1][j].getDir() != DOWN ) return false;
        }
        else if(dir == UP){
            if(j + 1 >= 0 && j + 1  < COL && pieces[i][j+1].getype() != 'S' && pieces[i][j+1].getDir() != LEFT ) return false;
            if(i - 1 >= 0 && pieces[i-1][j].getype() != 'S' && pieces[i-1][j].getDir() != RIGHT ) return false;
        }
        else if(dir == RIGHT){
            if(j + 1 >= 0 && j +1  < COL && pieces[i][j+1].getype() != 'S' && pieces[i][j+1].getDir() != DOWN ) return false;
            if(i + 1 >= 0 && i + 1  < LINE && pieces[i+1][j].getype() != 'S' && pieces[i+1][j].getDir() != UP ) return false;
        }
        else if(dir == DOWN){
            if(j - 1 >= 0 && pieces[i][j-1].getype() != 'S' && pieces[i][j-1].getDir() != RIGHT ) return false;
            if(i + 1 >= 0 && i + 1  < LINE && pieces[i+1][j].getype() != 'S' && pieces[i+1][j].getDir() != LEFT ) return false;
        }

        return true;
    }

    private boolean adjacentOfSide(int i, int j){
        Piece.Direction dir = pieces[i][j].getDir();

        if(dir == LEFT){
            if(i - 1 >= 0 && pieces[i-1][j].getDir() != DOWN ) return false;
            if(i + 1 >= 0 && i + 1 < LINE && pieces[i+1][j].getDir() != LEFT ) return false;
        }
        else if(dir == UP){
            if(j + 1 >= 0 && j + 1  < COL && pieces[i][j+1].getDir() != LEFT ) return false;
            if(j - 1 >= 0 && pieces[i][j-1].getDir() != UP ) return false;
        }
        else if(dir == RIGHT){
            if(i + 1 >= 0 && j +1  < LINE && pieces[i+1][j].getDir() != UP ) return false;
            if(i - 1 >= 0 &&  pieces[i-1][j].getDir() != RIGHT ) return false;
        }
        else if(dir == DOWN){
            if(j - 1 >= 0 && pieces[i][j-1].getDir() != RIGHT ) return false;
            if(j + 1 >= 0 && i + 1  < COL && pieces[i][j+1].getDir() != DOWN ) return false;
        }

        return true;
    }


    private boolean adjacentOfInvert(int i, int j){
        Piece.Direction dir = pieces[i][j].getDir();

        if(dir == LEFT){
            if(i - 1 >= 0 && pieces[i-1][j].getDir() != DOWN ) return false;
            if(i + 1 >= 0 && i + 1 < LINE && pieces[i+1][j].getDir() != LEFT ) return false;
        }
        else if(dir == UP){
            if(j + 1 >= 0 && j + 1  < COL && pieces[i][j+1].getDir() != LEFT ) return false;
            if(j - 1 >= 0 && pieces[i][j-1].getDir() != UP ) return false;
        }
        else if(dir == RIGHT){
            if(i + 1 >= 0 && j +1  < LINE && pieces[i+1][j].getDir() != UP ) return false;
            if(i - 1 >= 0 &&  pieces[i-1][j].getDir() != RIGHT ) return false;
        }
        else if(dir == DOWN){
            if(j - 1 >= 0 && pieces[i][j-1].getDir() != RIGHT ) return false;
            if(j + 1 >= 0 && i + 1  < COL && pieces[i][j+1].getDir() != DOWN ) return false;
        }

        return true;
    }


    private boolean adjacentOfLink(int i, int j){
        Piece.Direction dir = pieces[i][j].getDir();

        if(dir == LEFT){
            if(i - 1 >= 0 && pieces[i-1][j].getDir() != DOWN ) return false;
            if(i + 1 >= 0 && i + 1 < LINE && pieces[i+1][j].getDir() != LEFT ) return false;
        }
        else if(dir == UP){
            if(j + 1 >= 0 && j + 1  < COL && pieces[i][j+1].getDir() != LEFT ) return false;
            if(j - 1 >= 0 && pieces[i][j-1].getDir() != UP ) return false;
        }
        else if(dir == RIGHT){
            if(i + 1 >= 0 && j +1  < LINE && pieces[i+1][j].getDir() != UP ) return false;
            if(i - 1 >= 0 &&  pieces[i-1][j].getDir() != RIGHT ) return false;
        }
        else if(dir == DOWN){
            if(j - 1 >= 0 && pieces[i][j-1].getDir() != RIGHT ) return false;
            if(j + 1 >= 0 && i + 1  < COL && pieces[i][j+1].getDir() != DOWN ) return false;
        }

        return true;
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

        return null;
    }
/**
    public void initCell(){
        list_cell = new HashMap<Character,Piece>();
        list_cell.put('E', new Empty());
        list_cell.put('B', new Block());
        list_cell.put('S', new Side());
        list_cell.put('C', new Corner());
        list_cell.put('I', new Invert());
        list_cell.put('L', new Link());
    }
*/





    private void init() {
        if (mapIdxImgType == null) {
            mapIdxImgType = new HashMap<>();
            mapIdxImgType.put(0, new Block());
            mapIdxImgType.put(1, new Corner());
            mapIdxImgType.put(2, new Empty());
            mapIdxImgType.put(3, new Invert());
            mapIdxImgType.put(4, new Link());
            mapIdxImgType.put(5, new Side());
        }
    }
}
