package net.zoo9.moti.model;

import java.util.Date;

/**
 * Created by sheldon on 16. 4. 13..
 */
public class Board {
    public int boardId;
    public String userName;
    public Integer stickerSize = new Integer(0);
    public String listOfGoals;
    public String prize;
    public int stickerPos; // 최종 스티커 위치를 나타내는 값. 아무것도 없으면 0, 1개 붙었으면 1.
    public String endDate;
    public String startDate;

    public String toString() {
        return "board: "+boardId+" :::: "+userName+","+prize+","+listOfGoals+","+stickerPos+"/"+stickerSize;
    }
}
