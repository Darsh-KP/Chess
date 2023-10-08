package chess;

import chess.Chess.Player;

public class Knight extends ReturnPiece {
    // Creates a knight, which can be casted to returnPiece
    Knight(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WN : PieceType.BN;
        pieceFile = file;
        pieceRank = rank;
    }
}