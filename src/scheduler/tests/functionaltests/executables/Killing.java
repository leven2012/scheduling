package functionaltests.executables;

import java.io.Serializable;

import org.ow2.proactive.scheduler.common.task.TaskResult;
import org.ow2.proactive.scheduler.common.task.executable.JavaExecutable;


public class Killing extends JavaExecutable {

    @Override
    public Serializable execute(TaskResult... results) throws Throwable {
        System.exit(1);
        return "Killing";
    }

}