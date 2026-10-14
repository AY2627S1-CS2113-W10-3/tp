package seedu.duke.ratings;

public class Rating {
    private final String stall;
    private final String food;
    private final float score;
    

    public Rating(String stall, String food, float score) {
        if (stall == null || stall.isBlank()) {
            throw new IllegalArgumentException("Stall name cannot be blank.");
        }
        if (food == null || food.isBlank()) {
            throw new IllegalArgumentException("Food name cannot be blank.");
        }
        if (Float.isNaN(score) || score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10.");
        }

        this.stall = stall.trim();
        this.food = food.trim();
        this.score = score;
    }

    public String getStall() {
        return stall;
    }

    public String getFood() {
        return food;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("Stall: %s, Food: %s, Score: %.1f", stall, food, score);
    }

    public String toStorageString() {
        return String.format("%s|%s|%.1f", stall, food, score);
    }
}
