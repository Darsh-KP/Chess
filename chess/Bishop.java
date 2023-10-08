package chess;

import chess.Chess.Player;

public class Bishop extends ReturnPiece {
    // Creates a bishop, which can be casted to returnPiece
    Bishop(Chess.Player player, PieceFile file, int rank) {
        pieceType = (player == Player.white) ? PieceType.WB : PieceType.BB;
        pieceFile = file;
        pieceRank = rank;
    }
}