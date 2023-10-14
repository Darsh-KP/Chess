package chess;

import chess.Chess.Player;

public class Rook extends ReturnPiece {
    // Keeps track if the piece is allowed to castle
    boolean canCastle = true;

    // Creates a rook, which can be casted to returnPiece
    Rook(Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WR : PieceType.BR;
        pieceFile = file;
        pieceRank = rank;
    }

    public int move(String moveTo, ReturnPiece[][] currentBoard) {
        // Get move destination
        int fileDestination = Chess.getFile(moveTo.charAt(0));
        int rankDestination = Chess.getRank(Character.getNumericValue(moveTo.charAt(1)));
        
        // Check if the move is valid
        if (isValidMove(currentBoard, fileDestination, rankDestination) == -1) return -1;

        // Cannot caslte once the piece has moved
        if (canCastle == true) canCastle = false;

        // Update the board
        currentBoard[fileDestination][rankDestination] = currentBoard[Chess.getFile(pieceFile)][Chess.getRank(pieceRank)];
        currentBoard[Chess.getFile(pieceFile)][Chess.getRank(pieceRank)] = null;

        // Update piece position data
        pieceFile = Chess.getFile(fileDestination);
        pieceRank = Chess.getRank(rankDestination);

        return 1;
        // Return 1 for succesful move
        // -1 for illegal move
    }

    // Checks if the move is valid for a rook
    private int isValidMove(ReturnPiece[][] currentBoard, int toFile, int toRank) {
        // Where the piece is currently
        int thisFile = Chess.getFile(pieceFile);
        int thisRank = Chess.getRank(pieceRank);

        // Figure out which direction we are moving towards
        if ((toFile == thisFile) && (toRank < thisRank)) { // Going up
            // Check if square are empty up till the destination
            for (int r = thisRank - 1; r != toRank; r--) {
                if (currentBoard[toFile][r] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile) && (toRank > thisRank)) { // Going down
            // Check if square are empty up till the destination
            for (int r = thisRank + 1; r != toRank; r++) {
                if (currentBoard[toFile][r] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile > thisFile) && (toRank == thisRank)) { // Going right
            // Check if square are empty up till the destination
            for (int f = thisFile + 1; f != toFile; f++) {
                if (currentBoard[f][toRank] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile < thisFile) && (toRank == thisRank)) { // Going left
            // Check if square are empty up till the destination
            for (int f = thisFile - 1; f != toFile; f--) {
                if (currentBoard[f][toRank] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        }

        return -1;
        // Return 1 for succesful move
        // -1 for illegal move
    }
}