package com.example.background.module;

import com.example.background.Database.DBFlowDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


@Table(database = DBFlowDataBase.class)
public class Orders extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String time;

    @Column
    public String dealer;

    @Column
    public String name;

    @Column
    public float cash;

    @Column
    public int type;

    public Orders() {
    }

    public Orders(String time, String dealer, String name, float cash, int type) {
        this.time = time;
        this.dealer = dealer;
        this.name = name;
        this.cash = cash;
        this.type = type;
    }
}
