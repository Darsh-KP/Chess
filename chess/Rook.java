package chess;

import chess.Chess.Player;

public class Rook extends ReturnPiece {
    // Creates a rook, which can be casted to returnPiece
    Rook(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WR : PieceType.BR;
        pieceFile = file;
        pieceRank = rank;
    }
}