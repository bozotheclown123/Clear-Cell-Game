package model;

import java.util.Random;

/**
 * This class extends GameModel and implements the logic of the clear cell game,
 * specifically.
 * 
 *
 */

public class ClearCellGameModel extends GameModel {

	/* Include whatever instance variables you think are useful. */
	int score;
	int rows;
	int cols;
	Random random;
	/**
	 * Defines a board with empty cells.  It relies on the
	 * super class constructor to define the board.
	 * 
	 * @param rows number of rows in board
	 * @param cols number of columns in board
	 * @param random random number generator to be used during game when
	 * rows are randomly created
	 */
	public ClearCellGameModel(int rows, int cols, Random random) {
		super(rows, cols);
		this.rows = rows;
		this.cols = cols;
		this.score = 0;
		this.random = random;
	}

	/**
	 * The game is over when the last row (the one with i equal
	 * to board.length-1) contains at least one cell that is not empty.
	 */
	public boolean isGameOver() {
		// The loop starts with the last row and the other loop will check each of its column 
		// to determine if a cell is not empty
		for (int i = board.length - 1; i <= board.length - 1; i++) {
			for (int j = 0; j < cols; j++) {
				if (board[i][j] != BoardCell.EMPTY) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the player's score.  The player should be awarded one point
	 * for each cell that is cleared.
	 * 
	 * @return player's score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * This method must do nothing in the case where the game is over.
	 * 
	 * As long as the game is not over yet, this method will do 
	 * the following:
	 * 
	 * 1. Shift the existing rows down by one position.
	 * 2. Insert a row of random BoardCell objects at the top
	 * of the board. The row will be filled from left to right with cells 
	 * obtained by calling BoardCell.getNonEmptyRandomBoardCell().  (The Random
	 * number generator passed to the constructor of this class should be
	 * passed as the argument to this method call.)
	 */
	public void nextAnimationStep() {
		// Must check first if the game is not over yet
		if (isGameOver() == false) {
			// This nested for-loop will will shift the existing rows down by one position
			for (int i = board.length - 1 ; i > 0; i--) {
				for (int j = 0; j < board[i].length; j++) {
					setBoardCell(i, j, getBoardCell (i - 1, j));
				}
			}
			// This for-loop will insert a row of random BoardCell objects at the top of the board
			for (int col = 0; col < board[0].length; col++) {
				board[0][col] = BoardCell.getNonEmptyRandomBoardCell(random);
			}
		}
	}
	// Private Helper Method
	// This method will take in one parameter, a row, and return true if the row is empty or return false if otherwise
	private boolean checkEmptyRowExists(int row) {
		int total = 0;
		for (int col = 0; col < board[0].length; col++) {
			if (getBoardCell(row, col) == BoardCell.EMPTY) {
				total++;
			}
		}
		if (total == board[0].length) {  
			return true;
		}
		return false;
	}

	/**
	 * This method is called when the user clicks a cell on the board.
	 * If the selected cell is not empty, it will be set to BoardCell.EMPTY, 
	 * along with any adjacent cells that are the same color as this one.  
	 * (This includes the cells above, below, to the left, to the right, and 
	 * all in all four diagonal directions.)
	 * 
	 * If any rows on the board become empty as a result of the removal of 
	 * cells then those rows will "collapse", meaning that all non-empty 
	 * rows beneath the collapsing row will shift upward. 
	 * 
	 * @throws IllegalArgumentException with message "Invalid row index" for 
	 * invalid row or "Invalid column index" for invalid column.  We check 
	 * for row validity first.
	 */
	public void processCell(int row, int col) {
		if (row < 0 || row > board.length) {
			throw new IllegalArgumentException("Invalid row index");
		}
		if (col < 0 || col > board[0].length) {
			throw new IllegalArgumentException("Invalid column index");
		}
		
		if (board[row][col] != BoardCell.EMPTY) {
			// North
			if (row > 0) {
				if (board[row - 1][col] == board[row][col]) {
					board[row - 1][col] = BoardCell.EMPTY;
					score = score + 1;
				}
				// North-East
				if (col + 1 < board[0].length) {
					if (board[row - 1][col + 1] == board[row][col]) {
						board[row - 1][col + 1] = BoardCell.EMPTY;
						score = score + 1;
					}
				}
				// North-West
				if (col > 0) {
					if (board[row - 1][col - 1] == board[row][col]) {
						board[row - 1][col - 1] = BoardCell.EMPTY;
						score = score + 1;
					}
				}
			}
			// South
			if (row + 1 < board.length) {
				if (board[row + 1][col] == board[row][col]) {
					board[row + 1][col] = BoardCell.EMPTY;
					score = score + 1;
				}
				// South-East
				if (col + 1 < board[0].length) {
					if (board[row + 1][col + 1] == board[row][col]) {
						board[row + 1][col + 1] = BoardCell.EMPTY;
						score = score + 1;
					}
				}
				// South-West
				if (col > 0) {
					if (board[row + 1][col - 1] == board[row][col]) {
						board[row + 1][col - 1] = BoardCell.EMPTY;
						score = score + 1;
					}
				}
			}
			// East
			if (col + 1 < board[0].length) {
				if (board[row][col + 1] == board[row][col]) {
					board[row][col + 1] = BoardCell.EMPTY;
					score = score + 1;
				}
			}
			// West
			if (col > 0) {
				if (board[row][col - 1] == board[row][col]) {
					board[row][col - 1] = BoardCell.EMPTY;
					score = score + 1;
				}
			}
			board[row][col] = BoardCell.EMPTY;
			score = score + 1;
		}
		
		
		for (int index = board.length - 1; index >= 0; index--) {
			// A call to the helper method for finding a empty row if there is any
			if (checkEmptyRowExists(index) == true) {
				// This nested for-loop will remove the empty row and move the remaining non-empty rows up
				for (int i = index; i < board.length - 1; i++) {
					for (int j = 0; j < board[0].length; j++) {
						setBoardCell(i, j, getBoardCell(i + 1, j));
						setBoardCell(i + 1, j, BoardCell.EMPTY);
					}
				}
			}
		} 
	}
}

