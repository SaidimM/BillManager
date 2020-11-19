package com.example.background.module;

import com.example.background.Database.DBFlowDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = DBFlowDataBase.class)
public class User extends BaseModel {

    @PrimaryKey(autoincrement = true) //主键  //autoincrement 开启自增
    public int id;

    @Column               //表示一栏 一列
    public  String name; //自己需要存储的字段

    @Column
    public String pass;     //密码

    @Column
    public String portrait;     //头像

}
