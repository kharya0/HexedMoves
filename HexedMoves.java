import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author user
 */

class Cell {
     int colors[] = {0,1,2};
     int row;
     int col;
     int currentColor;

     public Cell(){
         ;
     }

     public Cell (int i, int j){
         row = i;
         col = j;
     }

     public Cell(int color, int row, int col){
         currentColor = color;
         this.row = row;
         this.col = col;
     }
 } // end of class cell

class Board{
     int rows = 7;
     int columns = 9;

     Cell[][] cells = new Cell[rows][columns];

     Board(){
         ;
     }

    public static int[][] setInitialBoard(int row, int col, int color){
        int[][] startingBoard = new int[col][row];

        // assign no color to all tiles.
        // loop through cols
        for (int i = 0; i < startingBoard.length; i++) {
            //loop through rows
            for (int j = 0; j < startingBoard[i].length; j++) {
                startingBoard[i][j] = HexedMoves.NO_COLOR;
            }
        }

        if (color == HexedMoves.GREEN) {
            //even
            if(col % 2 == 0){
                startingBoard[col][row] = HexedMoves.GREEN;
                startingBoard[col-1][row-1] = HexedMoves.RED;
                startingBoard[col-1][row-2] = HexedMoves.GREEN;
                startingBoard[col][row-2] = HexedMoves.RED;
                startingBoard[col+1][row-2] = HexedMoves.GREEN;
                startingBoard[col+1][row-1] = HexedMoves.RED;
            }else{
                startingBoard[col][row] = HexedMoves.GREEN;
                startingBoard[col-1][row] = HexedMoves.RED;
                startingBoard[col-1][row-1] = HexedMoves.GREEN;
                startingBoard[col][row-2] = HexedMoves.RED;
                startingBoard[col+1][row-1] = HexedMoves.GREEN;
                startingBoard[col+1][row] = HexedMoves.RED;
            }
        }

        if (color == HexedMoves.RED) {
            //even
            if(col % 2 == 0){
                startingBoard[col][row] = HexedMoves.RED;
                startingBoard[col-1][row-1] = HexedMoves.GREEN;
                startingBoard[col-1][row-2] = HexedMoves.RED;
                startingBoard[col][row-2] = HexedMoves.GREEN;
                startingBoard[col+1][row-2] = HexedMoves.RED;
                startingBoard[col+1][row-1] = HexedMoves.GREEN;
            }else{
                startingBoard[col][row] = HexedMoves.RED;
                startingBoard[col-1][row] = HexedMoves.GREEN;
                startingBoard[col-1][row-1] = HexedMoves.RED;
                startingBoard[col][row-2] = HexedMoves.GREEN;
                startingBoard[col+1][row-1] = HexedMoves.RED;
                startingBoard[col+1][row] = HexedMoves.GREEN;
            }
        }
        return startingBoard;
    }

     public void show(){
         for(int i=0; i < rows; i++){
             for(int j=0; j < columns; j++){
                  System.out.print(cells[i][j].currentColor + " | ");
             }
             System.out.println("");
         }
     }

     void putMove(int player, int row, int col){
         cells[row][col].currentColor = player;
     }

     Cell getCell(int row, int col){
         return cells[row][col];
     }


     void updateBoard(){


     }

     void swapCells(Cell start, Cell end){
         Cell temp;
         temp = start;
         start = end;
         end = temp;
     }

     void doVerticalConvert(int newColor, Cell start, Cell end){
         for(int i = start.row + 2; i < end.row; i=i+2){
             cells[i][start.col].currentColor = newColor;
         }
     }

     void doLeftToRightDiagonalConvert(int newColor, Cell start, Cell end){
         int offset = start.row - start.col;
         for(int i = start.row + 1; i < end.row; i++){
             for(int j = start.col + 1; j < end.col; j++){
                 if(j == i - offset)cells[i][j].currentColor = newColor;
             }
         }
     }

     void doRightToLeftDiagonalConvert(int newColor, Cell start, Cell end){
         int sum = start.row + start.col;
         for(int i = start.row + 1; i < end.row; i++){
             for(int j = start.col - 1; j > end.col; j--){
                 if(i + j == sum)cells[i][j].currentColor = newColor;
             }
         }
     }


