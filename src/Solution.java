import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Solution {

    static PrintStream out;

    static final short U = 1;
    static final short D = 2;
    static final short L = 4;
    static final short R = 8;

    static final char LEFT_RIGHT = '\u2501';
    static final char UP_DOWN = '\u2503';
    static final char DOWN_RIGHT = '\u250F';
    static final char DOWN_LEFT = '\u2513';
    static final char UP_RIGHT = '\u2517';
    static final char UP_LEFT = '\u251B';
    static final char UP_DOWN_RIGHT = '\u2523';
    static final char UP_DOWN_LEFT = '\u252B';
    static final char DOWN_LEFT_RIGHT = '\u2533';
    static final char UP_LEFT_RIGHT = '\u253B';
    static final char UP_DOWN_LEFT_RIGHT = '\u254B';
    static final char LEFT = '\u2578';
    static final char UP = '\u2579';
    static final char RIGHT = '\u257A';
    static final char DOWN = '\u257B';

    public static void main(String args[]) throws Exception {
        out = new PrintStream(System.out, true, "UTF-8");
        if (args.length >= 2) {
            for (int i = 0; i < args.length; i += 2) {
                printMaze(args[i], args[i + 1]);
            }
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while (true) {
            line = br.readLine();
            if (line == null || "".equals(line.trim())) {
                break;
            }
            String[] dims = line.split(" ");
            printMaze(dims[0], dims[1]);
        }
    }

    private static void printMaze(String width, String height) {
        Dimension dim = new Dimension(Integer.parseInt(width), Integer.parseInt(height));
        printMaze(dim);
    }

    private static void printMaze(Dimension dim) {
        Maze m = new Maze(dim);
        Cell[][] cells = m.cells;
        Cell entry = m.entry;

        short[][] maze = new short[dim.height + 1][dim.width + 1];
        drawBorder(dim, maze);
        drawCellBounds(cells, maze);
        openDoors(dim, entry, maze);
        printMaze(maze);
    }

    static class Maze {
        final Dimension dim;
        Cell[][] cells;
        Cell entry;

        Maze(Dimension dim) {
            this.dim = dim;
            initializeCells();
            constructMaze();
        }

        private void initializeCells() {
            // build grid
            cells = new Cell[dim.width][dim.height];
            for (int i = 0; i < dim.width; i++) {
                for (int j = 0; j < dim.height; j++) {
                    cells[i][j] = new Cell(i, j);
                }
            }
            for (int i = 0; i < dim.width; i++) {
                for (int j = 0; j < dim.height; j++) {
                    if (i > 0) {
                        cells[i][j].walls.add(intern(new Wall(cells[i - 1][j], cells[i][j])));
                    }
                    if (j > 0) {
                        cells[i][j].walls.add(intern(new Wall(cells[i][j - 1], cells[i][j])));
                    }
                    if (i < dim.width - 1) {
                        cells[i][j].walls.add(intern(new Wall(cells[i][j], cells[i + 1][j])));
                    }
                    if (j < dim.height - 1) {
                        cells[i][j].walls.add(intern(new Wall(cells[i][j], cells[i][j + 1])));
                    }
                }
            }
        }

        private void constructMaze() {
            Stack<Cell> stack = new Stack<Cell>();
            stack.push(cells[dim.width - 1][dim.height - 1]);
            SecureRandom r = new SecureRandom();
            while (! stack.empty()) {
                Cell c = stack.peek();
                if (c.x == 0) {
                    entry = c; // this is a potential entry cell, but might be overridden if another one is found later.
                }
                c.visited = true;
                List<Wall> ws = new ArrayList<Wall>();
                for (Wall w : c.walls) {
                    if (! w.other(c).visited) {
                        ws.add(w);
                    }
                }
                if (ws.size() == 0) {
                    // this cell is a dead end, so pop
                    stack.pop();
                } else {
                    Wall w = ws.get(r.nextInt(ws.size()));
                    w.destroy();
                    stack.push(w.other(c));
                }
            }
        }

    }

    private static void printMaze(short[][] maze) {
        for (short[] aMaze : maze) {
            for (short c : aMaze) {
                out.print(maskToChar(c));
            }
            out.println();
        }
    }

    private static void openDoors(Dimension dim, Cell entry, short[][] maze) {
        maze[entry.y][0] &= ~D;
        maze[entry.y + 1][0] &= ~U;
        maze[dim.height - 1][dim.width] &= ~D;
        maze[dim.height][dim.width] &= ~U;
    }

    private static void drawCellBounds(Cell[][] cells, short[][] maze) {
        for (Cell[] cs : cells) {
            for (Cell c : cs) {
                if (c.isWallUp()) {
                    maze[c.y][c.x] |= R;
                    maze[c.y][c.x + 1] |= L;
                }
                if (c.isWallDown()) {
                    maze[c.y + 1][c.x] |= R;
                    maze[c.y + 1][c.x + 1] |= L;
                }
                if (c.isWallLeft()) {
                    maze[c.y][c.x] |= D;
                    maze[c.y + 1][c.x] |= U;
                }
                if (c.isWallRight()) {
                    maze[c.y][c.x + 1] |= D;
                    maze[c.y + 1][c.x + 1] |= U;
                }
            }
        }
    }

    private static void drawBorder(Dimension dim, short[][] maze) {
        for (int i = 0; i <= dim.width; i++) {
            for (int j = 0; j <= dim.height; j++) {
                if (i == 0) {
                    if (j == 0) {
                        maze[j][i] = D | R;
                    } else if (j == dim.height) {
                        maze[j][i] = U | R;
                    } else {
                        maze[j][i] = U | D;
                    }
                } else if (i == dim.width) {
                    if (j == 0) {
                        maze[j][i] = D | L;
                    } else if (j == dim.height) {
                        maze[j][i] = U | L;
                    } else {
                        maze[j][i] = U | D;
                    }
                } else if (j == 0 || j == dim.height) {
                    maze[j][i] = L | R;
                }
            }
        }
    }

    static char maskToChar(short s) {
        if ((s & U) != 0) {
            if ((s & D) != 0) {
                if ((s & L) != 0) {
                    if ((s & R) != 0) {
                        return UP_DOWN_LEFT_RIGHT;
                    } else { // not right
                        return UP_DOWN_LEFT;
                    }
                } else { // not left
                    if ((s & R) != 0) {
                        return UP_DOWN_RIGHT;
                    } else { // not right
                        return UP_DOWN;
                    }
                }
            } else { // not down
                if ((s & L) != 0) {
                    if ((s & R) != 0) {
                        return UP_LEFT_RIGHT;
                    } else { // not right
                        return UP_LEFT;
                    }
                } else { // not left
                    if ((s & R) != 0) {
                        return UP_RIGHT;
                    } else { // not right
                        return UP;
                    }
                }
            }
        } else { // not up
            if ((s & D) != 0) {
                if ((s & L) != 0) {
                    if ((s & R) != 0) {
                        return DOWN_LEFT_RIGHT;
                    } else { // not right
                        return DOWN_LEFT;
                    }
                } else { // not left
                    if ((s & R) != 0) {
                        return DOWN_RIGHT;
                    } else { // not right
                        return DOWN;
                    }
                }
            } else { // not down
                if ((s & L) != 0) {
                    if ((s & R) != 0) {
                        return LEFT_RIGHT;
                    } else { // not right
                        return LEFT;
                    }
                } else { // not left
                    if ((s & R) != 0) {
                        return RIGHT;
                    } else { // not right
                        return ' '; // ("" + s).charAt(s < 10 ? 0 : 1);
                    }
                }
            }
        }
    }

    static Set<Wall> _walls = new HashSet<Wall>();
    static Wall intern(Wall wall) {
        if (! _walls.add(wall)) {
            for (Wall w : _walls) {
                if (w == wall) {
                    return w;
                }
            }
        }
        return wall;
    }

    static class Wall {
        Cell a;
        Cell b;

        Wall(Cell a, Cell b) {
            this.a = a;
            this.b = b;
        }

        Cell other(Cell one) {
            return a == one ? b : a;
        }

        void destroy() {
            a.walls.remove(this);
            b.walls.remove(this);
            _walls.remove(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Wall)) return false;

            Wall wall = (Wall) o;

            if (a != null ? !a.equals(wall.a) : wall.a != null) return false;
            //noinspection RedundantIfStatement
            if (b != null ? !b.equals(wall.b) : wall.b != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = a != null ? a.hashCode() : 0;
            result = 31 * result + (b != null ? b.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Wall{" +
                "a=" + a +
                ", b=" + b +
                '}';
        }
    }

    static class Cell {
        int x;
        int y;
        boolean visited = false;

        Collection<Wall> walls = new ArrayList<Wall>();

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cell)) return false;

            Cell cell = (Cell) o;

            if (x != cell.x) return false;
            //noinspection RedundantIfStatement
            if (y != cell.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
        }

        public boolean isWallUp() {
            for (Wall w : walls) {
                Cell o = w.other(this);
                if (o.x == x && o.y == y - 1) {
                    return true;
                }
            }
            return false;
        }

        public boolean isWallDown() {
            for (Wall w : walls) {
                Cell o = w.other(this);
                if (o.x == x && o.y == y + 1) {
                    return true;
                }
            }
            return false;
        }

        public boolean isWallLeft() {
            for (Wall w : walls) {
                Cell o = w.other(this);
                if (o.x == x - 1 && o.y == y) {
                    return true;
                }
            }
            return false;
        }

        public boolean isWallRight() {
            for (Wall w : walls) {
                Cell o = w.other(this);
                if (o.x == x + 1 && o.y == y) {
                    return true;
                }
            }
            return false;
        }
    }

}
