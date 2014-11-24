package com.oforsky.mmm.timer;

import com.truetel.jcore.data.Schedule;
import com.truetel.jcore.part.ScheduleTimerTask;

import java.util.Date;

public interface TimerService {

    void schedule(MmmTimerTask task, int delaySec) throws Exception;

    void schedule(MmmTimerTask task, Date fireTime) throws Exception;

    void cancel(Integer taskId) throws Exception;

}
