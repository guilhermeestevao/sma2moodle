package moodle.Agentes.actions.Companheiro.comunicacao;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class InitiatorAcompanhanteTutorAgAtividadeEncerrando extends AchieveREInitiator{
	
	public InitiatorAcompanhanteTutorAgAtividadeEncerrando(Agent a, ACLMessage msg){
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
		
	}

}
