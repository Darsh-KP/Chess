package chess;

import chess.Chess.Player;

public class Queen extends ReturnPiece {
    // Creates a queen, which can be casted to returnPiece
    Queen(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WQ : PieceType.BQ;
        pieceFile = file;
        pieceRank = rank;
    }
}