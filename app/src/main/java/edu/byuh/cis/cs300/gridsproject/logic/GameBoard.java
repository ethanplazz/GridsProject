package edu.byuh.cis.cs300.gridsproject.logic;

public class GameBoard {

    private Player[][] grid;
    private final int DIM = 5;
    private Player whoseTurnIsIt;

    public GameBoard() {
        //Create a 5x5 gameboard of BLANK cells
        grid = new Player[DIM][DIM];
        for (int i=0; i<DIM; ++i) {
            for (int j=0; j<DIM; ++j) {
                grid[i][j] = Player.BLANK;
            }
        }

        //Arbitrarily, we make X the first player.
        whoseTurnIsIt = Player.X;
    }

    public void submitMove(char move, Player p) {
        if (move >= '1' && move <= '5') {
            //vertical move, move tokens down
            int col = (int)(move - '1');
            Player newVal = p;
            for (int i=0; i<DIM; ++i) {
                if (grid[i][col] == Player.BLANK) {
                    grid[i][col] = newVal;
                    break;
                } else {
                    Player tmp = grid[i][col];
                    grid[i][col] = newVal;
                    newVal = tmp;
                }
            }

        } else { //A-E
            //horizontal move, move tokens right
            int row = (int)(move - 'A');
            Player newVal = p;
            for (int i=0; i<DIM; ++i) {
                if (grid[row][i] == Player.BLANK) {
                    grid[row][i] = newVal;
                    break;
                } else {
                    Player tmp = grid[row][i];
                    grid[row][i] = newVal;
                    newVal = tmp;
                }
            }
        }

        //change whose turn it is
        if (whoseTurnIsIt == Player.X) {
            whoseTurnIsIt = Player.O;
        } else {
            whoseTurnIsIt = Player.X;
        }
    }

    public Player checkForWin() {
        Player winner = Player.BLANK;
        boolean xWinner = false;
        boolean oWinner = false;

        //check all rows
        for (int i=0; i<DIM; ++i) {
            if (grid[i][0] != Player.BLANK) {
                winner = grid[i][0];
                boolean rowWin = true;
                for (int j=0; j<DIM; ++j) {
                    if (grid[i][j] != winner) {
                        rowWin = false;
                        break;
                    }
                }
                if (rowWin) {
                    if (winner == Player.X) {
                        xWinner = true;
                    }
                    else if (winner == Player.O) {
                        oWinner = true;
                    }
                }
            }
        }

        //check all columns
        for (int i=0; i<DIM; ++i) {
            if (grid[0][i] != Player.BLANK) {
                winner = grid[0][i];
                boolean columnWin = true;
                for (int j=0; j<DIM; ++j) {
                    if (grid[j][i] != winner) {
                        columnWin = false;
                        break;
                    }
                }
                if (columnWin) {
                    if (winner == Player.X) {
                        xWinner = true;
                    }
                    else if (winner == Player.O) {
                        oWinner = true;
                    }
                }
            }
        }

        //check top-left -> bottom-right diagonal
        if (grid[0][0] != Player.BLANK) {
            winner = grid[0][0];
            boolean diagonalWin = true;
            for (int i=0; i<DIM; ++i) {
                if (grid[i][i] != winner) {
                    diagonalWin = false;
                    break;
                }
            }
            if (diagonalWin) {
                if (winner == Player.X) {
                    xWinner = true;
                }
                else if (winner == Player.O) {
                    oWinner = true;
                }
            }
        }

        //check bottom-left -> top-right diagonal
        if (grid[DIM-1][0] != Player.BLANK) {
            winner = grid[DIM-1][0];
            boolean diagonalWin = true;
            for (int i=0; i<DIM; ++i) {
                if (grid[DIM-1-i][i] != winner) {
                    diagonalWin = false;
                    break;
                }
            }
            if (diagonalWin) {
                if (winner == Player.X) {
                    xWinner = true;
                }
                else if (winner == Player.O) {
                    oWinner = true;
                }
            }
        }

        if (xWinner && oWinner) {
            return Player.BOTH;
        }
        else if (xWinner) {
            return Player.X;
        }
        else if (oWinner) {
            return Player.O;
        }
        else {
            return Player.BLANK;
        }
    }

    public void consoleDraw() {
        System.out.print("  ");
        for (int i=0; i<DIM; ++i) {
            System.out.print(i+1);
        }
        System.out.println();
        System.out.print(" /");
        for (int i=0; i<DIM; ++i) {
            System.out.print("-");
        }
        System.out.println("\\");
        for (int i=0; i<DIM; ++i) {
            System.out.print(((char)('A'+i)) + "|");
            for (int j=0; j<DIM; ++j) {
                if (grid[i][j] == Player.BLANK) {
                    System.out.print(" ");
                } else {
                    System.out.print(grid[i][j]);
                }
            }
            System.out.println("|");
        }
        System.out.print(" \\");
        for (int i=0; i<DIM; ++i) {
            System.out.print("-");
        }
        System.out.println("/");

    }

    public Player getCurrentPlayer() {
        return whoseTurnIsIt;
    }
}
