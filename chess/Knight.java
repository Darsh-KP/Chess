package chess;

import chess.Chess.Player;

public class Knight extends ReturnPiece {
    // Creates a knight, which can be casted to returnPiece
    Knight(Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WN : PieceType.BN;
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

    // Checks if the move is valid for a knight
    private int isValidMove(ReturnPiece[][] currentBoard, int toFile, int toRank) {
        // Where the piece is currently
        int thisFile = Chess.getFile(pieceFile);
        int thisRank = Chess.getRank(pieceRank);

        // Check if the knight can move...
        if ((toFile == thisFile + 1) && (toRank == thisRank - 2)) { // 2 up, 1 right
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile - 1) && (toRank == thisRank - 2)) { // 2 up, 1 left
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile + 1) && (toRank == thisRank + 2)) { // 2 down, 1 right
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile - 1) && (toRank == thisRank + 2)) { // 2 down, 1 left
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile + 2) && (toRank == thisRank - 1)) { // 1 up, 2 right
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile - 2) && (toRank == thisRank - 1)) { // 1 up, 2 left
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;

        } else if ((toFile == thisFile + 2) && (toRank == thisRank + 1)) { // 1 down, 2 right
            // Empty square
            if (currentBoard[toFile][toRank] == null) return 1;

            // Capture the piece
            if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;
            
        } else if ((toFile == thisFile - 2) && (toRank == thisRank + 1)) { // 1 down, 2 left
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