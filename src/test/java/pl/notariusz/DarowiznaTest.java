package pl.notariusz;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.TaskServiceImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.RequiredHistoryLevel;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;

@RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_AUDIT)
public class DarowiznaTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources = {"Darowizna.bpmn"})
    public void zadluzenieTest() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("darowizna_imie", "Marian")
                .putValue("darowizna_nazwisko", "Kordoba")
                .putValue("darowizna_rodzina", true)
                .putValue("darowizna_wartosc", 10000)
                .putValue("darowizna_rodzaj", "zegarek")
                .putValue("darowizna_zadluzenie", true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Przekazanie_darowizny", variablesIn);

        assertThat(processInstance).isEnded();
    }
    
    @Test
    @Deployment(resources = {"Darowizna.bpmn"})
    public void wartoscTest() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("darowizna_imie", "Marian")
                .putValue("darowizna_nazwisko", "Kordoba")
                .putValue("darowizna_rodzina", true)
                .putValue("darowizna_wartosc", -10000)
                .putValue("darowizna_rodzaj", "zegarek")
                .putValue("darowizna_zadluzenie", false);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Przekazanie_darowizny", variablesIn);

        assertThat(processInstance).isEnded();
    }
    
    @Test
    @Deployment(resources = {"Finalizowanie.bpmn"})
    public void podpisTest() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("imie", "Marian")
                .putValue("nazwisko", "Kordoba")
                .putValue("podpis", "Mariusz Korduba");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Finalizowanie", variablesIn);

        assertThat(processInstance).isEnded();
    }

    @Test
    @Deployment(resources = {"Finalizowanie.bpmn"})
    public void podpisTest2() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("imie", "Marian")
                .putValue("nazwisko", "Kordoba")
                .putValue("podpis", "Marian Kordoba")
                .putValue("darowizna_rodzaj", "zegarek");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Finalizowanie", variablesIn);

        assertThat(processInstance).isEnded();
    }
    
    @Test
    @Deployment(resources = {"Zawiadomienie.bpmn"})
    public void zgodaTest() throws InterruptedException {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("podatek", 3000.0)
                .putValue("prowizja", 150.0);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Zawiadomienie", variablesIn);
        assertThat(processInstance).isWaitingAt("Telefon");
        Thread.sleep(5000);
        assertThat(processInstance).isNotWaitingAt("Telefon");
        assertThat(processInstance).isWaitingAt("Decyzja");
        
      TaskService taskService = new TaskServiceImpl();
      Map<String,Object> variables = new HashMap<String, Object>();
      variables.put("zgoda", false);
      taskService.complete("Zawiadomienie", variables);
      assertThat(processInstance).isEnded();       

    }
    
//    @Test
//    @Deployment(resources = {"Darowizna.bpmn"})
//    public void podatekObliczony() {
//        RuntimeService runtimeService = processEngineRule.getRuntimeService();
//        HistoryService historyService = processEngineRule.getHistoryService();
//        VariableMap variablesIn = Variables.createVariables()
//                .putValue("darowizna_imie", "Marian")
//                .putValue("darowizna_nazwisko", "Kordoba")
//                .putValue("darowizna_rodzina", true)
//                .putValue("darowizna_wartosc", 10000.0)
//                .putValue("darowizna_rodzaj", "zegarek")
//                .putValue("darowizna_zadluzenie", false);
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Przekazanie_darowizny", variablesIn);
//        assertThat(processInstance).isNotEnded();       
//        TaskService taskService = new TaskServiceImpl();
//        Map<String,Object> variables = new HashMap<String, Object>();
//        variables.put("zgoda", false);
//        taskService.complete("Przekazanie_darowizny", variables);
//    }
}
