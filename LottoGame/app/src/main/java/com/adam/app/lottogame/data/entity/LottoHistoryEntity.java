/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the entity of lotto history.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "lotto_history")
public class LottoHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "draw_id")
    private String mDrawId;

    @ColumnInfo(name = "draw_date")
    private String mDrawDate;

    @ColumnInfo(name = "numbers")
    private List<Integer> mNumbers;

    /**
     * Constructor
     */
    public LottoHistoryEntity(String drawId, String drawDate, List<Integer> numbers) {
        this.mDrawId = drawId;
        this.mDrawDate = drawDate;
        this.mNumbers = numbers;
    }

    // ---setter/getter ---
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getDrawId() {
        return mDrawId;
    }
    public void setDrawId(String drawId) {
        this.mDrawId = drawId;
    }
    public String getDrawDate() {
        return mDrawDate;
    }
    public void setDrawDate(String drawDate) {
        this.mDrawDate = drawDate;
    }
    public List<Integer> getNumbers() {
        return mNumbers;
    }
    public void setNumbers(List<Integer> numbers) {
        this.mNumbers = numbers;
    }

}