     void convert(int newColor, Cell start, Cell end){
         //determine if direction is
         //vertical, leftToRightDiagonal or rightToLeftDiagonal
         if(start.col == end.col){ //vertical
             if(start.row > end.row) { //reversed start and end cells
                 //swap the start and end cells for easier color conversion
                 swapCells(start, end);
             }
             //do the conversion for vertical
             doVerticalConvert(newColor, start, end);
         }
         else{ //diagonal
             if(start.col < end.col){
                 if(start.row < end.row) { //leftToRightDiagonal
                     //do the conversion for leftToRightDiagonal
                     doLeftToRightDiagonalConvert(newColor, start, end);
                 }
                 else{ //reversed rightToLeftDiagonal
                     swapCells(start, end);
                     //do the conversion for rightToLeftDiagonal
                     doRightToLeftDiagonalConvert(newColor, start, end);
                 }
             }

             if(start.col > end.col) {
                 if(start.row < end.row){ //rightToLeftDiagonal
                     //do the conversion for rightToLeftDiagonal
                      doRightToLeftDiagonalConvert(newColor, start, end);
                }
                 else { //reversed leftToRightDiagonal
                     swapCells(start, end);
                     //do the conversion for leftToRightDiagonal
                     doLeftToRightDiagonalConvert(newColor, start, end);
                 }
             }
         }

     }
 } //end of class Board


class MovesManager {
     Board board = new Board();
     int player1 = 1;
     int player2 = 2;
     ArrayList<Cell> player1moves = new ArrayList<Cell>();
     ArrayList<Cell> player2moves = new ArrayList<Cell>();
     //so it's easier to select a player's list
     ArrayList[] playersLists = {null, player1moves, player2moves};

     MovesManager(Board board){
         this.board = board;
     }

     void addMoveToPlayersList(int player, int row, int col){
         //no validation done to check if cell is currently owned by other player
         //get cell from board
         Cell cell = board.getCell(row,col);
         playersLists[player].add(cell);
     }

     void addMoveToPlayersList(Cell cell){
         //no validation done to check if cell is currently owned by other player
         //get cell from board
         playersLists[cell.currentColor].add(cell);
     }

     void getValidMoves(int player){
        //for  each cell that is around it, i.e. LUC, RUC, LBC, RBC, TC, BC)
        //and it does not own, check if the cell directly across the current
        //cell is not owned by any player (no color)
        //if it is then it's a valid move,
        //if not, continue to next cell along same direction
        //until you reach the border cell

     }

} // end of class MovesManager

public class HexedMoves {
    public static Scanner kbd = new Scanner(System.in);
    public static final int GREEN = 1; //constant color value assignments
    public static final int RED = 2;
    public static final int NO_COLOR = 0; // constant value for no color tile
    public static final int NO_TILE = 3; // constant value for no tile

