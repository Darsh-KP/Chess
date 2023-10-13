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
}
