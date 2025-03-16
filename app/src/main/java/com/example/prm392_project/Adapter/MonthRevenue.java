package com.example.prm392_project.Adapter;

import androidx.room.ColumnInfo;

public class MonthRevenue {
        @ColumnInfo(name = "month")
        public int month;
        @ColumnInfo(name = "revenue")
        public float revenue;
}
