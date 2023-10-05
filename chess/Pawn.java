package chess;

import chess.Chess.Player;

public class Pawn extends ReturnPiece{
    Pawn(Chess.Player player, ReturnPiece.PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WP : PieceType.BP;
        pieceFile = file;
        pieceRank = rank;
    }
}
