/*
 * Copyright (C) 2026 Adam Game
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
package com.adam.app.snake.domain.bridge;

import com.adam.app.snake.domain.repository.ILeaderboardRepository;
import com.adam.app.snake.domain.repository.ISettingRepository;
import com.adam.app.snake.domain.usecase.LeaderboardUseCase;
import com.adam.app.snake.domain.usecase.SettingUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Domain bridge module for Hilt
 */
@Module
@InstallIn(SingletonComponent.class)
public class DomainBridgeModule {

    /**
     * Provide leaderboard use case
     * @param repository ILeaderboardRepository
     * @return LeaderboardUseCase
     */
    @Provides
    @Singleton
    public LeaderboardUseCase provideLeaderboardUseCase(ILeaderboardRepository repository) {
        return new LeaderboardUseCase(repository);
    }

    /**
     * Provide setting use case
     * @param repository ISettingRepository
     * @return SettingUseCase
     */
    @Provides
    @Singleton
    public SettingUseCase provideSettingUseCase(ISettingRepository repository) {
        return new SettingUseCase(repository);
    }
}
