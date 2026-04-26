import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void playerStartsAlive() {
        Player p = new Player();
        assertTrue(p.isAlive());
    }

    @Test
    void playerStartsWithEmptyHand() {
        Player p = new Player();
        assertEquals(0, p.getHandSize());
    }
}