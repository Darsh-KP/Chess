package chess;

import chess.Chess.Player;

public class King extends ReturnPiece {
    // Creates a king, which can be casted to returnPiece
    King(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WK : PieceType.BK;
        pieceFile = file;
        pieceRank = rank;
    }

    // Castleing
            // Cannot castle if check going thourgh
            // Cannot castle if piece has moved
            // Only allowed if nothing in the way

}