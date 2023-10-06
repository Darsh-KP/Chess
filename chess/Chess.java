package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPlay.Message;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {
	// Playe enum to select between current player
	enum Player {
		white, black
	}



	// Stores the pieces and the status of the game
	private static ReturnPlay currentStatus;
	private static ReturnPiece[][] currentBoard;
	private static Player currentPlayer;
	
	public static ReturnPlay play(String move) {
		// Remove leading and trailing spaces
		move = move.trim();

		// Reset message everytime
		currentStatus.message = null;

		// Check for Resign
		if ((move.length() > 5) && (move.substring(0, 6).equals("resign"))) {
			// Return the same board with specific message
			currentStatus.message = (currentPlayer == Player.white) ? Message.RESIGN_BLACK_WINS : Message.RESIGN_WHITE_WINS;
			System.out.println("Resigned");
			return currentStatus;
		}

		// Move the piece
			// If succesful, do nothing
			// Otherwise illegal move

		// Check for check
			// Check for checkmate

		// Draw if draw was called

		// (Optional) Other draws

		// If move is sucessful, change the current player to another player
		currentPlayer = (currentPlayer == Player.white) ? Player.black : Player.white;

		// Adds all the pieces from array to arraylist
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (currentBoard[i][j] != null)
					currentStatus.piecesOnBoard.add(currentBoard[i][j]);
			}
		}

		return currentStatus; // Needs to return a ReturnPlay Object
	}



	// Starts a new game of chess, puting all the pieces in their starting positions
	public static void start() {
		// Creates new return play object to reset game status
		currentStatus = new ReturnPlay();
		currentStatus.piecesOnBoard = new ArrayList<ReturnPiece>();

		// Creates new board
		currentBoard = new ReturnPiece[8][8];

		// Adds all the white pawns
		for (int file = 0; file < 8; file++) currentBoard[getRank(2)][file] = new Pawn(Player.white, getFile(file), 2);

		// Adds all the black pawns
		for (int file = 0; file < 8; file++) currentBoard[getRank(7)][file] = new Pawn(Player.black, getFile(file), 7);

		// Adds all the pieces from array to arraylist
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (currentBoard[i][j] != null)
					currentStatus.piecesOnBoard.add(currentBoard[i][j]);
			}
		}

		// Set's the current player to white everytime a new game starts
		currentPlayer = Player.white;
	}


	
	// Helps gets the rank in the array, as the rank in the array starts from the top
	public static int getRank(int rank) {
		return 7 - rank;
	}

	// Converts a int for file to a PieceFile enum
	public static PieceFile getFile(int file) {
		switch (file) {
            case 0: return PieceFile.a;
            case 1: return PieceFile.b;
            case 2: return PieceFile.c;
            case 3: return PieceFile.d;
            case 4: return PieceFile.e;
            case 5: return PieceFile.f;
            case 6: return PieceFile.g;
            case 7: return PieceFile.h;
        }
		return PieceFile.a;
	}

	public static int getIntFile(char file) {
		switch (file) {
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'g': return 6;
            case 'h': return 7;
        }
		return 0;
	}
}