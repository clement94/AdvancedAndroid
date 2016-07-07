package com.cberthelot.advancedandoid.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by cberthelot on 07/07/2016.
 */
@DatabaseTable
public class SimpleData {

    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(index = true)
    String name;
    @DatabaseField
    Date date;

    public SimpleData() {
    }

    public SimpleData(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return "SimpleData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
