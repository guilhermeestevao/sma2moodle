package moodle.Agentes.actions.Companheiro.comunicacao;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class InitiatorAcompanhanteTutorAgNotasBaixas extends AchieveREInitiator {
	public InitiatorAcompanhanteTutorAgNotasBaixas(Agent a, ACLMessage msg){
		super(a,msg);
	}
	
	@Override
	protected void handleAgree(ACLMessage agree) {
		
	}
	
	@Override
	protected void handleFailure(ACLMessage failure) {
		
	}
	
	@Override
	protected void handleNotUnderstood(ACLMessage notUnderstood) {
		
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		
	}

	protected void handleInform(ACLMessage inform){
		// Como deu certo, add as informa��es do Map enviado na base de dados para que nao seja enviado novamente
		
	}

}

