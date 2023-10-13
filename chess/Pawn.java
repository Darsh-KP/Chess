package chess;

import chess.Chess.Player;

public class Pawn extends ReturnPiece {
    // Keeps track of a piece that you can en-passent
    private static ReturnPiece enPassantTarget;

    // Creates a pawn, which can be casted to returnPiece
    Pawn(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WP : PieceType.BP;
        pieceFile = file;
        pieceRank = rank;
    }

    public int move(String moveTo, ReturnPiece[][] currentBoard) {
        // System.out.println("Moving Pawn to: " + moveTo);

        // Get move destination
        int fileDestination = Chess.getFile(moveTo.charAt(0));
        int rankDestination = Chess.getRank(Character.getNumericValue(moveTo.charAt(1)));

        // Check for valid moves and make the move
        switch (isValidMoveWhite(currentBoard, fileDestination, rankDestination)) {            
            case -1:
                return -1;
            case 2:
                if (enPassantTarget != this) enPassantTarget = this;
                break;
            case 4:
                // Capture if en-passant is possible
                currentBoard[fileDestination][Chess.getRank(pieceRank)] = null;
            default:
                enPassantTarget = null;
        }
        
        // Update the board
        currentBoard[fileDestination][rankDestination] = currentBoard[Chess.getFile(pieceFile)][Chess.getRank(pieceRank)];
        currentBoard[Chess.getFile(pieceFile)][Chess.getRank(pieceRank)] = null;

        // Update piece position data
        pieceFile = Chess.getFile(fileDestination);
        pieceRank = Chess.getRank(rankDestination);

        System.out.println("ETarget = " + enPassantTarget);
        
        

            
        
            // Cannont move a piece if there is a check
                // Can move if the move can block a check or capture the checking piece
                    // Cannont capture checking piece if double check, or it results in check
                    // (if check is still present after the move)
                // Cannot move into a check position
            
        // En Passent

        // Pawn Promotion

        // Find a way to return the original board if move was unable to be exceuted
            // Return -1 if move was unsuccesful (Illegal Move)
        return 1; // Return 1 for succesful move
    }

    // Returns an arraylist with all the moves that the piece can make
    private int isValidMoveWhite(ReturnPiece[][] currentBoard, int toFile, int toRank) {
        // Where the piece is currently
        int thisFile = Chess.getFile(pieceFile);
        int thisRank = Chess.getRank(pieceRank);

        // Check if the pawn can move forward
        if ((toFile == thisFile) && (toRank == thisRank - 1)) {
            if (currentBoard[thisFile][thisRank - 1] != null) return -1;
            return 1;
        }

        // Check if the pawn can take 2 steps from the starting position
        if ((toFile == thisFile) && (toRank == Chess.getRank(4))) {
            if (currentBoard[thisFile][thisRank - 1] != null) return -1;
            if (currentBoard[thisFile][thisRank - 2] != null) return -1;
            return 2;
        }

        // Check if the pawn can capture right
        if ((toFile == thisFile + 1) && (toRank == thisRank - 1)) {
            if (currentBoard[thisFile + 1][thisRank - 1] == null) {
                // Check if you en-passant is possible
                if ((thisRank == Chess.getRank(5)) && 
                    (currentBoard[thisFile + 1][thisRank].equals(enPassantTarget))) 
                    return 4;
                
                return -1;
            }

            // Check if the piece to capture is of opposite color
            switch (currentBoard[thisFile + 1][thisRank - 1].pieceType) {
                case BP: case BR: case BN: case BB: case BQ: case BK:
                    return 3;
                default:
                    return -1;
            }
        }
        
        // Check if the pawn can capture left
        if ((toFile == thisFile - 1) && (toRank == thisRank - 1)) {
            if (currentBoard[thisFile - 1][thisRank - 1] == null) {
                // Check if you en-passant is possible
                if ((thisRank == Chess.getRank(5)) && 
                    (currentBoard[thisFile - 1][thisRank].equals(enPassantTarget))) 
                    return 4;

                return -1;
            }
            
            // Check if the piece to capture is of opposite color
            switch (currentBoard[thisFile - 1][thisRank - 1].pieceType) {
                case BP: case BR: case BN: case BB: case BQ: case BK:
                    return 3;
                default:
                    return -1;
            }
        }

        return -1;
        // Return -1 means not a valid move
        // 1 means moved 1 step, 2 means moved 2 steps
        // 3 means normal capture
        // 4 means en passant
    }
}
