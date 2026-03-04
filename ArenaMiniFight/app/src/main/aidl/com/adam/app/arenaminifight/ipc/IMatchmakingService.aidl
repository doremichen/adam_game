// IMatchmakingService.aidl
package com.adam.app.arenaminifight.ipc;

import com.adam.app.arenaminifight.ipc.model.PlayerInfo;
import com.adam.app.arenaminifight.ipc.model.MatchResult;

interface IMatchmakingService {
    void joinQueue(in PlayerInfo info);
    void cancelQueue();
    MatchResult getMatchResult();
}