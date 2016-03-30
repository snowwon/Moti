package net.zoo9.moti;

import java.util.Date;

/**
 * Created by Sheldon on 2016. 3. 29..
 */
public class Sticker {
    public Date checkedDate = null;

    public Sticker(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    public Sticker() {
        this.checkedDate = null;
    }
}
