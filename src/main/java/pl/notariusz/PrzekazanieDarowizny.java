package pl.notariusz;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

@ProcessApplication("Darowizna")
public class PrzekazanieDarowizny extends ServletProcessApplication {

    @PostDeploy
    public void evaluateDecisionTable(ProcessEngine processEngine) {
        DecisionService decisionService = processEngine.getDecisionService();
        VariableMap variables = Variables.createVariables().
        		putValue("darowizna_rodzaj", "samochód").
        		putValue("darowizna_wartosc", 15000.0);
        DmnDecisionTableResult decisionResult = decisionService.evaluateDecisionTableByKey("Podatek", variables);
	    DmnDecisionRuleResult sr = decisionResult.getSingleResult();
	    
	   
	    if (sr.containsKey("podatek")) {
	    
	    	Double podatek = sr.getEntry("podatek");
	    	System.out.println("Rodzaj darowizny: " + variables.getValue("darowizna_rodzaj", String.class));
	    	System.out.println("Wartość darowizny: " + variables.getValue("darowizna_wartosc", Double.class));
	    	System.out.println("Wyliczony podatek: " + podatek);
	    }
    }
}
