package chess;

import chess.Chess.Player;

public class Pawn extends ReturnPiece {
    // Creates a pawn and which can be casted to returnPiece
    Pawn(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WP : PieceType.BP;
        pieceFile = file;
        pieceRank = rank;
    }

    public void move() {
        // Check for valid moves
            // Cannont move through pieces
            // Can capture a piece at the end of path if the piece is of opposite color
            // Cannont move a piece if there is a check
                // Can move if the move can block a check or capture the checking piece
                    // Cannont capture checking piece if double check, or it results in check
                // Cannot move into a check

        // Pawn Promotion

        // Castleing
            // Cannot castle if check going thourgh
            // Cannot castle if piece has moved
            // Only allowed if nothing in the way

        // En Passent

        // Find a way to return the original board if move was unable to be exceuted
            // Return -1 if move was unsuccesful (Illegal Move)
    }

    public String[] validMoves() {
        return new String[1];
    }
}
