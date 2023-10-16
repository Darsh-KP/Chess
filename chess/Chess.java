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

		// Check if there are 2 positions as input
		if (move.length() < 5) {
			currentStatus.message = Message.ILLEGAL_MOVE;
			return currentStatus;
		}

		// Check for Resign
		if ((move.length() > 5) && (move.substring(0, 6).equals("resign"))) {
			// Return the same board with specific message
			currentStatus.message = (currentPlayer == Player.white) ? Message.RESIGN_BLACK_WINS : Message.RESIGN_WHITE_WINS;
			return currentStatus;
		}

		// Illegal move if no piece is selected
		if (currentBoard[getFile(move.charAt(0))][getRank(Character.getNumericValue(move.charAt(1)))] == null) {
			currentStatus.message = Message.ILLEGAL_MOVE;
			return currentStatus;
		}

		// If selected piece pos, and target pos is the same, then illegal move
		if ((move.charAt(0) == move.charAt(3)) && (move.charAt(1) == move.charAt(4))) {
			currentStatus.message = Message.ILLEGAL_MOVE;
			return currentStatus;
		}

		// Save the board in case we need to revert for an illegal move
		Piece.saveBoard(currentBoard);

		// Select the piece
		ReturnPiece selectedPiece = currentBoard[getFile(move.charAt(0))][getRank(Character.getNumericValue(move.charAt(1)))];

		// Check if the selected piece is of current player, if so, make a move
		int moveStatus = -1;
		String moveToString = move.substring(3);

		if (currentPlayer == Player.white) {
			switch (selectedPiece.pieceType) {
				case WP:
					moveStatus = (((Pawn)selectedPiece).move(moveToString, currentBoard));
					break;
				case WR:
					moveStatus = (((Rook)selectedPiece).move(moveToString, currentBoard));
					break;
				case WN:
					moveStatus = (((Knight)selectedPiece).move(moveToString, currentBoard));
					break;
				case WB:
					moveStatus = (((Bishop)selectedPiece).move(moveToString, currentBoard));
					break;
				case WQ:
					moveStatus = (((Queen)selectedPiece).move(moveToString, currentBoard));
					break;
				case WK:
					moveStatus = (((King)selectedPiece).move(moveToString, currentBoard));
					break;
				default:
					moveStatus = -1;
			}
		} else if (currentPlayer == Player.black) {
			switch (selectedPiece.pieceType) {
				case BP:
					moveStatus = (((Pawn)selectedPiece).move(moveToString, currentBoard));
					break;
				case BR:
					moveStatus = (((Rook)selectedPiece).move(moveToString, currentBoard));
					break;
				case BN:
					moveStatus = (((Knight)selectedPiece).move(moveToString, currentBoard));
					break;
				case BB:
					moveStatus = (((Bishop)selectedPiece).move(moveToString, currentBoard));
					break;
				case BQ:
					moveStatus = (((Queen)selectedPiece).move(moveToString, currentBoard));
					break;
				case BK:
					moveStatus = (((King)selectedPiece).move(moveToString, currentBoard));
					break;
				default:
					moveStatus = -1;
			}
		}
		
		// Check if the move was legal
		if (moveStatus == -1) {
			currentStatus.message = Message.ILLEGAL_MOVE;
			return currentStatus;
		}

		// If after making the move, current player is in check, revert the move, and call illegal move
		ReturnPiece ownKing = (currentPlayer == Player.white) ? King.whiteKing : King.blackKing;
		int ownKingFile = getFile(ownKing.pieceFile);
		int ownKingRank = getRank(ownKing.pieceRank);
		if (Piece.isChecked(currentBoard, currentPlayer, ownKingFile, ownKingRank, false) > 0) {
			Piece.revertBoard(currentBoard);

			// Adds all the pieces from array to arraylist
			currentStatus.piecesOnBoard.clear();
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (currentBoard[i][j] != null)
						currentStatus.piecesOnBoard.add(currentBoard[i][j]);
				}
			}

			currentStatus.message = Message.ILLEGAL_MOVE;
			return currentStatus;
		}

		// Set's en-passant target
		switch (selectedPiece.pieceType) {
			case WP: case BP:
				// If it was a pawns initial 2 step move, set en-passant target
				if (moveStatus == 2) {
					Pawn.enPassantTarget = selectedPiece;
					break;
				}
			default:
				// Remove en-passant taget for any other move
				Pawn.enPassantTarget = null;
		}

		// If move is sucessful, change the current player to another player
		currentPlayer = (currentPlayer == Player.white) ? Player.black : Player.white;

		// Check for check
		ownKing = (currentPlayer == Player.white) ? King.whiteKing : King.blackKing;
		ownKingFile = getFile(ownKing.pieceFile);
		ownKingRank = getRank(ownKing.pieceRank);
		if (Piece.isChecked(currentBoard, currentPlayer, ownKingFile, ownKingRank, false) > 0) {
			currentStatus.message = Message.CHECK;

			// Check for checkmate
			if (Piece.isCheckmate(currentBoard, currentPlayer))
				currentStatus.message = (currentPlayer == Player.white) ? Message.CHECKMATE_BLACK_WINS: Message.CHECKMATE_WHITE_WINS;
		}

		// Adds all the pieces from array to arraylist
		currentStatus.piecesOnBoard.clear();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (currentBoard[i][j] != null)
					currentStatus.piecesOnBoard.add(currentBoard[i][j]);
			}
		}
		

		// Draw if draw was called
		if (move.substring(move.length() - 5).equals("draw?")) {
			// Return the same board with specific message
			currentStatus.message = Message.DRAW;
			return currentStatus;
		}
		

		// DEBUG
		// DEBUG
		// DEBUG
		// DEBUG
		// DEBUG
		/*
		// Prints 2d array
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (currentBoard[file][rank] != null)
					System.out.print(currentBoard[file][rank] + " ");
			}
			System.out.println();
		}
		*/

		return currentStatus; // Needs to return a ReturnPlay Object
	}



	// Starts a new game of chess, puting all the pieces in their starting positions
	public static void start() {
		// Creates new return play object to reset game status
		currentStatus = new ReturnPlay();
		currentStatus.piecesOnBoard = new ArrayList<ReturnPiece>();

		// Creates new board
		currentBoard = new ReturnPiece[8][8];

		// Adds all the white pieces
		for (int col = 0; col < 8; col++) currentBoard[col][getRank(2)] = new Pawn(Player.white, getFile(col), 2); // Pawns
		currentBoard[getFile('a')][getRank(1)] = new Rook(Player.white, PieceFile.a, 1); // Rook Left
		currentBoard[getFile('b')][getRank(1)] = new Knight(Player.white, PieceFile.b, 1); // Knight Left
		currentBoard[getFile('c')][getRank(1)] = new Bishop(Player.white, PieceFile.c, 1); // Bishop Left
		currentBoard[getFile('d')][getRank(1)] = new Queen(Player.white, PieceFile.d, 1); // Queen
		currentBoard[getFile('e')][getRank(1)] = new King(Player.white, PieceFile.e, 1); // King
		currentBoard[getFile('f')][getRank(1)] = new Bishop(Player.white, PieceFile.f, 1); // Bishop Right
		currentBoard[getFile('g')][getRank(1)] = new Knight(Player.white, PieceFile.g, 1); // Knight Right
		currentBoard[getFile('h')][getRank(1)] = new Rook(Player.white, PieceFile.h, 1); //  Rook Right

		// Adds all the black pieces
		for (int col = 0; col < 8; col++) currentBoard[col][getRank(7)] = new Pawn(Player.black, getFile(col), 7); // Pawns
		currentBoard[getFile('a')][getRank(8)] = new Rook(Player.black, PieceFile.a, 8); // Rook Left
		currentBoard[getFile('b')][getRank(8)] = new Knight(Player.black, PieceFile.b, 8); // Knight Left
		currentBoard[getFile('c')][getRank(8)] = new Bishop(Player.black, PieceFile.c, 8); // Bishop Left
		currentBoard[getFile('d')][getRank(8)] = new Queen(Player.black, PieceFile.d, 8); // Queen
		currentBoard[getFile('e')][getRank(8)] = new King(Player.black, PieceFile.e, 8); // King
		currentBoard[getFile('f')][getRank(8)] = new Bishop(Player.black, PieceFile.f, 8); // Bishop Right
		currentBoard[getFile('g')][getRank(8)] = new Knight(Player.black, PieceFile.g, 8); // Knight Right
		currentBoard[getFile('h')][getRank(8)] = new Rook(Player.black, PieceFile.h, 8); //  Rook Right

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
		return 8 - rank;
	}

	// Converts a int for file to a PieceFile enum
	public static PieceFile getFile(int column) {
		switch (column) {
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

	// Converts from char of a file to number of a file
	public static int getFile(char file) {
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

	// Converts from pieceFile to int
	public static int getFile(PieceFile file) {
		switch (file) {
            case a: return 0;
            case b: return 1;
            case c: return 2;
            case d: return 3;
            case e: return 4;
            case f: return 5;
            case g: return 6;
            case h: return 7;
        }
		return 0;
	}
}