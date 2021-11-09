package pl.notariusz;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ZawiadomienieCallback implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) throws Exception {
        RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Zawiadomienie Koniec")
                .processInstanceId(delegateExecution.getVariable("parentBusinessKey").toString())
                .setVariable("zgoda", delegateExecution.getVariable("zgoda"))
                .correlateWithResult();
    }
}
