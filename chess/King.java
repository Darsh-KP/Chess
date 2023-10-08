package chess;

import chess.Chess.Player;

public class King extends ReturnPiece {
    // Creates a king, which can be casted to returnPiece
    King(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WK : PieceType.BK;
        pieceFile = file;
        pieceRank = rank;
    }
}