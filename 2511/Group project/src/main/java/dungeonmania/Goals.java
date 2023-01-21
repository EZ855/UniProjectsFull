package dungeonmania;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Goals {

    private ArrayList<String> currentGoals;
    private ArrayList<String> dungeonGoals;
    private ArrayList<String> completedGoals = new ArrayList<>();

    public Goals(ArrayList<String> goals) {
        dungeonGoals = goals;
        this.currentGoals = dungeonGoals;
    }

    public static String FindGoal (JsonObject object, ArrayList<String> list) {
        String output = "";
        if (object.get("goal").getAsString().equals("AND") || object.get("goal").getAsString().equals("OR")) {
            int i = 0;
            for (JsonElement element : object.get("subgoals").getAsJsonArray()) {
                if(++i == object.get("subgoals").getAsJsonArray().size()) {
                    FindGoal(element.getAsJsonObject(), list);
                }
                else {
                    System.out.println((object.get("goal").getAsString()));
                    FindGoal(element.getAsJsonObject(), list);
                    list.add(object.get("goal").getAsString());
                }
            }
        }
        else {
            System.out.println((object.get("goal").getAsString()));
            output += ":" + object.get("goal").getAsString();
            list.add(output);
        }
        return output;
    }

    public void addGoal(String string) {
        completedGoals.add(string);
    }

    public void refreshCompleted() {
        completedGoals.removeAll(completedGoals);
    }

    public boolean checkString(String string) {
        if (completedGoals.contains(string)) {
            return true;
        }
        return false;
    }

    public boolean SingularGoalCase (ArrayList<String> list) {
        if (list.size() == 1) {
            if (checkString(list.get(0))) {
                list.remove(0);
                return true;
            }
        }
        return false;
    }

    public void RemoveCompletedANDCase (ArrayList<String> list) {
       
        if (list.size() <= 1) {
        }
        else {
            for (int i = 0; (list.size()-i) > 1; i+=2) {
                if (checkString(list.get(i))) {
                    if (list.get(i+1).equals("AND")) {
                        list.remove(i);
                        list.remove(i);
                        i-=2;
                        System.out.println("  ");
                        System.out.println(" deleting ");
                        Iterator<String> itr = list.listIterator();
                        while (itr.hasNext()) {
                            System.out.println(itr.next());
                        }
                    }
                }
            }
            if (checkString(list.get(list.size()-1))) {
                if (list.size() == 1) {
                    list.remove(list.size()-1);
                }
                else if (list.get(list.size()-2).equals("AND")) {
                    list.remove(list.size()-1);
                    list.remove(list.size()-1);
                }
                
            }
        }
        System.out.println("  ");
        System.out.println(" result ");
        Iterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
       
    }

    public Boolean RemoveCompletedORCase (ArrayList<String> list, int i) {
        if (list.size() == 1) {
            return false;
        }
        if ((list.size() - i) < 2) {
            if (list.get(i-1).equals("OR") && (checkString(list.get(i-2)) || checkString(list.get(i)))) {
                list.remove(i);
                return true;
            }
            else {
                return false;
            }
        }
        if (list.get(i+1).equals("OR")) {
            if(RemoveCompletedORCase(list, i+2) || checkString(list.get(i))) {
                list.remove(i);
                list.remove(i);
                removeallbellow(list, i);
                return true;
            }
            else {
                return false;
            }
        }
        if (list.get(i+1).equals("AND")) {
            if (RemoveCompletedORCase(list, i+2)) {
                list.remove(i+1);
            };
        }
        return false;
    }

    public static void removeallbellow(ArrayList<String> list, int i) {
        while (list.size() > i) {
            list.remove(i);
        }
    }

    // public static void check3 (ArrayList<String> list) {
    //     for (int i = 0; (list.size()-i) > 1; i+=2) {
    //         if (checkString(list.get(i))) {
    //             if (list.get(i+1).equals("OR")) {
    //                 for (int x = i; list.size() >= i; x++) {
    //                     list.remove(x-1);
    //                 }
    //             }
    //         }
    //     }
    //     if (checkString(list.get(list.size()-1))) {
    //         list.remove(list.size()-1);
    //         list.remove(list.size()-1);
    //     }
    // }

    @Override
    public String toString() {
        update();
        // Iterator<String> itr = original.listIterator();
        // while (itr.hasNext()) {
        //     System.out.println(itr.next());
        // }
        String output = "";
        int bracket = 0;
        for (int i = 0; i < currentGoals.size(); i+=2) {
            if (i == currentGoals.size()-1) {
                output += currentGoals.get(i);
            }
            else {
                output += "(";
                bracket++;
                output += currentGoals.get(i) + " " + currentGoals.get(i+1) + " ";
            }
        }
        for (int i = 0; i < bracket; i++) {
            output += ")";
        }
        
        return output;
    }

    public void update() {
        ArrayList<String> list = new ArrayList<>(dungeonGoals);
        if (list.size() > 0) {
            if (SingularGoalCase(list)) {
                currentGoals = list;
            }
            else {
                RemoveCompletedANDCase(list);
                if (list.size() >= 1) {
                    RemoveCompletedORCase(list, 0);
                }
                
            }
        }
        currentGoals = list;
    }

    public void debug() {
        Iterator<String> itr = completedGoals.listIterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    // public  void main(String[] args) {
    //     ArrayList<String> list = new ArrayList<String>();
    //     list.add("x");
    //     list.add("OR");
    //     list.add("y");
    //     list.add("OR");
    //     list.add("z");
    //     list.add("AND");
    //     list.add("a");
    //     list.add("AND");
    //     list.add("b");
    //     check2(list);
    //     check5(list, 0);
    // }

    
}
