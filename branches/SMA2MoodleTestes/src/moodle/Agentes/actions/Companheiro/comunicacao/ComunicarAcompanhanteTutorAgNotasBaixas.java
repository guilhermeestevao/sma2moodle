package moodle.Agentes.actions.Companheiro.comunicacao;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.GerenciaCurso;


import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jamder.Environment;
import jamder.agents.GenericAgent;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class ComunicarAcompanhanteTutorAgNotasBaixas extends Action {

	private boolean done = false;
	private boolean mantemAtivo;// duvida
	public ComunicarAcompanhanteTutorAgNotasBaixas(String name) {
		super(name);
	}
	
	public ComunicarAcompanhanteTutorAgNotasBaixas(String name, Condition pre_condition,
			Condition pos_condition){
		super(name, pre_condition, pos_condition);
	}
	
	@Override
	public void execute(Environment env, Object[] params){

		
		MoodleEnv envir = (MoodleEnv) env;
		GenericAgent agent = (GenericAgent) myAgent;
		
		
		mantemAtivo = envir.getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		
		Map<Curso, List<Aluno>> alunosNotaBaixa = envir.getAlunosNotaBaixa();
		
		try{
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.addReceiver(new AID("AcompanhanteTutorAg", AID.ISLOCALNAME));
			msg.setConversationId("AlunosNotasBaixas");
			msg.setContentObject((Serializable)alunosNotaBaixa);
			myAgent.addBehaviour(new InitiatorAcompanhanteTutorAgNotasBaixas(myAgent,msg));
		
		}catch(IOException e){
			System.err.println(e);
			e.printStackTrace();
		}
		
		done = true;
	}

	public boolean done(){
		return done;
	}
}
