package pl.notariusz;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class ZawiadomienieRequest implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) throws Exception {
        RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("parentBusinessKey", delegateExecution.getProcessInstanceId());
        if(delegateExecution.hasVariable("podatek")) {
        	processVariables.put("podatek",
        			delegateExecution.getVariable("podatek").toString());
        }
        else {
        	processVariables.put("podatek",0);
        }
        processVariables.put("prowizja",delegateExecution.getVariable("prowizja").toString());
        runtimeService.startProcessInstanceByMessage("Zawiadomienie Start", processVariables);
    }
}
