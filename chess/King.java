package chess;

import chess.Chess.Player;

public class King extends ReturnPiece {
    // Keeps track if the piece is allowed to castle
    boolean canCastle = true;

    // Keeps track of both kings' positions, used for checking for check
    static ReturnPiece whiteKing;
    static ReturnPiece blackKing;

    // Creates a king, which can be casted to returnPiece
    King(Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WK : PieceType.BK;
        pieceFile = file;
        pieceRank = rank;

        // Put a tracker on the king
        if (pieceType == PieceType.WK) whiteKing = this;
        if (pieceType == PieceType.BK) blackKing = this;
    }

    public int move(String moveTo, ReturnPiece[][] currentBoard) {
        // Get move destination
        int fileDestination = Chess.getFile(moveTo.charAt(0));
        int rankDestination = Chess.getRank(Character.getNumericValue(moveTo.charAt(1)));
        
        // Check if the move is valid
        if (isValidMove(currentBoard, fileDestination, rankDestination) == -1) return -1;

        // Cannot caslte once the piece has moved
        if (canCastle == true) canCastle = false;

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

    private int isValidMove(ReturnPiece[][] currentBoard, int toFile, int toRank) {
        // Where the piece is currently
        int thisFile = Chess.getFile(pieceFile);
        int thisRank = Chess.getRank(pieceRank);

        // Check if the king can take 1 step in any direction
        for (int r = -1; r < 2; r++) {
            for (int f = -1; f < 2; f++) {
                if ((r == 0) && (f == 0)) continue;
                if ((toFile != thisFile + f) || (toRank != thisRank + r)) continue;

                // Empty square
                if (currentBoard[toFile][toRank] == null) return 1;

                // Capture the piece
                if (Piece.canCapture(this.pieceType, currentBoard[toFile][toRank].pieceType)) return 1;
            }
        }

        // Check if the king can castle
        if (!canCastle) return -1;

        // Right side castle
        if (toFile == Chess.getFile('g')) {
            // Check if there is a piece on the h file
            if (currentBoard[Chess.getFile('h')][thisRank] == null) return -1;

            // Check if the piece is the same colored rook on the h file
            if ((currentBoard[Chess.getFile('h')][thisRank].pieceType != 
                ((pieceType == PieceType.WK) ? PieceType.WR : PieceType.BR))) return -1;

            // Check if the rook can castle
            if (!((Rook)currentBoard[Chess.getFile('h')][thisRank]).canCastle) return -1;

            // Check if anything blocks the path
            if (currentBoard[Chess.getFile('f')][thisRank] != null) return -1;
            if (currentBoard[Chess.getFile('g')][thisRank] != null) return -1;

            // Can't castle if the king is in check already
            // Check if the path of the king has a check going through

            // Allowed to castle, move the rook
            currentBoard[Chess.getFile('f')][thisRank] = currentBoard[Chess.getFile('h')][thisRank];
            ((Rook)currentBoard[Chess.getFile('f')][thisRank]).pieceFile = PieceFile.f;
            ((Rook)currentBoard[Chess.getFile('f')][thisRank]).canCastle = false;
            currentBoard[Chess.getFile('h')][thisRank] = null;
            return 1;
        }

        // Left side castle
        if (toFile == Chess.getFile('c')) {
            // Check if there is a piece on the a file
            if (currentBoard[Chess.getFile('a')][thisRank] == null) return -1;

            // Check if the piece is the same colored rook on the a file
            if ((currentBoard[Chess.getFile('a')][thisRank].pieceType != 
                ((pieceType == PieceType.WK) ? PieceType.WR : PieceType.BR))) return -1;

            // Check if the rook can castle
            if (!((Rook)currentBoard[Chess.getFile('a')][thisRank]).canCastle) return -1;

            // Check if anything blocks the path
            if (currentBoard[Chess.getFile('d')][thisRank] != null) return -1;
            if (currentBoard[Chess.getFile('c')][thisRank] != null) return -1;
            if (currentBoard[Chess.getFile('b')][thisRank] != null) return -1;

            // Can't castle if the king is in check already
            // Check if the path of the king has a check going through

            // Allowed to castle, move the rook
            currentBoard[Chess.getFile('d')][thisRank] = currentBoard[Chess.getFile('a')][thisRank];
            ((Rook)currentBoard[Chess.getFile('d')][thisRank]).pieceFile = PieceFile.d;
            ((Rook)currentBoard[Chess.getFile('d')][thisRank]).canCastle = false;
            currentBoard[Chess.getFile('a')][thisRank] = null;
            return 1;
        }

        return -1;
        // Return 1 for succesful move
        // -1 for illegal move
    }
}