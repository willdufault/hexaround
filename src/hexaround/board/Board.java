package hexaround.board;

import java.util.ArrayList;

public class Board {
    private int count = 0;
    private int offset = 0;
    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();

    public Board(int count) {
        this.count = count;
        this.offset = count / 2;

        this.initBoard();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(int r = 0; r < this.count; r++) {
            StringBuilder row = new StringBuilder(" ");

            for(int c = 0; c < this.count; c++) {
                row.append(
                        this.tiles.get(r).get(c) != null ?
                        String.format("(%d, %d) ", r - this.offset, c - this.offset) :
                        "(null) "
                );
            }

            result.append(String.format("[%s]\n", row.toString()));
        }

        return result.toString();
    }

    private void initBoard() {
        for (int r = 0; r < this.count; r++) {
            this.tiles.add(new ArrayList<>());

            int shift = this.offset - r - 1;

            for (int c = 0; c < this.count; c++) {
                this.tiles.get(r).add(
                        c > shift && c <= this.count + shift ?
                        new Tile() :
                        null
                );
            }
        }
    }
}
