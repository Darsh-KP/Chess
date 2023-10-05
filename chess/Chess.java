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
	enum Player { white, black }
	
	// chessBoard is stoing the pieces and the status of the game
	private static ReturnPlay currentStatus = new ReturnPlay();
	
	public static ReturnPlay play(String move) {

		return currentStatus; // Needs to return a ReturnPlay Object
	}
	
	// Starts a new game of chess, puting all the pieces in their starting positions
	public static void start() {
		currentStatus.piecesOnBoard = new ArrayList<ReturnPiece>();
		ArrayList<ReturnPiece> currentBoard = currentStatus.piecesOnBoard;
		
		ReturnPiece thisPawn = new Pawn(Player.white, PieceFile.a, 2);
		currentBoard.add(thisPawn);
	}
}