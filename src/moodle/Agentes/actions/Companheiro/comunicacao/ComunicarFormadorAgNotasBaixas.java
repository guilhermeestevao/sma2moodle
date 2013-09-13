package moodle.Agentes.actions.Companheiro.comunicacao;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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

public class ComunicarFormadorAgNotasBaixas extends Action{

	private boolean done = false;
	private boolean mantemAtivo;
	
	
	public ComunicarFormadorAgNotasBaixas(String name) {
		super(name);
		
	}
	
	public ComunicarFormadorAgNotasBaixas(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);

	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		
		
		MoodleEnv envir = (MoodleEnv) env;
		GenericAgent agent = (GenericAgent) myAgent;
		
		
		
		
		mantemAtivo = envir.getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		
		Map<Curso, List<Aluno>> alunosNotaBaixa = envir.getAlunosNotaBaixa();
		
			try {
				
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
				msg.addReceiver(new AID("FormadorAg", AID.ISLOCALNAME));
				msg.setConversationId("AlunosNotaBaixa");
				msg.setContentObject((Serializable)alunosNotaBaixa);
				myAgent.addBehaviour(new InitiatorFormadorAgNotasBaixas(myAgent, msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		
			
		
		
		done = true;
		
		
}
	
	@Override
	public boolean done() {
		return done;
	}
}
