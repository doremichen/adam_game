/*
 * Copyright (c) 2026 Adam Chen
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

package com.adam.app.arenaminifight;

import com.adam.app.arenaminifight.data.local.ScoreEntity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test for ScoreEntity data model (UT-02).
 * Trace: UC-MENU-04, REQ-MENU-04
 */
public class ScoreEntityUnitTest {

    @Test
    public void testScoreEntityCreation() {
        long timestamp = System.currentTimeMillis();
        ScoreEntity entity = new ScoreEntity("Adam", 120L, 15, 5, timestamp);

        assertEquals("Player name should match", "Adam", entity.getPlayerName());
        assertEquals("Survival time should match", 120L, entity.getSurvivalTime());
        assertEquals("Kill count should match", 15, entity.getKillCount());
        assertEquals("Level should match", 5, entity.getLevel());
        assertEquals("Timestamp should match", timestamp, entity.getTimestamp());
    }
}
