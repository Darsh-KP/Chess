package chess;

import chess.Chess.Player;

public class Bishop extends ReturnPiece {
    // Creates a bishop, which can be casted to returnPiece
    Bishop(Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WB : PieceType.BB;
        pieceFile = file;
        pieceRank = rank;
    }

    public int move(String moveTo, ReturnPiece[][] currentBoard) {
        // Get move destination
        int fileDestination = Chess.getFile(moveTo.charAt(0));
        int rankDestination = Chess.getRank(Character.getNumericValue(moveTo.charAt(1)));
        
        // Check if the move is valid
        if (isValidMove(currentBoard, fileDestination, rankDestination) == -1) return -1;

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

    // Checks if the move is valid for a bishop
    private int isValidMove(ReturnPiece[][] currentBoard, int toFile, int toRank) {
        // Where the piece is currently
        int thisFile = Chess.getFile(pieceFile);
        int thisRank = Chess.getRank(pieceRank);

        // Check if the given square is a diagonal (subtraction)
        if (Math.abs(thisFile - toFile) != Math.abs(thisRank - toRank)) return -1;

        // Figure out which direction we are moving towards
        if ((toFile > thisFile) && (toRank < thisRank)) { // Going up, right
            // Check if square are empty up till the destination
            for (int i = 1; i < 8; i++) {
                // If we have reached the target square, stop
                if (((thisFile + i) == toFile) && ((thisRank - i) == toRank)) break;

                if (currentBoard[thisFile + i][thisRank - i] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile < thisFile) && (toRank < thisRank)) { // Going up, left
            // Check if square are empty up till the destination
            for (int i = 1; i < 8; i++) {
                // If we have reached the target square, stop
                if (((thisFile - i) == toFile) && ((thisRank - i) == toRank)) break;

                if (currentBoard[thisFile - i][thisRank - i] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile > thisFile) && (toRank > thisRank)) { // Going down, right
            // Check if square are empty up till the destination
            for (int i = 1; i < 8; i++) {
                // If we have reached the target square, stop
                if (((thisFile + i) == toFile) && ((thisRank + i) == toRank)) break;

                if (currentBoard[thisFile + i][thisRank + i] != null) return -1;
            }

            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile < thisFile) && (toRank > thisRank)) { // Going down, left
            // Check if square are empty up till the destination
            for (int i = 1; i < 8; i++) {
                // If we have reached the target square, stop
                if (((thisFile - i) == toFile) && ((thisRank + i) == toRank)) break;

                if (currentBoard[thisFile - i][thisRank + i] != null) return -1;
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