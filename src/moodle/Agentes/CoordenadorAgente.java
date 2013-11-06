package moodle.Agentes;
import jamder.behavioural.*;
import jamder.Environment;
import jamder.roles.AgentRole;
import jamder.structural.*;
import java.util.List;
import jamder.agents.*;

public class CoordenadorAgente extends MASMLAgent {
   
	private int idAgente;
	
	//Constructor 
   public CoordenadorAgente (String name, Environment env, AgentRole agRole) {
     super(name, env, agRole);
     
     idAgente = 5;
     
     addBelief("crencasCoordenador.pl", new Belief("crencasCoordenador.pl", "String", ""));
     Goal requisitarAcoesDosAgentesG = new LeafGoal("requisitarAcoesDosAgentesG", "Boolean", "false");
     addGoal("requisitarAcoesDosAgentesG", requisitarAcoesDosAgentesG);
   
     Action verificaAcaoDoAgenteAc = new Action("verificaAcaoDoAgenteAc", null, null);
     addAction("verificaAcaoDoAgenteAc", verificaAcaoDoAgenteAc);
     Action requisitaAcaoAc = new Action("requisitaAcaoAc", null, null);
     addAction("requisitaAcaoAc", requisitaAcaoAc);
   
     Plan requisitarAcaoPlan = new Plan("requisitarAcaoPlan", requisitarAcoesDosAgentesG);
     addPlan("requisitarAcaoPlan", requisitarAcaoPlan); 

     requisitarAcaoPlan.addAction("verificaAcaoDoAgenteAc", verificaAcaoDoAgenteAc);
     requisitarAcaoPlan.addAction("requisitaAcaoAc", requisitaAcaoAc);
   }


}

