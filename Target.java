import java.util.Scanner;

public class Target {
    public int[] setTarget(String difficulty) {
        Scanner input = new Scanner(System.in);
        difficulty = difficulty.toLowerCase();
        int[] score = new int[3]; // [targetScore, balls, wickets]
        score[1] = 30; // default balls
        score[2] = 3;  // default wickets

        switch (difficulty) {
            case "easy":
                score[0] = 28;
                break;
            case "medium":
                score[0] = 51;
                break;
            case "hard":
                score[0] = 71;
                break;
            case "custom":
                System.out.print("Enter Your Target: ");
                int target = Integer.parseInt(input.nextLine());

                System.out.print("Enter Number of Balls: ");
                int balls = Integer.parseInt(input.nextLine());

                System.out.print("Enter Number of Wickets: ");
                int wickets = Integer.parseInt(input.nextLine());

                score[0] = target;
                score[1] = balls;
                score[2] = wickets;
                break;
            default:
                score[0] = -1; // invalid difficulty
        }

        return score;
    }
}