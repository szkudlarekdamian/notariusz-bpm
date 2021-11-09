package pl.notariusz;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class LoggingExecutionListener implements ExecutionListener {

    public void notify(DelegateExecution delegateExecution) throws Exception {
        String eventName = delegateExecution.getEventName();
        String activityName = delegateExecution.getCurrentActivityName();
        System.out.printf("Event: [%s] [%s]%n", eventName, activityName);
    }
}
