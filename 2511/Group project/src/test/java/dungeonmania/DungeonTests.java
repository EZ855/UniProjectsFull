package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import dungeonmania.response.models.DungeonResponse;

import static dungeonmania.TestUtils.*;

import dungeonmania.util.Direction;

public class DungeonTests {
    @Test
    @DisplayName("Testing a map with 4 conjunction goal")
    public void andAll() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        
        DungeonResponse res = dmc.newGame("exit_goal_order", "c_complexGoalsTest_andAll");
        assertTrue(getGoals(res).equals("(:boulders AND (:exit AND :treasure))"));

        res = dmc.newGame("advanced", "c_complexGoalsTest_andAll");
        assertTrue(getGoals(res).equals("(:enemies AND :treasure)"));
    }

    public static void main(String[] args) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("exit_goal_order", "c_complexGoalsTest_andAll");
        System.out.println(getGoals(res));

        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("OR");list.add("b");list.add("AND");list.add("c");list.add("AND");list.add("d");list.add("OR");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("e");
        goals.update();
        assertTrue(goals.toString().equals("(b and (c and (d and e)))"));
    }

    @Test
    public void singleGoal() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        Goals goals = new Goals(list);
        goals.addGoal("a");
        goals.update();
        assertTrue(goals.toString().equals(""));
    }

    @Test
    public void goalAllAND1() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("AND");list.add("b");list.add("AND");list.add("c");list.add("AND");list.add("d");list.add("AND");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("a");
        goals.update();
        assertTrue(goals.toString().equals("(b AND (c AND (d AND e)))"));
    }

    @Test
    public void goalAllAND2() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("AND");list.add("b");list.add("AND");list.add("c");list.add("AND");list.add("d");list.add("AND");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("e");
        goals.update();
        assertTrue(goals.toString().equals("(a AND (b AND (c AND d)))"));
    }

    @Test
    public void goalAllOR1() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("OR");list.add("b");list.add("OR");list.add("c");list.add("OR");list.add("d");list.add("OR");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("a");
        goals.update();
        assertTrue(goals.toString().equals(""));
    }

    @Test
    public void goalAllOR2() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("OR");list.add("b");list.add("OR");list.add("c");list.add("OR");list.add("d");list.add("OR");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("e");
        goals.update();
        assertTrue(goals.toString().equals(""));
    }

    @Test
    public void goalRandom1() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("AND");list.add("b");list.add("AND");list.add("c");list.add("OR");list.add("d");list.add("OR");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("b");
        goals.update();
        assertTrue(goals.toString().equals("(a AND (c OR (d OR e)))"));
    }

    @Test
    public void goalRandom2() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("AND");list.add("b");list.add("AND");list.add("c");list.add("OR");list.add("d");list.add("OR");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("e");
        goals.update();
        assertTrue(goals.toString().equals("(a AND b)"));
    }

    @Test
    public void goalRandom3() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("OR");list.add("b");list.add("AND");list.add("c");list.add("AND");list.add("d");list.add("OR");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("d");
        goals.update();
        assertTrue(goals.toString().equals("(a OR (b AND c))"));
    }

    @Test
    public void goalRandom4() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("OR");list.add("b");list.add("OR");list.add("c");list.add("AND");list.add("d");list.add("AND");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("b");
        goals.update();
        assertTrue(goals.toString().equals(""));
    }

    @Test
    public void goalRandom5() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");list.add("AND");list.add("b");list.add("OR");list.add("c");list.add("OR");list.add("d");list.add("AND");list.add("e");
        Goals goals = new Goals(list);
        goals.addGoal("b");
        goals.update();
        assertTrue(goals.toString().equals("a"));
    }
}


