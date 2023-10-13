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
        // Get move destination
        int fileDestination = Chess.getFile(moveTo.charAt(0));
        int rankDestination = Chess.getRank(Character.getNumericValue(moveTo.charAt(1)));

        // Checks for pawn color, as white pawn and black pawn has differnt moves (differnt directions)
        int moveStatus = 0;
        if (pieceType == PieceType.WP) {
            moveStatus = isValidMoveWhite(currentBoard, fileDestination, rankDestination);
        } else if (pieceType == PieceType.BP) {
            moveStatus = isValidMoveBlack(currentBoard, fileDestination, rankDestination);
        }

        // Check is the move is valid (legal)
        switch (moveStatus) {
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

        // Check for pawn promotion
        if ((pieceType == PieceType.WP) && (pieceRank == 8)) {
            // Check for queen promotion
            if (moveTo.length() >= 3) {
                if (moveTo.charAt(3) == 'R') {
                    // Rook promotion
                    currentBoard[fileDestination][rankDestination] = new Rook(Player.white, pieceFile, pieceRank);
                } else if (moveTo.charAt(3) == 'N') {
                    // Knight promotion
                    currentBoard[fileDestination][rankDestination] = new Knight(Player.white, pieceFile, pieceRank);
                } else if (moveTo.charAt(3) == 'B') {
                    // Bishop promotion
                    currentBoard[fileDestination][rankDestination] = new Bishop(Player.white, pieceFile, pieceRank);
                } 
            }
            // Queen promotion
            currentBoard[fileDestination][rankDestination] = new Queen(Player.white, pieceFile, pieceRank);
        } else if ((pieceType == PieceType.BP) && (pieceRank == 1)) {
            // Check for queen promotion
            if (moveTo.length() >= 3) {
                if (moveTo.charAt(3) == 'R') {
                    // Rook promotion
                    currentBoard[fileDestination][rankDestination] = new Rook(Player.black, pieceFile, pieceRank);
                } else if (moveTo.charAt(3) == 'N') {
                    // Knight promotion
                    currentBoard[fileDestination][rankDestination] = new Knight(Player.black, pieceFile, pieceRank);
                } else if (moveTo.charAt(3) == 'B') {
                    // Bishop promotion
                    currentBoard[fileDestination][rankDestination] = new Bishop(Player.black, pieceFile, pieceRank);
                }
            }
            // Queen promotion
            currentBoard[fileDestination][rankDestination] = new Queen(Player.black, pieceFile, pieceRank);
        }
        
        
        // Cannont move a piece if there is a check
            // Can move if the move can block a check or capture the checking piece
                // Cannont capture checking piece if double check, or it results in check
                // (if check is still present after the move)
            // Cannot move into a check position

        // Find a way to return the original board if move was unable to be exceuted
            // Return -1 if move was unsuccesful (Illegal Move)
        

        return 1;
        // Return 1 for succesful move
        // -1 for illegal move
    }

    // Checks if the move is valid for white pawn
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
            if (!(Piece.canCapture(this.pieceType, currentBoard[thisFile + 1][thisRank - 1].pieceType))) return -1;
            return 3;
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
            if (!(Piece.canCapture(this.pieceType, currentBoard[thisFile - 1][thisRank - 1].pieceType))) return -1;
            return 3;
        }

        return -1;
        // Return -1 means not a valid move
        // 1 means moved 1 step, 2 means moved 2 steps
        // 3 means normal capture
        // 4 means en passant
    }

    // Checks if the move is valid for black pawn
    private int isValidMoveBlack(ReturnPiece[][] currentBoard, int toFile, int toRank) {
        // Where the piece is currently
        int thisFile = Chess.getFile(pieceFile);
        int thisRank = Chess.getRank(pieceRank);

        // Check if the pawn can move forward
        if ((toFile == thisFile) && (toRank == thisRank + 1)) {
            if (currentBoard[thisFile][thisRank + 1] != null) return -1;
            return 1;
        }

        // Check if the pawn can take 2 steps from the starting position
        if ((toFile == thisFile) && (toRank == Chess.getRank(5))) {
            if (currentBoard[thisFile][thisRank + 1] != null) return -1;
            if (currentBoard[thisFile][thisRank + 2] != null) return -1;
            return 2;
        }

        // Check if the pawn can capture right
        if ((toFile == thisFile + 1) && (toRank == thisRank + 1)) {
            if (currentBoard[thisFile + 1][thisRank + 1] == null) {
                // Check if you en-passant is possible
                if ((thisRank == Chess.getRank(4)) && 
                    (currentBoard[thisFile + 1][thisRank].equals(enPassantTarget))) 
                    return 4;
                
                return -1;
            }

            // Check if the piece to capture is of opposite color
            if (!(Piece.canCapture(this.pieceType, currentBoard[thisFile + 1][thisRank + 1].pieceType))) return -1;
            return 3;
        }
        
        // Check if the pawn can capture left
        if ((toFile == thisFile - 1) && (toRank == thisRank + 1)) {
            if (currentBoard[thisFile - 1][thisRank + 1] == null) {
                // Check if you en-passant is possible
                if ((thisRank == Chess.getRank(4)) && 
                    (currentBoard[thisFile - 1][thisRank].equals(enPassantTarget))) 
                    return 4;

                return -1;
            }
            
            // Check if the piece to capture is of opposite color
            if (!(Piece.canCapture(this.pieceType, currentBoard[thisFile - 1][thisRank + 1].pieceType))) return -1;
            return 3;
        }

        return -1;
        // Return -1 means not a valid move
        // 1 means moved 1 step, 2 means moved 2 steps
        // 3 means normal capture
        // 4 means en passant
    }
}
