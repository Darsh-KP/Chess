package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;

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

	public static ReturnPlay play(String move) {

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
}