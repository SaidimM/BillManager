package com.example.background.Database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database( version = DBFlowDataBase.VERSION, name = DBFlowDataBase.NAME)
public class DBFlowDataBase {
    //数据库名称
    public static final String NAME = "CashBook";
    //数据库版本
    public static final int VERSION = 1;
}