import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    int mineCount = 0;
    
    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;
    String difficulty = "";

    // Difficulty selection frame
    JFrame difficultyFrame = new JFrame("Select Difficulty");
    
    public Minesweeper() {
        showDifficultySelection();
    }

    void showDifficultySelection() {
        difficultyFrame.setSize(400, 300);
        difficultyFrame.setLocationRelativeTo(null);
        difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        difficultyFrame.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Select Difficulty", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Easy button
        JButton easyButton = createDifficultyButton("Easy", "10-14 mines", Color.GREEN);
        easyButton.addActionListener(e -> {
            difficulty = "easy";
            mineCount = random.nextInt(5) + 10; // 10-14 mines
            startGame();
            difficultyFrame.dispose();
        });
        
        // Medium button
        JButton mediumButton = createDifficultyButton("Medium", "15-19 mines", Color.ORANGE);
        mediumButton.addActionListener(e -> {
            difficulty = "medium";
            mineCount = random.nextInt(5) + 15; // 15-19 mines
            startGame();
            difficultyFrame.dispose();
        });
        
        // Hard button
        JButton hardButton = createDifficultyButton("Hard", "20-24 mines", Color.RED);
        hardButton.addActionListener(e -> {
            difficulty = "hard";
            mineCount = random.nextInt(5) + 20; // 20-24 mines
            startGame();
            difficultyFrame.dispose();
        });
        
        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);
        
        difficultyFrame.add(titleLabel, BorderLayout.NORTH);
        difficultyFrame.add(buttonPanel, BorderLayout.CENTER);
        difficultyFrame.setVisible(true);
    }
    
    JButton createDifficultyButton(String title, String description, Color color) {
        JButton button = new JButton("<html><center><b>" + title + "</b><br>" + description + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(200, 80));
        return button;
    }

    void startGame() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper - " + difficulty + ": " + mineCount + " mines");
        textLabel.setOpaque(true);
        textLabel.setBackground(Color.LIGHT_GRAY);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);

        // Create restart button
        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setBackground(Color.YELLOW);
        restartButton.addActionListener(e -> restartGame());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(restartButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
            
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        //left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText() == "") {
                                if (mineList.contains(tile)) {
                                    revealMines();
                                }
                                else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        }
                        //right click
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText() == "" && tile.isEnabled()) {
                                tile.setText("🚩");
                            }
                            else if (tile.getText() == "🚩") {
                                tile.setText("");
                            }
                        }
                    } 
                });

                boardPanel.add(tile);
            }
        }

        frame.setVisible(true);
        setMines();
    }

    void setMines() {
        mineList = new ArrayList<MineTile>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c]; 
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft -= 1;
            }
        }
    }

    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        textLabel.setText("Game Over! " + mineCount + " mines");
    }

    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        //top 3
        minesFound += countMine(r-1, c-1);
        minesFound += countMine(r-1, c);
        minesFound += countMine(r-1, c+1);

        //left and right
        minesFound += countMine(r, c-1);
        minesFound += countMine(r, c+1);

        //bottom 3
        minesFound += countMine(r+1, c-1);
        minesFound += countMine(r+1, c);
        minesFound += countMine(r+1, c+1);

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");
            
            // Recursively check surrounding tiles
            checkMine(r-1, c-1);
            checkMine(r-1, c);
            checkMine(r-1, c+1);
            checkMine(r, c-1);
            checkMine(r, c+1);
            checkMine(r+1, c-1);
            checkMine(r+1, c);
            checkMine(r+1, c+1);
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }
    }

    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }

    void restartGame() {
        frame.dispose();
        new Minesweeper();
    }

   
}