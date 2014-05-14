package moodle.Agentes;

import java.math.BigInteger;
import java.util.Map;

import moodle.Agentes.actions.AcompanhanteDeProfessores.InformarNotasAtrasadas;
import jamder.Environment;
import jamder.agents.ReflexAgent;
import jamder.behavioural.Action;
import jamder.roles.AgentRole;

public class AcompanhanteDeProfessores extends ReflexAgent{

	private int idAgente;
	
	public AcompanhanteDeProfessores(String name, Environment environment,
			AgentRole agentRole) {
		super(name, environment, agentRole);
		setIdAgente(8);
		System.out.println("Agente Teste chamado");
		
		Action informarNotasAtrasadas = new InformarNotasAtrasadas("informarNotasAtrasadas", null, null, new BigInteger(""+idAgente));
		//addAction("informarNotasAtrasadas", informarNotasAtrasadas);
		
		
	}
	
	@Override
	protected void setup() {
		super.setup();
		
		Map<String, Action> actions = this.getAllActions();
		if(!actions.isEmpty()){
			for(Map.Entry<String, Action> results : actions.entrySet()){
				addBehaviour(results.getValue());
			}
		}
	}
	
	public int getIdAgente() {
		return idAgente;
	}


	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}


}
