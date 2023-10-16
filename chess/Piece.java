package chess;

import chess.ReturnPiece.PieceFile;
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

    // Returns true if the player is checkmated
    public static boolean isCheckmate (ReturnPiece[][] currentBoard, Player ownColor) {
        // Own king's position
        ReturnPiece ownKing = (ownColor == Player.white) ? King.whiteKing : King.blackKing;
        int ownKingFile = Chess.getFile(ownKing.pieceFile);
        int ownKingRank = Chess.getRank(ownKing.pieceRank);

        // Is it possible for the king to move and escape check?
        for (int r = -1; r < 2; r++) { // Check top, mid, bot
            if (!((ownKingRank + r) >= 0) || !((ownKingRank + r) <= 7)) continue;
            for (int f = -1; f < 2; f++) { // Check left, mid, right
                if ((r == 0) && (f == 0)) continue; // Skip the given square
                if (!((ownKingFile + f) >= 0) || !((ownKingFile + f) <= 7)) continue;

                // If square is not empty and if the piece can't be captured safely by the king, then move on
                if ((currentBoard[ownKingFile + f][ownKingRank + r] != null) && 
                    (!canCapture(ownKing.pieceType, currentBoard[ownKingFile + f][ownKingRank + r].pieceType))) continue;
               
                if (isChecked(currentBoard, ownColor, ownKingFile + f, ownKingRank + r, true) > 0) continue;
                return false;
            }
        }
        
        // No king moves left
        // If double check then, checkmate
        if (isChecked(currentBoard, ownColor, ownKingFile, ownKingRank, false) > 1) return true;

        // Find the checking piece
        ReturnPiece checkingPiece = getCheckingPiece(currentBoard, ownColor, ownKingFile, ownKingRank, false);

        // If checking piece can be captured, safely, then no checkmate
        Player oppositeColor = (ownColor == Player.white) ? Player.black : Player.white;
        int checkingPieceFile = Chess.getFile(checkingPiece.pieceFile);
        int checkingPieceRank = Chess.getRank(checkingPiece.pieceRank);
        int counterAttacks = isChecked(currentBoard, oppositeColor, checkingPieceFile, checkingPieceRank, false);
        ReturnPiece counterPiece;

        if (counterAttacks > 0) {
            if (counterAttacks > 2) return false;
            counterPiece = getCheckingPiece(currentBoard, oppositeColor, checkingPieceFile, checkingPieceRank, false);

            // Try countering, if it works then no check mate
            Piece.saveBoard(currentBoard);

            int counterPieceFile = Chess.getFile(counterPiece.pieceFile);
            int counterPieceRank = Chess.getRank(counterPiece.pieceRank);
            currentBoard[checkingPieceFile][checkingPieceRank] = counterPiece;
            currentBoard[counterPieceFile][counterPieceRank] = null;

            // If there is no check, then no check mate
            if (isChecked(currentBoard, ownColor, ownKingFile, ownKingRank, false) < 1) {
                Piece.revertBoard(currentBoard);
                return false;
            }

            Piece.revertBoard(currentBoard);
        }
        
        // If it can't be captured safely by other pieces and it it a pawn, then check if it can be en-passant
        if (checkingPiece.pieceType == PieceType.BP) { // If black pawn is the one providing the check
            if (checkingPiece.equals(Pawn.enPassantTarget)) {
                if (((checkingPieceFile + 1) <= 7) && 
                    (currentBoard[checkingPieceFile + 1][checkingPieceRank] != null) && 
                    (currentBoard[checkingPieceFile + 1][checkingPieceRank].pieceType == PieceType.WP)) {
                        // Try to see if the pawn is protected by a queen or bishop, opposite of king
                        for (int i = 2; ((checkingPieceFile + i) <= 7) && ((checkingPieceRank - i) >= 0); i++) { // Up, right
                            if (currentBoard[checkingPieceFile + i][checkingPieceRank - i] != null) {
                                if ((currentBoard[checkingPieceFile + i][checkingPieceRank - i].pieceType == PieceType.BB) || 
                                    (currentBoard[checkingPieceFile + i][checkingPieceRank - i].pieceType == PieceType.BQ)) {
                                        return true;
                                }
                                break;
                            }
                        }
                        

                        return false; // If a white pawn is to the right, no checkmate
                    }
                if (((checkingPieceFile - 1) >= 0) &&
                    (currentBoard[checkingPieceFile - 1][checkingPieceRank] != null) && 
                    (currentBoard[checkingPieceFile - 1][checkingPieceRank].pieceType == PieceType.WP)) {
                        // Try to see if the pawn is protected by a queen or bishop, opposite of king
                        for (int i = 2; ((checkingPieceFile - i) >= 0) && ((checkingPieceRank - i) >= 0); i++) { // Up, left
                            if (currentBoard[checkingPieceFile - i][checkingPieceRank - i] != null) {
                                if ((currentBoard[checkingPieceFile - i][checkingPieceRank - i].pieceType == PieceType.BB) || 
                                    (currentBoard[checkingPieceFile - i][checkingPieceRank - i].pieceType == PieceType.BQ)) {
                                        return true;
                                }
                                break;
                            }
                        }

                        return false; // If a white pawn is to the left, no checkmate
                    }
            }
        } else if (checkingPiece.pieceType == PieceType.WP) { // If white pawn is the one providing the check
            if (((checkingPieceFile + 1) <= 7) && 
                    (currentBoard[checkingPieceFile + 1][checkingPieceRank] != null) && 
                    (currentBoard[checkingPieceFile + 1][checkingPieceRank].pieceType == PieceType.BP)) {
                        // Try to see if the pawn is protected by a queen or bishop, opposite of king
                        for (int i = 2; ((checkingPieceFile + i) <= 7) && ((checkingPieceRank + i) <= 7); i++) { // Down, right
                            if (currentBoard[checkingPieceFile + i][checkingPieceRank + i] != null) {
                                if ((currentBoard[checkingPieceFile + i][checkingPieceRank + i].pieceType == PieceType.BB) || 
                                    (currentBoard[checkingPieceFile + i][checkingPieceRank + i].pieceType == PieceType.BQ)) {
                                        return true;
                                }
                                break;
                            }
                        }

                        return false; // If a black pawn is to the right, no checkmate
                    }
                if (((checkingPieceFile - 1) >= 0) &&
                    (currentBoard[checkingPieceFile - 1][checkingPieceRank] != null) && 
                    (currentBoard[checkingPieceFile - 1][checkingPieceRank].pieceType == PieceType.BP)) {
                        // Try to see if the pawn is protected by a queen or bishop, opposite of king
                        for (int i = 2; ((checkingPieceFile - i) >= 0) && ((checkingPieceRank + i) <= 7); i++) { // Down, right
                            if (currentBoard[checkingPieceFile - i][checkingPieceRank + i] != null) {
                                if ((currentBoard[checkingPieceFile - i][checkingPieceRank + i].pieceType == PieceType.BB) || 
                                    (currentBoard[checkingPieceFile - i][checkingPieceRank + i].pieceType == PieceType.BQ)) {
                                        return true;
                                }
                                break;
                            }
                        }
                        
                        return false; // If a black pawn is to the left, no checkmate
                    }
        }

        // Determine which direction the checking piece is in
        // If the square can be blocked by movement, no checkmate
        if ((checkingPieceFile == ownKingFile) && (checkingPieceRank < ownKingRank)) { // Check from N
            for (int r = ownKingRank - 1; r > checkingPieceRank; r--) {
                if (canBeReached(currentBoard, ownColor, ownKingFile, r)) return false;
            }
        } else if ((checkingPieceFile == ownKingFile) && (checkingPieceRank > ownKingRank)) { // Check from S
            for (int r = ownKingRank + 1; r < checkingPieceRank; r++) {
                if (canBeReached(currentBoard, ownColor, ownKingFile, r)) return false;
            }
        } else if ((checkingPieceFile > ownKingFile) && (checkingPieceRank == ownKingRank)) { // Check from E
            for (int f = ownKingFile + 1; f < checkingPieceRank; f++) {
                if (canBeReached(currentBoard, ownColor, f, ownKingRank)) return false;
            }
        } else if ((checkingPieceFile < ownKingFile) && (checkingPieceRank == ownKingRank)) { // Check from W
            for (int f = ownKingFile - 1; f > checkingPieceRank; f--) {
                if (canBeReached(currentBoard, ownColor, f, ownKingRank)) return false;
            }
        } else if ((checkingPieceFile > ownKingFile) && (checkingPieceRank < ownKingRank)) { // Check from NE
            for (int i = 1; (((ownKingFile + i) < checkingPieceFile) && ((ownKingRank - i) > checkingPieceRank)); i++) {
                if (canBeReached(currentBoard, ownColor, ownKingFile + i, ownKingRank - i)) return false;
            }
        } else if ((checkingPieceFile > ownKingFile) && (checkingPieceRank > ownKingRank)) { // Check from SE
            for (int i = 1; (((ownKingFile + i) < checkingPieceFile) && ((ownKingRank + i) < checkingPieceRank)); i++) {
                if (canBeReached(currentBoard, ownColor, ownKingFile + i, ownKingRank + i)) return false;
            }
        } else if ((checkingPieceFile < ownKingFile) && (checkingPieceRank < ownKingRank)) { // Check from NW
            for (int i = 1; (((ownKingFile - i) > checkingPieceFile) && ((ownKingRank - i) > checkingPieceRank)); i++) {
                if (canBeReached(currentBoard, ownColor, ownKingFile - i, ownKingRank - i)) return false;
            }
        } else if ((checkingPieceFile < ownKingFile) && (checkingPieceRank > ownKingRank)) { // Check from SW
            for (int i = 1; (((ownKingFile - i) > checkingPieceFile) && ((ownKingRank + i) < checkingPieceRank)); i++) {
                if (canBeReached(currentBoard, ownColor, ownKingFile - i, ownKingRank + i)) return false;
            }
        }

        return true;
    }

    // Returns the piece that is checking the king
    // Only use when there is one check, otherwise it will only return the first one it finds
    private static ReturnPiece getCheckingPiece(ReturnPiece[][] currentBoard, Player ownColor, int checkFile, int checkRank, boolean ignoreOwnKing) {
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
                    return currentBoard[checkFile][r];
                }
                break;
            }
        }

        for (int r = checkRank + 1; r <= 7; r++) { // Down
            if (currentBoard[checkFile][r] != null && !((ignoreOwnKing) && (currentBoard[checkFile][r].pieceType == ownKing))) {
                if ((currentBoard[checkFile][r].pieceType == oppRook) || (currentBoard[checkFile][r].pieceType == oppQueen)) {
                    return currentBoard[checkFile][r];
                }
                break;
            }
        }

        for (int f = checkFile + 1; f <= 7; f++) { // Right
            if (currentBoard[f][checkRank] != null && !((ignoreOwnKing) && (currentBoard[f][checkRank].pieceType == ownKing))) {
                if ((currentBoard[f][checkRank].pieceType == oppRook) || (currentBoard[f][checkRank].pieceType == oppQueen)) {
                    return currentBoard[f][checkRank];
                }
                break;
            }
        }

        for (int f = checkFile - 1; f >= 0; f--) { // Left
            if (currentBoard[f][checkRank] != null && !((ignoreOwnKing) && (currentBoard[f][checkRank].pieceType == ownKing))) {
                if ((currentBoard[f][checkRank].pieceType == oppRook) || (currentBoard[f][checkRank].pieceType == oppQueen)) {
                    return currentBoard[f][checkRank];
                }
                break;
            }
        }

        // If opposite color bishop or queen in diagonal, then check
        for (int i = 1; ((checkFile + i) <= 7) && ((checkRank - i) >= 0); i++) { // Up, right
            if (currentBoard[checkFile + i][checkRank - i] != null && !((ignoreOwnKing) && (currentBoard[checkFile + i][checkRank - i].pieceType == ownKing))) {
                if ((currentBoard[checkFile + i][checkRank - i].pieceType == oppBishop) || (currentBoard[checkFile + i][checkRank - i].pieceType == oppQueen)) {
                    return currentBoard[checkFile + i][checkRank - i];
                }
                break;
            }
        }

        for (int i = 1; ((checkFile - i) >= 0) && ((checkRank - i) >= 0); i++) { // Up, left
            if (currentBoard[checkFile - i][checkRank - i] != null && !((ignoreOwnKing) && (currentBoard[checkFile - i][checkRank - i].pieceType == ownKing))) {
                if ((currentBoard[checkFile - i][checkRank - i].pieceType == oppBishop) || (currentBoard[checkFile - i][checkRank - i].pieceType == oppQueen)) {
                    return currentBoard[checkFile - i][checkRank - i];
                }
                break;
            }
        }

        for (int i = 1; ((checkFile + i) <= 7) && ((checkRank + i) <= 7); i++) { // Down, right
            if (currentBoard[checkFile + i][checkRank + i] != null && !((ignoreOwnKing) && (currentBoard[checkFile + i][checkRank + i].pieceType == ownKing))) {
                if ((currentBoard[checkFile + i][checkRank + i].pieceType == oppBishop) || (currentBoard[checkFile + i][checkRank + i].pieceType == oppQueen)) {
                    return currentBoard[checkFile + i][checkRank + i];
                }
                break;
            }
        }

        for (int i = 1; ((checkFile - i) >= 0) && ((checkRank + i) <= 7); i++) { // Down, left
            if (currentBoard[checkFile - i][checkRank + i] != null && !((ignoreOwnKing) && (currentBoard[checkFile - i][checkRank + i].pieceType == ownKing))) {
                if ((currentBoard[checkFile - i][checkRank + i].pieceType == oppBishop) || (currentBoard[checkFile - i][checkRank + i].pieceType == oppQueen)) {
                    return currentBoard[checkFile - i][checkRank + i];
                }
                break;
            }
        }

        // If opposite color knight in the 8 knight moves squares, then check
        if (((checkFile + 1) <= 7) && ((checkRank - 2) >= 0) && 
            (currentBoard[checkFile + 1][checkRank - 2] != null ) && 
            (currentBoard[checkFile + 1][checkRank - 2].pieceType == oppKnight)) 
                return currentBoard[checkFile + 1][checkRank - 2]; // 2 up, 1 right
        if (((checkFile - 1) >= 0) && ((checkRank - 2) >= 0) && 
            (currentBoard[checkFile - 1][checkRank - 2] != null ) && 
            (currentBoard[checkFile - 1][checkRank - 2].pieceType == oppKnight)) 
                return currentBoard[checkFile - 1][checkRank - 2]; // 2 up, 1 left
        if (((checkFile + 1) <= 7) && ((checkRank + 2) <= 7) && 
            (currentBoard[checkFile + 1][checkRank + 2] != null ) && 
            (currentBoard[checkFile + 1][checkRank + 2].pieceType == oppKnight)) 
                return currentBoard[checkFile + 1][checkRank + 2]; // 2 down, 1 right
        if (((checkFile - 1) >= 0) && ((checkRank + 2) <= 7) && 
            (currentBoard[checkFile - 1][checkRank + 2] != null ) && 
            (currentBoard[checkFile - 1][checkRank + 2].pieceType == oppKnight)) 
                return currentBoard[checkFile - 1][checkRank + 2]; // 2 down, 1 left
        if (((checkFile + 2) <= 7) && ((checkRank - 1) >= 0) && 
            (currentBoard[checkFile + 2][checkRank - 1] != null ) && 
            (currentBoard[checkFile + 2][checkRank - 1].pieceType == oppKnight)) 
                return currentBoard[checkFile + 2][checkRank - 1]; // 1 up, 2 right
        if (((checkFile - 2) >= 0) && ((checkRank - 1) >= 0) && 
            (currentBoard[checkFile - 2][checkRank - 1] != null ) && 
            (currentBoard[checkFile - 2][checkRank - 1].pieceType == oppKnight)) 
                return currentBoard[checkFile - 2][checkRank - 1]; // 1 up, 2 left
        if (((checkFile + 2) <= 7) && ((checkRank + 1) <= 7) && 
            (currentBoard[checkFile + 2][checkRank + 1] != null ) && 
            (currentBoard[checkFile + 2][checkRank + 1].pieceType == oppKnight)) 
                return currentBoard[checkFile + 2][checkRank + 1]; // 1 down, 2 right
        if (((checkFile - 2) >= 0) && ((checkRank + 1) <= 7) && 
            (currentBoard[checkFile - 2][checkRank + 1] != null ) && 
            (currentBoard[checkFile - 2][checkRank + 1].pieceType == oppKnight)) 
                return currentBoard[checkFile - 2][checkRank + 1]; // 1 down, 2 left


        // Opposite color pawn capture position (different direction based on color), then check
        if (oppPawn == PieceType.BP) { // Opposite pawn is black
            if (((checkFile + 1) <= 7) && ((checkRank - 1) >= 0) && 
                (currentBoard[checkFile + 1][checkRank - 1] != null ) && 
                (currentBoard[checkFile + 1][checkRank - 1].pieceType == oppPawn)) 
                    return currentBoard[checkFile + 1][checkRank - 1]; // 1 up, 1 right
            if (((checkFile - 1) >= 0) && ((checkRank - 1) >= 0) && 
                (currentBoard[checkFile - 1][checkRank - 1] != null ) && 
                (currentBoard[checkFile - 1][checkRank - 1].pieceType == oppPawn)) 
                    return currentBoard[checkFile - 1][checkRank - 1]; // 1 up, 1 left
        } else {
            if (((checkFile + 1) <= 7) && ((checkRank + 1) <= 7) && 
                (currentBoard[checkFile + 1][checkRank + 1] != null ) && 
                (currentBoard[checkFile + 1][checkRank + 1].pieceType == oppPawn)) 
                    return currentBoard[checkFile + 1][checkRank + 1]; // 1 down, 1 right
            if (((checkFile - 1) >= 0) && ((checkRank + 1) <= 7) && 
                (currentBoard[checkFile - 1][checkRank + 1] != null ) && 
                (currentBoard[checkFile - 1][checkRank + 1].pieceType == oppPawn)) 
                    return currentBoard[checkFile - 1][checkRank + 1]; // 1 down, 1 left
        }
        
        // Opposite color king in the 8 squares around
        for (int r = -1; r < 2; r++) { // Check top, mid, bot
            if (!((checkRank + r) >= 0) || !((checkRank + r) <= 7)) continue;
            for (int f = -1; f < 2; f++) { // Check left, mid, right
                if ((r == 0) && (f == 0)) continue; // Skip the given square
                if (!((checkFile + f) >= 0) || !((checkFile + f) <= 7)) continue;
                if (currentBoard[checkFile + f][checkRank + r] == null) continue;

                if (currentBoard[checkFile + f][checkRank + r].pieceType == oppKing) {
                    return currentBoard[checkFile + f][checkRank + r];
                }
            }
        }

        return null; // Returns null if cannnot find piece, meaning error or bad call
    }

    // A method that returns true if any piece of own color can move to that location
    private static boolean canBeReached(ReturnPiece[][] currentBoard, Player ownColor, int targetFile, int targetRank) {
        // Piece type of own color
        PieceType ownPawn, ownRook, ownKnight, ownBishop, ownQueen;
        if (ownColor == Player.white) {
            ownPawn = PieceType.WP;
            ownRook = PieceType.WR;
            ownKnight = PieceType.WN;
            ownBishop = PieceType.WB;
            ownQueen = PieceType.WQ;
        } else {
            ownPawn = PieceType.BP;
            ownRook = PieceType.BR;
            ownKnight = PieceType.BN;
            ownBishop = PieceType.BB;
            ownQueen = PieceType.BQ;
        }

        // If own color rook or queen in a plus, then can be reached
        for (int r = targetRank - 1; r >= 0; r--) { // Up
            if (currentBoard[targetFile][r] != null) {
                if ((currentBoard[targetFile][r].pieceType == ownRook) || (currentBoard[targetFile][r].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        for (int r = targetRank + 1; r <= 7; r++) { // Down
            if (currentBoard[targetFile][r] != null) {
                if ((currentBoard[targetFile][r].pieceType == ownRook) || (currentBoard[targetFile][r].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        for (int f = targetFile + 1; f <= 7; f++) { // Right
            if (currentBoard[f][targetRank] != null) {
                if ((currentBoard[f][targetRank].pieceType == ownRook) || (currentBoard[f][targetRank].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        for (int f = targetFile - 1; f >= 0; f--) { // Left
            if (currentBoard[f][targetRank] != null) {
                if ((currentBoard[f][targetRank].pieceType == ownRook) || (currentBoard[f][targetRank].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        // If own color bishop or queen in diagonal, then can be reached
        for (int i = 1; ((targetFile + i) <= 7) && ((targetRank - i) >= 0); i++) { // Up, right
            if (currentBoard[targetFile + i][targetRank - i] != null) {
                if ((currentBoard[targetFile + i][targetRank - i].pieceType == ownBishop) || (currentBoard[targetFile + i][targetRank - i].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        for (int i = 1; ((targetFile - i) >= 0) && ((targetRank - i) >= 0); i++) { // Up, left
           if (currentBoard[targetFile - i][targetRank - i] != null) {
                if ((currentBoard[targetFile - i][targetRank - i].pieceType == ownBishop) || (currentBoard[targetFile - i][targetRank - i].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        for (int i = 1; ((targetFile + i) <= 7) && ((targetRank + i) <= 7); i++) { // Down, right
            if (currentBoard[targetFile + i][targetRank + i] != null) {
                if ((currentBoard[targetFile + i][targetRank + i].pieceType == ownBishop) || (currentBoard[targetFile + i][targetRank + i].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        for (int i = 1; ((targetFile - i) >= 0) && ((targetRank + i) <= 7); i++) { // Down, left
            if (currentBoard[targetFile - i][targetRank + i] != null) {
                if ((currentBoard[targetFile - i][targetRank + i].pieceType == ownBishop) || (currentBoard[targetFile - i][targetRank + i].pieceType == ownQueen)) {
                    return true;
                }
                break;
            }
        }

        // If own color knight in the 8 knight moves squares, then can be reached
        if (((targetFile + 1) <= 7) && ((targetRank - 2) >= 0) && 
            (currentBoard[targetFile + 1][targetRank - 2] != null ) && 
            (currentBoard[targetFile + 1][targetRank - 2].pieceType == ownKnight)) 
                return true; // 2 up, 1 right
        if (((targetFile - 1) >= 0) && ((targetRank - 2) >= 0) && 
            (currentBoard[targetFile - 1][targetRank - 2] != null ) && 
            (currentBoard[targetFile - 1][targetRank - 2].pieceType == ownKnight)) 
                return true; // 2 up, 1 left
        if (((targetFile + 1) <= 7) && ((targetRank + 2) <= 7) && 
            (currentBoard[targetFile + 1][targetRank + 2] != null ) && 
            (currentBoard[targetFile + 1][targetRank + 2].pieceType == ownKnight)) 
                return true; // 2 down, 1 right
        if (((targetFile - 1) >= 0) && ((targetRank + 2) <= 7) && 
            (currentBoard[targetFile - 1][targetRank + 2] != null ) && 
            (currentBoard[targetFile - 1][targetRank + 2].pieceType == ownKnight)) 
                return true; // 2 down, 1 left
        if (((targetFile + 2) <= 7) && ((targetRank - 1) >= 0) && 
            (currentBoard[targetFile + 2][targetRank - 1] != null ) && 
            (currentBoard[targetFile + 2][targetRank - 1].pieceType == ownKnight)) 
                return true; // 1 up, 2 right
        if (((targetFile - 2) >= 0) && ((targetRank - 1) >= 0) && 
            (currentBoard[targetFile - 2][targetRank - 1] != null ) && 
            (currentBoard[targetFile - 2][targetRank - 1].pieceType == ownKnight)) 
                return true; // 1 up, 2 left
        if (((targetFile + 2) <= 7) && ((targetRank + 1) <= 7) && 
            (currentBoard[targetFile + 2][targetRank + 1] != null ) && 
            (currentBoard[targetFile + 2][targetRank + 1].pieceType == ownKnight)) 
                return true; // 1 down, 2 right
        if (((targetFile - 2) >= 0) && ((targetRank + 1) <= 7) && 
            (currentBoard[targetFile - 2][targetRank + 1] != null ) && 
            (currentBoard[targetFile - 2][targetRank + 1].pieceType == ownKnight)) 
                return true; // 1 down, 2 left


        // Own color pawn capture position (different direction based on color), then can be reached
        if (ownPawn == PieceType.BP) { // Own pawn is black
            if (((targetRank - 1) >= 0) && 
                (currentBoard[targetFile][targetRank - 1] != null ) && 
                (currentBoard[targetFile][targetRank - 1].pieceType == ownPawn)) 
                    return true; // 1 up
            if (((targetRank - 2) == 1) && 
                (currentBoard[targetFile][targetRank - 2] != null ) && 
                (currentBoard[targetFile][targetRank - 2].pieceType == ownPawn)) 
                    return true; // 2 up from starting pos
        } else {
            if (((targetRank + 1) >= 0) && 
                (currentBoard[targetFile][targetRank + 1] != null ) && 
                (currentBoard[targetFile][targetRank + 1].pieceType == ownPawn)) 
                    return true; // 1 down
            if (((targetRank + 2) == 6) && 
                (currentBoard[targetFile][targetRank + 2] != null ) && 
                (currentBoard[targetFile][targetRank + 2].pieceType == ownPawn)) 
                    return true; // 1 down, 1 left
        }

        return false; // Returns null if cannnot find piece, meaning error or bad call
    }
}

