/**
 * Copyright (c) 2026 LottoGame
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.adam.app.lottogame.data.entity;

import androidx.annotation.NonNull;
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
    private int mDrawId;

    @ColumnInfo(name = "draw_date")
    private String mDrawDate;

    @ColumnInfo(name = "numbers")
    private List<Integer> mNumbers;

    /**
     * Constructor
     */
    public LottoHistoryEntity(int drawId, String drawDate, List<Integer> numbers) {
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

    public int getDrawId() {
        return mDrawId;
    }
    public void setDrawId(int drawId) {
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

    /**
     * toString
     */
    @NonNull
    @Override
    public String toString() {
        return "LottoHistoryEntity{" + "\n" +
                "mId=" + mId + "\n" +
                ", mDrawId=" + mDrawId + "\n" +
                ", mDrawDate='" + mDrawDate + '\'' + "\n" +
                ", mNumbers=" + mNumbers + "\n" +
                '}';
    }

}
