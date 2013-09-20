package moodle.Agentes.actions.Companheiro.comunicacao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import dao.GerenciaCurso;

import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jamder.Environment;
import jamder.agents.GenericAgent;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class ComunicarAcompanhanteTutorAgAtividadesEncerrando extends Action{

	private boolean done = false;
	private boolean mantemAtivo;
	
	public ComunicarAcompanhanteTutorAgAtividadesEncerrando(String name) {
		super(name);
		
	}
	
	public ComunicarAcompanhanteTutorAgAtividadesEncerrando(String name,
			Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		MoodleEnv envir = (MoodleEnv) env;
		GenericAgent agent = (GenericAgent) myAgent;
		
		
		Map<Aluno, List<Atividade>> atividadesEncerrando = envir.getAtividadesEncerrando();
		
		mantemAtivo = envir.getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		
		GerenciaCurso manager = envir.getGerenciaCurso();		
		
		List<Aluno> todosOsAlunos = new ArrayList<Aluno>();
		List<Atividade> todasAsAtividades = new ArrayList<Atividade>();
		
		
		
		for (Curso c : manager.getCursos()){
			
			Set<Aluno> alunos = c.getAlunos();
			
			for (Aluno aluno : alunos) {
				if(!todosOsAlunos.contains(aluno))
					todosOsAlunos.add(aluno);
			}
			
			for(Atividade atividade : c.getAllAtividades()){
				if(!todasAsAtividades.contains(atividade))
					todasAsAtividades.add(atividade);
			}
				
		}
		
		for (Aluno aluno : todosOsAlunos) {
			
			List<Atividade> atividadesSemParticipacao = new ArrayList<Atividade>();
			
			for(Atividade ats : todasAsAtividades){
				
				
				if(ats instanceof AtividadeNota){
					AtividadeNota an = (AtividadeNota) ats;
					
					if(MoodleEnv.verificarData(an.getDataFinal(), 3))
						if(!an.getAlunosComNotas().containsKey(aluno))
							atividadesSemParticipacao.add(an);
							
				}else{
					
					AtividadeParticipacao ap = (AtividadeParticipacao) ats;
					
					if(ap.isAvaliativo() && MoodleEnv.verificarData(ap.getDataFinal(), 3))
						if(ap.getAlunosComNotas().containsKey(aluno))
							atividadesSemParticipacao.add(ap);
					
				}
					
				
			}
			
			if(!atividadesSemParticipacao.isEmpty())
				envir.addAtividadeEncerrando(aluno, atividadesSemParticipacao);
			
		}
		
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.addReceiver(new AID("AcompanhanteTutorAg", AID.ISLOCALNAME));
			msg.setConversationId("AtividadesEncerrando");
			msg.setContentObject((Serializable)envir.getAtividadesEncerrando());
			myAgent.addBehaviour(new InitiatorAcompanhanteTutorAgAtividadeEncerrando(myAgent, msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		done = true;
		
	}
	
	@Override
	public boolean done() {
		
		return done;
	}
	
	private int difDias(Date data) {
		long dataEntrada = data.getTime();
		long dataAtual = new Date().getTime();
		long dif = dataEntrada-dataAtual;
		int dias = (int) (dif/86400000);
		return dias;
	}

}
