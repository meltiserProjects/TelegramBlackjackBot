package ru.grigorev.telegram.blackjack.gameLogic;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int[] cards = {
            2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 11,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 11,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 11,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 11};
    private ArrayList<Integer> hand = new ArrayList<>();

    public Player() {
        initHand();
        replaceAceCardIfBusted();
    }

    public void initHand() {
        int card1 = new Random().nextInt(cards.length);
        int card2 = new Random().nextInt(cards.length);
        hand.add(cards[card1]);
        hand.add(cards[card2]);
    }

    public int getSumOfHand() {
        int sum = 0;
        for (Integer integer : hand) {
            sum += integer;
        }
        return sum;
    }

    public boolean isBustedHand() {
        return getSumOfHand() > 21;
    }

    public void replaceAceCardIfBusted() {
        if (isBustedHand()) {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i) == 11) hand.set(i, 1);
            }
        }
    }

    public void appendCardInHand() {
        int randomCard = new Random().nextInt(cards.length);
        hand.add(cards[randomCard]);
    }

    public ArrayList<Integer> getHand() {
        return hand;
    }

    public String getStringHand() {
        StringBuilder sb = new StringBuilder("Your cards are: ");
        for (Integer integer : hand) {
            sb.append(integer).append(" ");
        }
        sb.append("\nSum is: ").append(getSumOfHand());
        return sb.toString();
    }
}
