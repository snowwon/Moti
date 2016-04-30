package net.zoo9.moti.model;

import java.util.Date;

/**
 * Created by sheldon on 16. 4. 13..
 */
public class Board {
    public int boardId;
    public String userName;
    public Integer stickerSize;
    public String listOfGoals;
    public String prize;
    public int stickerPos;
    public String endDate;
    public String startDate;

    public String toString() {
        return "board: "+boardId+" :::: "+userName+","+prize+","+listOfGoals+","+stickerPos+"/"+stickerSize;
    }
}
