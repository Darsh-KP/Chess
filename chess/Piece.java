package chess;

import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

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
    /*
    ignoreOwnKing asks if the provided player's king should be ignore while checking for check on a square, count same color king as null
    checkFile and checkRank must the coords of the 2D array, not actual file and rank
    returns the number of check, helps when there is a double check where the king is forced to move
    */
    public static int isChecked(ReturnPiece[][] currentBoard, Player ownColor, int checkFile, int checkRank, boolean ignoreOwnKing) {
        // Tracks the number of pieces attacking a given position
        int totalChecks = 0;

        // Piece type of opposite color
        PieceType oppPawn, oppRook, oppKnight, oppBishop, oppQueen, oppKing, ownKing;
        if (ownColor == Player.white) {
            oppPawn = PieceType.BP;
            oppRook = PieceType.BR;
            oppKnight = PieceType.BN;
            oppBishop = PieceType.BB;
            oppQueen = PieceType.BQ;
            oppKing = PieceType.BK;

            ownKing = PieceType.WK;
        } else {
            oppPawn = PieceType.WP;
            oppRook = PieceType.WR;
            oppKnight = PieceType.WN;
            oppBishop = PieceType.WB;
            oppQueen = PieceType.WQ;
            oppKing = PieceType.WK;

            ownKing = PieceType.BK;
        }

        // If opposite color rook or queen in a plus, then check
        for (int r = checkRank - 1; r >= 0; r--) { // Up
            if (currentBoard[checkFile][r] != null && !((ignoreOwnKing) && (currentBoard[checkFile][r].pieceType == ownKing))) {
                if ((currentBoard[checkFile][r].pieceType == oppRook) || (currentBoard[checkFile][r].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int r = checkRank + 1; r <= 8; r++) { // Down
            if (currentBoard[checkFile][r] != null && !((ignoreOwnKing) && (currentBoard[checkFile][r].pieceType == ownKing))) {
                if ((currentBoard[checkFile][r].pieceType == oppRook) || (currentBoard[checkFile][r].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int f = checkFile + 1; f <= 8; f++) { // Right
            if (currentBoard[f][checkRank] != null && !((ignoreOwnKing) && (currentBoard[f][checkRank].pieceType == ownKing))) {
                if ((currentBoard[f][checkRank].pieceType == oppRook) || (currentBoard[f][checkRank].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int f = checkFile - 1; f >= 0; f--) { // Left
            if (currentBoard[f][checkRank] != null && !((ignoreOwnKing) && (currentBoard[f][checkRank].pieceType == ownKing))) {
                if ((currentBoard[f][checkRank].pieceType == oppRook) || (currentBoard[f][checkRank].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        // If opposite color bishop or queen in diagonal, then check
        for (int i = 1; ((checkFile + i) <= 8) && ((checkRank - i) >= 0); i++) { // Up, right
            if (currentBoard[checkFile + i][checkRank - i] != null && !((ignoreOwnKing) && (currentBoard[checkFile + i][checkRank - i].pieceType == ownKing))) {
                if ((currentBoard[checkFile + i][checkRank - i].pieceType == oppBishop) || (currentBoard[checkFile + i][checkRank - i].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int i = 1; ((checkFile - i) >= 0) && ((checkRank - i) >= 0); i++) { // Up, left
            if (currentBoard[checkFile - i][checkRank - i] != null && !((ignoreOwnKing) && (currentBoard[checkFile - i][checkRank - i].pieceType == ownKing))) {
                if ((currentBoard[checkFile - i][checkRank - i].pieceType == oppBishop) || (currentBoard[checkFile - i][checkRank - i].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int i = 1; ((checkFile + i) <= 8) && ((checkRank + i) <= 8); i++) { // Down, right
            if (currentBoard[checkFile + i][checkRank + i] != null && !((ignoreOwnKing) && (currentBoard[checkFile + i][checkRank + i].pieceType == ownKing))) {
                if ((currentBoard[checkFile + i][checkRank + i].pieceType == oppBishop) || (currentBoard[checkFile + i][checkRank + i].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int i = 1; ((checkFile - i) >= 0) && ((checkRank + i) <= 8); i++) { // Down, left
            if (currentBoard[checkFile - i][checkRank + i] != null && !((ignoreOwnKing) && (currentBoard[checkFile - i][checkRank + i].pieceType == ownKing))) {
                if ((currentBoard[checkFile - i][checkRank + i].pieceType == oppBishop) || (currentBoard[checkFile - i][checkRank + i].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        // If opposite color knight in the 8 knight moves squares, then check
        if (((checkFile + 1) <= 8) && ((checkRank - 2) >= 0) && (currentBoard[checkFile + 1][checkRank - 2].pieceType == oppKnight)) totalChecks++; // 2 up, 1 right
        if (((checkFile - 1) >= 0) && ((checkRank - 2) >= 0) && (currentBoard[checkFile - 1][checkRank - 2].pieceType == oppKnight)) totalChecks++; // 2 up, 1 left
        if (((checkFile + 1) <= 8) && ((checkRank + 2) <= 8) && (currentBoard[checkFile + 1][checkRank + 2].pieceType == oppKnight)) totalChecks++; // 2 down, 1 right
        if (((checkFile - 1) >= 0) && ((checkRank + 2) <= 8) && (currentBoard[checkFile - 1][checkRank + 2].pieceType == oppKnight)) totalChecks++; // 2 down, 1 left
        if (((checkFile + 2) <= 8) && ((checkRank - 1) >= 0) && (currentBoard[checkFile + 2][checkRank - 1].pieceType == oppKnight)) totalChecks++; // 1 up, 2 right
        if (((checkFile - 2) >= 0) && ((checkRank - 1) >= 0) && (currentBoard[checkFile - 2][checkRank - 1].pieceType == oppKnight)) totalChecks++; // 1 up, 2 left
        if (((checkFile + 2) <= 8) && ((checkRank + 1) <= 8) && (currentBoard[checkFile + 2][checkRank + 1].pieceType == oppKnight)) totalChecks++; // 1 down, 2 right
        if (((checkFile - 2) >= 0) && ((checkRank + 1) <= 8) && (currentBoard[checkFile - 2][checkRank + 1].pieceType == oppKnight)) totalChecks++; // 1 down, 2 left


        // Opposite color pawn capture position (different direction based on color), then check
        if (oppPawn == PieceType.BP) { // Opposite pawn is black
            if (((checkFile + 1) <= 8) && ((checkRank - 1) >= 0) && (currentBoard[checkFile + 1][checkRank - 1].pieceType == oppPawn)) totalChecks++; // 1 up, 1 right
            if (((checkFile - 1) >= 0) && ((checkRank - 1) >= 0) && (currentBoard[checkFile - 1][checkRank - 1].pieceType == oppPawn)) totalChecks++; // 1 up, 1 left
        } else {
            if (((checkFile + 1) <= 8) && ((checkRank + 1) <= 8) && (currentBoard[checkFile + 1][checkRank + 1].pieceType == oppPawn)) totalChecks++; // 1 down, 1 right
            if (((checkFile - 1) >= 0) && ((checkRank + 1) <= 8) && (currentBoard[checkFile - 1][checkRank + 1].pieceType == oppPawn)) totalChecks++; // 1 down, 1 left
        }
        
        // Opposite color king in the 8 squares around
        for (int r = -1; r < 2; r++) { // Check top, mid, bot
            boolean calledBreak = false;
            if (!((checkRank + r) >= 0) || !((checkRank + r) <= 8)) continue;
            for (int f = -1; f < 2; f++) { // Check left, mid, right
                if ((r == 0) && (f == 0)) continue; // Skip the given square
                if (!((checkFile + f) >= 0) || !((checkFile + f) <= 8)) continue;

                if (currentBoard[checkFile + f][checkRank + r].pieceType == oppKing) {
                    totalChecks++;
                    calledBreak = true;
                    break;
                }
            }

            if (calledBreak) break;
        }

        return totalChecks;
    }
    


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
