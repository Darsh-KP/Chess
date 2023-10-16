package chess;

import chess.ReturnPiece.PieceType;
import chess.Chess.Player;

public class Piece {
    // Saves the current board, and the static variables if needed to revert
    private static ReturnPiece[][] savedBoard;
    private static ReturnPiece enPassantTargetCopy, whiteKingCopy, blackKingCopy;
    

    private Piece() {}

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

        for (int r = checkRank + 1; r <= 7; r++) { // Down
            if (currentBoard[checkFile][r] != null && !((ignoreOwnKing) && (currentBoard[checkFile][r].pieceType == ownKing))) {
                if ((currentBoard[checkFile][r].pieceType == oppRook) || (currentBoard[checkFile][r].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int f = checkFile + 1; f <= 7; f++) { // Right
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
        for (int i = 1; ((checkFile + i) <= 7) && ((checkRank - i) >= 0); i++) { // Up, right
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

        for (int i = 1; ((checkFile + i) <= 7) && ((checkRank + i) <= 7); i++) { // Down, right
            if (currentBoard[checkFile + i][checkRank + i] != null && !((ignoreOwnKing) && (currentBoard[checkFile + i][checkRank + i].pieceType == ownKing))) {
                if ((currentBoard[checkFile + i][checkRank + i].pieceType == oppBishop) || (currentBoard[checkFile + i][checkRank + i].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        for (int i = 1; ((checkFile - i) >= 0) && ((checkRank + i) <= 7); i++) { // Down, left
            if (currentBoard[checkFile - i][checkRank + i] != null && !((ignoreOwnKing) && (currentBoard[checkFile - i][checkRank + i].pieceType == ownKing))) {
                if ((currentBoard[checkFile - i][checkRank + i].pieceType == oppBishop) || (currentBoard[checkFile - i][checkRank + i].pieceType == oppQueen)) {
                    totalChecks++;
                }
                break;
            }
        }

        // If opposite color knight in the 8 knight moves squares, then check
        if (((checkFile + 1) <= 7) && ((checkRank - 2) >= 0) && 
            (currentBoard[checkFile + 1][checkRank - 2] != null ) && 
            (currentBoard[checkFile + 1][checkRank - 2].pieceType == oppKnight)) 
                totalChecks++; // 2 up, 1 right
        if (((checkFile - 1) >= 0) && ((checkRank - 2) >= 0) && 
            (currentBoard[checkFile - 1][checkRank - 2] != null ) && 
            (currentBoard[checkFile - 1][checkRank - 2].pieceType == oppKnight)) 
                totalChecks++; // 2 up, 1 left
        if (((checkFile + 1) <= 7) && ((checkRank + 2) <= 7) && 
            (currentBoard[checkFile + 1][checkRank + 2] != null ) && 
            (currentBoard[checkFile + 1][checkRank + 2].pieceType == oppKnight)) 
                totalChecks++; // 2 down, 1 right
        if (((checkFile - 1) >= 0) && ((checkRank + 2) <= 7) && 
            (currentBoard[checkFile - 1][checkRank + 2] != null ) && 
            (currentBoard[checkFile - 1][checkRank + 2].pieceType == oppKnight)) 
                totalChecks++; // 2 down, 1 left
        if (((checkFile + 2) <= 7) && ((checkRank - 1) >= 0) && 
            (currentBoard[checkFile + 2][checkRank - 1] != null ) && 
            (currentBoard[checkFile + 2][checkRank - 1].pieceType == oppKnight)) 
                totalChecks++; // 1 up, 2 right
        if (((checkFile - 2) >= 0) && ((checkRank - 1) >= 0) && 
            (currentBoard[checkFile - 2][checkRank - 1] != null ) && 
            (currentBoard[checkFile - 2][checkRank - 1].pieceType == oppKnight)) 
                totalChecks++; // 1 up, 2 left
        if (((checkFile + 2) <= 7) && ((checkRank + 1) <= 7) && 
            (currentBoard[checkFile + 2][checkRank + 1] != null ) && 
            (currentBoard[checkFile + 2][checkRank + 1].pieceType == oppKnight)) 
                totalChecks++; // 1 down, 2 right
        if (((checkFile - 2) >= 0) && ((checkRank + 1) <= 7) && 
            (currentBoard[checkFile - 2][checkRank + 1] != null ) && 
            (currentBoard[checkFile - 2][checkRank + 1].pieceType == oppKnight)) 
                totalChecks++; // 1 down, 2 left


        // Opposite color pawn capture position (different direction based on color), then check
        if (oppPawn == PieceType.BP) { // Opposite pawn is black
            if (((checkFile + 1) <= 7) && ((checkRank - 1) >= 0) && 
                (currentBoard[checkFile + 1][checkRank - 1] != null ) && 
                (currentBoard[checkFile + 1][checkRank - 1].pieceType == oppPawn)) 
                    totalChecks++; // 1 up, 1 right
            if (((checkFile - 1) >= 0) && ((checkRank - 1) >= 0) && 
                (currentBoard[checkFile - 1][checkRank - 1] != null ) && 
                (currentBoard[checkFile - 1][checkRank - 1].pieceType == oppPawn)) 
                    totalChecks++; // 1 up, 1 left
        } else {
            if (((checkFile + 1) <= 7) && ((checkRank + 1) <= 7) && 
                (currentBoard[checkFile + 1][checkRank + 1] != null ) && 
                (currentBoard[checkFile + 1][checkRank + 1].pieceType == oppPawn)) 
                    totalChecks++; // 1 down, 1 right
            if (((checkFile - 1) >= 0) && ((checkRank + 1) <= 7) && 
                (currentBoard[checkFile - 1][checkRank + 1] != null ) && 
                (currentBoard[checkFile - 1][checkRank + 1].pieceType == oppPawn)) 
                    totalChecks++; // 1 down, 1 left
        }
        
        // Opposite color king in the 8 squares around
        for (int r = -1; r < 2; r++) { // Check top, mid, bot
            boolean calledBreak = false;
            if (!((checkRank + r) >= 0) || !((checkRank + r) <= 7)) continue;
            for (int f = -1; f < 2; f++) { // Check left, mid, right
                if ((r == 0) && (f == 0)) continue; // Skip the given square
                if (!((checkFile + f) >= 0) || !((checkFile + f) <= 7)) continue;
                if (currentBoard[checkFile + f][checkRank + r] == null) continue;

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




    // For every piece
        // Do not update the board, if a move results in check???
        // BIG PROBLEM!!!

    // Deep copies a piece and returns it
    private static ReturnPiece copyPiece (ReturnPiece originalPiece) {
        Rook rookCopy;
        King kingCopy;

        // Find the type of piece to copy
        switch (originalPiece.pieceType) {
            // Copies white pieces
            case WP:
                return new Pawn(Player.white, originalPiece.pieceFile, originalPiece.pieceRank);
            case WR:
                rookCopy = new Rook(Player.white, originalPiece.pieceFile, originalPiece.pieceRank);
                rookCopy.canCastle = ((Rook) originalPiece).canCastle;
                return rookCopy;
            case WN:
                return new Knight(Player.white, originalPiece.pieceFile, originalPiece.pieceRank);
            case WB:
                return new Bishop(Player.white, originalPiece.pieceFile, originalPiece.pieceRank);
            case WQ:
                return new Queen(Player.white, originalPiece.pieceFile, originalPiece.pieceRank);
            case WK:
                kingCopy = new King(Player.white, originalPiece.pieceFile, originalPiece.pieceRank);
                kingCopy.canCastle = ((King) originalPiece).canCastle;
                return kingCopy;

            // Copies black pieces
            case BP:
                return new Pawn(Player.black, originalPiece.pieceFile, originalPiece.pieceRank);
            case BR:
                rookCopy = new Rook(Player.black, originalPiece.pieceFile, originalPiece.pieceRank);
                rookCopy.canCastle = ((Rook) originalPiece).canCastle;
                return rookCopy;
            case BN:
                return new Knight(Player.black, originalPiece.pieceFile, originalPiece.pieceRank);
            case BB:
                return new Bishop(Player.black, originalPiece.pieceFile, originalPiece.pieceRank);
            case BQ:
                return new Queen(Player.black, originalPiece.pieceFile, originalPiece.pieceRank);
            case BK:
                kingCopy = new King(Player.black, originalPiece.pieceFile, originalPiece.pieceRank);
                kingCopy.canCastle = ((King) originalPiece).canCastle;
                return kingCopy;
        }

        // Code shouldn't get this far ever!!!
        return new ReturnPiece();
    }

    // Copies the 2d array of the board, and the state of the game
    public static void saveBoard(ReturnPiece[][] currentBoard) {
        savedBoard = new ReturnPiece[8][8];
        
        // Copy all the squares that are not empty
        for (int f = 0; f < 8; f++) {
            for (int r = 0; r < 8; r++) {
                if (currentBoard[f][r] == null) continue;

                savedBoard[f][r] = copyPiece(currentBoard[f][r]);
            }
        }

        // Save en-passant target, and the king pointers
        // Must point to pieces on the copied board, not the original board
        int targetFile, targetRank;
        
        // En-Passant target
        if (Pawn.enPassantTarget != null) {
            targetFile = Chess.getFile(Pawn.enPassantTarget.pieceFile);
            targetRank = Chess.getRank(Pawn.enPassantTarget.pieceRank);
            enPassantTargetCopy = savedBoard[targetFile][targetRank];
        } else enPassantTargetCopy = null;

        // White king pos
        if (King.whiteKing != null) {
            targetFile = Chess.getFile(King.whiteKing.pieceFile);
            targetRank = Chess.getRank(King.whiteKing.pieceRank);
            whiteKingCopy = savedBoard[targetFile][targetRank];
        } else whiteKingCopy = null;
        
        // Black king pos
        if (King.blackKing != null) {
            targetFile = Chess.getFile(King.blackKing.pieceFile);
            targetRank = Chess.getRank(King.blackKing.pieceRank);
            blackKingCopy = savedBoard[targetFile][targetRank];
        } else blackKingCopy = null;
    }

    // Reverts the state of the game to the saved version
    public static void revertBoard(ReturnPiece[][] currentBoard) {
        // Eampties and copies all the squares that are not empty
        for (int f = 0; f < 8; f++) {
            for (int r = 0; r < 8; r++) { 
                // Empties the square 
                currentBoard[f][r] = null;
                
                if (savedBoard[f][r] == null) continue;

                currentBoard[f][r] = copyPiece(savedBoard[f][r]);
            }
        }

        // Load en-passant target, and the king pointers
        // Must point to pieces on the copied board, not the saved board
        int targetFile, targetRank;
        
        // En-Passant target
        if (enPassantTargetCopy != null) {
            targetFile = Chess.getFile(enPassantTargetCopy.pieceFile);
            targetRank = Chess.getRank(enPassantTargetCopy.pieceRank);
            Pawn.enPassantTarget = currentBoard[targetFile][targetRank];
        } else Pawn.enPassantTarget = null;
        

        // White king pos
        if (whiteKingCopy != null) {
            targetFile = Chess.getFile(whiteKingCopy.pieceFile);
            targetRank = Chess.getRank(whiteKingCopy.pieceRank);
            King.whiteKing = currentBoard[targetFile][targetRank];
        } else King.whiteKing = null;
        
        
        // Black king pos
        if (blackKingCopy != null) {
            targetFile = Chess.getFile(blackKingCopy.pieceFile);
            targetRank = Chess.getRank(blackKingCopy.pieceRank);
            King.blackKing = currentBoard[targetFile][targetRank];
        } else King.blackKing = null;
    }
}
