package net.zoo9.moti.model;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class Goal {
    public int id;
    public String goal_desc;
    public boolean is_default;

    public Goal(int id, String goal_desc, boolean is_default){
        this.id = id;
        this.goal_desc = goal_desc;
        this.is_default = is_default;
    }

    public Goal() {}
}
