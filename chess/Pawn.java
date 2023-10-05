package chess;

import chess.Chess.Player;

public class Pawn extends ReturnPiece {
    // Creates a pawn and which can be casted to returnPiece
    Pawn(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WP : PieceType.BP;
        pieceFile = file;
        pieceRank = rank;
    }
}
