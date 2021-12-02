package com.zyx.crackgameserver.initrun;

import com.zyx.crackgameserver.modules.schedule.config.CornTaskRegister;
import com.zyx.crackgameserver.modules.schedule.model.SchedulePojo;
import com.zyx.crackgameserver.modules.schedule.service.ScheduleService;
import com.zyx.crackgameserver.modules.schedule.utils.ScheduleRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleRun implements CommandLineRunner {

   @Autowired
   private ScheduleService scheduleService;

   @Autowired
   private CornTaskRegister cornTaskRegister;



   @Async
   @Override
   public void run(String... args) throws Exception {
//       List<SchedulePojo> tasks = scheduleService.getJobStatus(1);
//       for (SchedulePojo task:
//               tasks) {
//           cornTaskRegister.addCorntask(new ScheduleRunnable(task.getBeanName(),task.getMethodName(), task.getParams()),task.getCornExpression());
//
//       }
   }

}
