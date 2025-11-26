/**
 * Copyright (C) 2025 Adam. All rights reserved.
 *
 * This class is the factory of lotto history.
 *
 * @Author: Adam Chen
 * @Date: 2025-11-26
 */
package com.adam.app.lottogame.data;

import com.adam.app.lottogame.data.entity.LottoHistoryEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class LottoHistoryFactory {

    private LottoHistoryFactory() {
        // avoid to instantiate
    }

    public static LottoHistoryEntity create(int drawnId, List<Integer> numbers) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);
        return new LottoHistoryEntity(String.valueOf(drawnId), dateString, numbers);
    }


}
