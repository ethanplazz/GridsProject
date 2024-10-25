package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs300.gridsproject.logic.Player;

public class GuiToken implements TickListener {

    private Bitmap ex; // Image for 'X' token
    private Bitmap oh; // Image for 'O' token
    private final RectF bounds;
    private final Player player;
    private final PointF velocity; // Use if needed for velocity, keep for future scalability
    private int stepsTaken; // Track the number of steps taken
    private static final int MAX_STEPS = 15; // Maximum steps before stopping
    private GridPosition position;

    public GuiToken(Resources res, char label, float left, float top, float width, float height, int imageResIdX, int imageResIdO) {
        this.bounds = new RectF(left, top, left + width, top + height);
        this.ex = BitmapFactory.decodeResource(res, imageResIdX);
        this.ex = Bitmap.createScaledBitmap(ex, (int) width, (int) height, true);
        this.oh = BitmapFactory.decodeResource(res, imageResIdO);
        this.oh = Bitmap.createScaledBitmap(oh, (int) width, (int) height, true);
        this.velocity = new PointF(0, 0); // Initialize velocity to zero
        this.stepsTaken = 0; // Initialize steps taken
        this.player = label == 'X' ? Player.X : Player.O;
        this.position = new GridPosition('A', '1'); // Default position or could be set later
    }

    // Set velocity
    public void setVelocity(float vx, float vy) {
        this.velocity.set(vx, vy);
    }

    // Method to draw the token on the canvas
    public void draw(Canvas canvas) {
        if (player == Player.X) {
            canvas.drawBitmap(ex, null, bounds, null);
        } else {
            canvas.drawBitmap(oh, null, bounds, null);
        }
    }

    public RectF getBounds() {
        return bounds;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAtDestination() {
        return stepsTaken >= MAX_STEPS; // Checks if maximum steps have been reached
    }

    public void move() {
        if (isAtDestination()) {
            return; // Stop movement
        }

        // Convert position characters to grid indices
        int colIndex = position.column - 'A'; // Convert column character to index
        int rowIndex = position.row - '1';    // Convert row character to index

        // Calculate the target position using grid cell sizes and offsets
        float cellSize = bounds.width(); // Assuming square cells
        float targetX = colIndex * cellSize + cellSize / 2; // Center the token in the cell
        float targetY = rowIndex * cellSize + cellSize / 2; // Center the token in the cell

        // Move the token's bounds according to the calculated velocity
        bounds.offset(velocity.x, velocity.y);
        stepsTaken++;

        // Check if token has reached (or nearly reached) target destination
        if (Math.abs(bounds.centerX() - targetX) < 5 && Math.abs(bounds.centerY() - targetY) < 5) {
            bounds.offsetTo(targetX - bounds.width() / 2, targetY - bounds.height() / 2); // Snap to target
            velocity.set(0, 0);
            stepsTaken = 0; // Reset steps for future moves
        }
    }

    @Override
    public void onTick() {
        move();
    }

    public void setPosition(char row, char column) {
        this.position = new GridPosition(row, column); // Instantiate and assign GridPosition
    }

    public GridPosition getPosition() {
        return position;
    }

    // Inner class GridPosition
    public static class GridPosition {
        public char row;    // Row of the grid
        public char column; // Column of the grid

        // Constructor to initialize row and column
        public GridPosition(char row, char column) {
            this.row = row;
            this.column = column;
        }
    }
}

