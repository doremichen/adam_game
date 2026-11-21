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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.adam.app.arenaminifight.domain.gateway.IGameGateway;
import com.adam.app.arenaminifight.ui.game.GameViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit Test for GameViewModel.
 * Trace: UT-01, UC-GAME-12
 */
public class GameViewModelUnitTest {

    @Rule
    public TestRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private IGameGateway mGatewayMock;

    private GameViewModel mViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mViewModel = new GameViewModel(mGatewayMock);
    }

    @Test
    public void testInitialPlayerStatus() {
        assertNotNull("Player status LiveData should not be null", mViewModel.getPlayerStatus());
        assertEquals("Initial status should be empty", "", mViewModel.getPlayerStatus().getValue());
    }
}
