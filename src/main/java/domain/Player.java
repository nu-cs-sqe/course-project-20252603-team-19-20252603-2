package domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private boolean alive;
    private final List<CardType> hand;

    public Player(String name) {
        this.name = name;
        this.alive = true;
        this.hand = new ArrayList<>();
    }

    public String getName()          { return name; }
    public boolean isAlive()         { return alive; }
    public List<CardType> getHand()  { return hand; }
}