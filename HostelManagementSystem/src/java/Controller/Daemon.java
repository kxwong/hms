package Controller;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class Daemon implements ServletContextListener {

    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println(new Date().toLocaleString());
            }
        };

        long delay = 0;
        long period = 1 * 1000; // unit * ms
        timer.schedule(myTask, delay, period);
    }

}
