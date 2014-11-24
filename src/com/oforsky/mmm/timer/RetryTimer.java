package com.oforsky.mmm.timer;

import com.oforsky.mmm.ebo.MmmConstant;
import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.data.ModuleStateEnum;
import com.truetel.jcore.data.Schedule;
import com.truetel.jcore.part.JcoreTimer;
import com.truetel.jcore.part.PartFactory;
import com.truetel.jcore.part.ScheduleTimerTask;
import org.apache.log4j.Logger;

import java.util.Date;

public class RetryTimer implements TimerService {

    private static RetryTimer instance = new RetryTimer();

    private Logger log = Logger.getLogger(RetryTimer.class);

    private JcoreTimer timer;

    private RetryTimer() {
    }

    public static RetryTimer get() {
        return instance;
    }

    @Override
    public void schedule(MmmTimerTask task, int delaySec) throws Exception {
        log.debug("schedule: taskId=[" + task.getTaskId() + ", delaySec=["
                + delaySec + "]");
        Date date = new Date(System.currentTimeMillis() + delaySec * 1000);
        MmmPart.getRetryTimerTimer().schedule(task.getTaskId(), task, date);
    }

    @Override
    public void schedule(MmmTimerTask task, Date fireTime) throws Exception {
        log.debug("schedule: taskId=[" + task.getTaskId() + ", fireTime=["
                + fireTime + "]");
        MmmPart.getRetryTimerTimer().schedule(task.getTaskId(), task, fireTime);
    }

    @Override
    public void cancel(Integer taskOid) throws Exception {
        MmmPart.getRetryTimerTimer().cancelTask("taskOid[" + taskOid + "]");
    }

}
