package com.oforsky.mmm.timer;

import java.util.Date;

public interface TimerService {

    void schedule(MmmTimerTask task, int delaySec) throws Exception;

    void schedule(MmmTimerTask task, Date fireTime) throws Exception;

    void cancel(Integer taskId) throws Exception;

}