    public static void main(String[] args) {
        System.out.println("test");

        Board board = new Board();
        MovesManager movesManager = new MovesManager(board);

        System.out.print("What player are you?[1/2]: ");
        int myPlayer = kbd.nextInt(); //select 1 or 2

        Cell[] initialMoves = new Cell[6];

        System.out.println("Enter initial game info.");
        System.out.print("Column: ");
        int initCol = kbd.nextInt();
        System.out.print("Row: ");
        int initRow = kbd.nextInt();
        System.out.print("Color[r/g]: ");
        char color = kbd.next().charAt(0);
        int initColor = 0;

        board.setInitialBoard(initRow, initCol, initColor);

        //replace values in constructors of cell instances
        initialMoves[0] = new Cell(initColor,initRow,initCol);
        if (initColor == HexedMoves.GREEN) {
            if (initCol % 2 == 0) {
                initialMoves[0] = new Cell(HexedMoves.GREEN,initRow,initCol);
                initialMoves[1] = new Cell(HexedMoves.GREEN,initRow - 2,initCol - 1);
                initialMoves[2] = new Cell(HexedMoves.GREEN,initRow - 2,initCol + 1);
                initialMoves[3] = new Cell(HexedMoves.RED,initRow - 1,initCol - 1);
                initialMoves[4] = new Cell(HexedMoves.RED,initRow - 2,initCol);
                initialMoves[5] = new Cell(HexedMoves.RED,initRow - 1 ,initCol + 1);
            } else {
                initialMoves[0] = new Cell(HexedMoves.GREEN,initRow,initCol);
                initialMoves[1] = new Cell(HexedMoves.GREEN,initRow - 1,initCol - 1);
                initialMoves[2] = new Cell(HexedMoves.GREEN,initRow - 1,initCol + 1);
                initialMoves[3] = new Cell(HexedMoves.RED,initRow,initCol - 1);
                initialMoves[4] = new Cell(HexedMoves.RED,initRow - 2,initCol);
                initialMoves[5] = new Cell(HexedMoves.RED,initRow,initCol + 1);
            }
        }
        if (initColor == HexedMoves.RED) {
            if (initCol % 2 == 0) {
                initialMoves[0] = new Cell(HexedMoves.RED, initRow,initCol);
                initialMoves[1] = new Cell(HexedMoves.RED,initRow - 2,initCol - 1);
                initialMoves[2] = new Cell(HexedMoves.RED,initRow - 2,initCol + 1);
                initialMoves[3] = new Cell(HexedMoves.GREEN,initRow - 1,initCol - 1);
                initialMoves[4] = new Cell(HexedMoves.GREEN,initRow - 2,initCol);
                initialMoves[5] = new Cell(HexedMoves.GREEN,initRow - 1 ,initCol + 1);
            } else {
                initialMoves[0] = new Cell(HexedMoves.RED, initRow,initCol);
                initialMoves[1] = new Cell(HexedMoves.RED,initRow - 1,initCol - 1);
                initialMoves[2] = new Cell(HexedMoves.RED,initRow - 1,initCol + 1);
                initialMoves[3] = new Cell(HexedMoves.GREEN,initRow,initCol - 1);
                initialMoves[4] = new Cell(HexedMoves.GREEN,initRow - 2,initCol);
                initialMoves[5] = new Cell(HexedMoves.GREEN,initRow,initCol + 1);
            }
        }


        //update board and players moves list for initial moves
        Cell cell;
        for(int i=0; i<6; i++){
            cell = initialMoves[i];
            board.putMove(cell.currentColor, cell.row, cell.col);
            movesManager.addMoveToPlayersList(cell);
        }
        board.show();

        //while game is not done
        //while(true){
        //identify valid moves for selected player
        //movesManager.getValidMoves(myPlayer);


        //identify best move from valid moves
        //put move: put it on the board
        //update board including cell conversions and redraw board
        //}



    }
//   // ====== tests done: you can remove this ==============================
//   public static void convertTest(Board testBoard){
//         //vertical
//        setData(testBoard, "rightToLeft");
//        testBoard.show();
//        System.out.println();
//        System.out.println();
//        //testBoard.convert(player1, testBoard.cells[4][7], testBoard.cells[7][4]);
//        testBoard.show();
//        // =====================================================================
//    }
//
//
//
//
//    //======================= tests ============================
//
//
//    public static void setData(Board testBoard, String direction){
//        if(direction.equals("vertical")){
//            testBoard.putMove(1,1,1);
//            testBoard.putMove(2,3,1);
//            testBoard.putMove(2,5,1);
//            testBoard.putMove(1,7,1);
//        }
//        if(direction.equals("leftToRight")){
//            testBoard.putMove(1,1,1);
//            testBoard.putMove(2,2,2);
//            testBoard.putMove(2,3,3);
//            testBoard.putMove(1,4,4);
//        }
//        if(direction.equals("rightToLeft")){
//            testBoard.putMove(1,4,7);
//            testBoard.putMove(2,5,6);
//            testBoard.putMove(2,6,5);
//            testBoard.putMove(1,7,4);
//        }
//    }
//
//
//
//    public static void verticalTest(Board testBoard){
//        setData(testBoard, "vertical");
//        testBoard.show();
//        System.out.println();
//        System.out.println();
//        testBoard.doVerticalConvert(player1, testBoard.cells[1][1], testBoard.cells[7][1]);
//        testBoard.show();
//    }
//
//    public static void leftToRightDiagonalTest(Board testBoard){
//        setData(testBoard, "leftToRight");
//        testBoard.show();
//        System.out.println();
//        System.out.println();
//        testBoard.doLeftToRightDiagonalConvert(1, testBoard.cells[1][1], testBoard.cells[4][4]);
//        testBoard.show();
//    }
//
//    public static void rightToLeftDiagonalTest(Board testBoard){
//        setData(testBoard, "rightToLeft");
//        testBoard.show();
//        System.out.println();
//        System.out.println();
//        testBoard.doRightToLeftDiagonalConvert(1, testBoard.cells[4][7], testBoard.cells[7][3]);
//        testBoard.show();
//    }
//
} // end of class HexedMoves
