package chess;

import chess.ReturnPiece.PieceType;

public class Piece {
    private void Piece() {}

    // Checks if a piece can capture another piece, opposite colors mean you can capture
    public static boolean canCapture(PieceType selectedPieceType, PieceType targetPieceType) {
        switch (selectedPieceType) {
            // Checks for white piece
            case WP: case WR: case WN: case WB: case WQ: case WK:
                // Cannot capture the same colored piece
                switch (targetPieceType) {
                    case WP: case WR: case WN: case WB: case WQ: case WK:
                        return false;
                    default:
                        return true;
                }
            // Checks for black piece
            case BP: case BR: case BN: case BB: case BQ: case BK:
                // Cannot capture the same colored piece
                switch (targetPieceType) {
                    case BP: case BR: case BN: case BB: case BQ: case BK:
                        return false;
                    default:
                        return true;
                }
        }

        return false;
    }

    // Checking for a check on a square
        // If opp color rook or queen in a plus, then check
        // If opp color bishop or queen in diagonal, then check
        // If opp color knight in the 8 knight moves squares, then check
        // Opp color pawn capture position (with direction), then check

        // Ignore the same color king, that you are checking for (argument only)
        // Opp color king in the 8 squares around
    
    // Checking for checkmate
        // Move the king and see if you can escape check
        // If the attacking piece can be captured
            // If the pos of this piece is in check by own color
            // Doesn't work if double check though!!!!
        // If a move can block the check
            // If one of the square in the checking path is in check by own color
            // Doesn't work if double check though!!!!

    // For more than 1 check, have a counter?


    // Add the check for legal move logic everywhere
        // Check for what is missing
        // Do a lot of testing
            // Check for all moves
            // Check for all captures
            // Edge cases
            // As much as possible
}
