import java.util.Arrays;

public class GameLogic {
    private int[] probabilities;
    private int scoreToChase;
    private int balls;
    private int wickets;
    private int totalScore;
    private Batter batter;

    public GameLogic(int scoreToChase, int balls, int wickets) {
        this.scoreToChase = scoreToChase;
        this.balls = balls;
        this.wickets = wickets;
        this.totalScore = 0;
        this.batter = new Batter();
        this.probabilities = new int[]{batter.zero, batter.one, batter.two, batter.four, batter.six, batter.out};
    }

    private int[] probabilityArray(int a) {
        int[] arr = new int[100];
        Arrays.fill(arr, 0);
        for (int i = 0; i < a; i++) {
            arr[i] = 1;
        }
        return arr;
    }

    private boolean yesOrNo(int[] a) {
        int randomVal = (int) (Math.random() * 100);
        return a[randomVal] == 1;
    }

    public String playBall(int choice) {
        if (balls <= 0 || wickets <= 0 || scoreToChase <= 0) {
            return "Game Over.";
        }

        int[] scoreProb = probabilityArray(probabilities[getIndex(choice)]);
        int[] outProb = probabilityArray(probabilities[5]);

        StringBuilder result = new StringBuilder();
        if (yesOrNo(outProb)) {
            wickets--;
            balls--;
            result.append("OUT!\n");
            resetProbabilities();
        } else {
            if (yesOrNo(scoreProb)) {
                totalScore += choice;
                scoreToChase -= choice;
                balls--;
                result.append(choice).append(" SCORED\n");
                adjustProbabilities(choice);
            } else {
                balls--;
                result.append("BALL MISSED\n");
            }
        }

        if (scoreToChase <= 0) {
            result.append("YOU WON THE MATCH\n");
        } else if (balls == 0 || wickets == 0) {
            result.append("YOU LOST THE MATCH\n");
        }

        return result.toString();
    }

    private int getIndex(int score) {
        switch (score) {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 4: return 3;
            case 6: return 4;
            default: return -1;
        }
    }

    private void resetProbabilities() {
        this.probabilities = new int[]{batter.zero, batter.one, batter.two, batter.four, batter.six, batter.out};
    }

    private void adjustProbabilities(int score) {
        switch (score) {
            case 0:
                probabilities[0] += 5;
                probabilities[1] += 5;
                probabilities[2] += 5;
                probabilities[3] += 10;
                probabilities[4] += 10;
                probabilities[5] -= 10;
                break;
            case 1:
                probabilities[1] += 5;
                probabilities[2] += 5;
                probabilities[3] += 5;
                probabilities[4] += 10;
                probabilities[5] -= 5;
                break;
            case 2:
                probabilities[1] += 5;
                probabilities[2] += 5;
                probabilities[3] += 5;
                probabilities[4] += 5;
                break;
            case 4:
                probabilities[0] += 5;
                probabilities[1] += 5;
                probabilities[3] -= 10;
                probabilities[4] -= 5;
                probabilities[5] += 10;
                break;
            case 6:
                probabilities[0] += 5;
                probabilities[1] += 5;
                probabilities[3] -= 5;
                probabilities[4] -= 15;
                probabilities[5] += 15;
                break;
        }

        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] < 0) probabilities[i] = 0;
            if (probabilities[i] > 100) probabilities[i] = 100;
        }
    }

    public String getStatus() {
        if (balls <= 0 || wickets <= 0 || scoreToChase <= 0) {
            return ""; // Return an empty string if the game is over
        }
        return "Target to Chase: " + scoreToChase + ", Balls: " + balls + ", Wickets: " + wickets;
    }

    public String getProbs(){
        if (balls <= 0 || wickets <= 0 || scoreToChase <= 0) {
            return ""; // Return an empty string if the game is over
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(String.format("%-11s%-11s%-11s%-11s%-9s%-8s\n", " 0", "1", "2", "4", "6", "OUT"));
        sb.append(String.format("%-6s%-8s%-8s%-8s%-8s%-8s\n",
                probabilities[0] + "%",
                probabilities[1] + "%",
                probabilities[2] + "%",
                probabilities[3] + "%",
                probabilities[4] + "%",
                probabilities[5] + "%"));
        sb.append("\n");

        return sb.toString();
    }
}
