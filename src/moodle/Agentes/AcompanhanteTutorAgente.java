package moodle.Agentes;

import java.util.Map;

import moodle.Agentes.actions.AcompanhanteTutor.ativas.AlunosParticipantes;
import moodle.Agentes.actions.AcompanhanteTutor.ativas.InformarNovaDisciplina;
import moodle.Agentes.actions.AcompanhanteTutor.ativas.MantemForumAtivo;
import moodle.Agentes.actions.AcompanhanteTutor.ativas.MantemTutorAtivo;
import moodle.Agentes.actions.AcompanhanteTutor.ativas.NotificaCoordenadorDeTutores;
import moodle.Agentes.actions.AcompanhanteTutor.ativas.TutoresParticipantes;
import moodle.Agentes.actions.AcompanhanteTutor.comunicacao.InformarAtividadesEncerrando;
import moodle.Agentes.actions.AcompanhanteTutor.comunicacao.OrientarAlunoNotaBaixa;
import moodle.Agentes.actions.AcompanhanteTutor.comunicacao.ResponderAgentes;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jamder.Environment;
import jamder.agents.ModelAgent;
import jamder.behavioural.*;
import jamder.roles.AgentRole;
import jamder.structural.Belief;

public class AcompanhanteTutorAgente extends ModelAgent{

	private int idAgente;
	
	
	public AcompanhanteTutorAgente(String name, Environment environment,AgentRole agentRole) {
		super(name, environment, agentRole);
		
		setIdAgente(1);
				
		
		Action alunosParticipantes = new AlunosParticipantes("alunosParticipantes", null, null);
		addAction("alunosParticipantes", alunosParticipantes);
		
		Action mantemForumAtivo = new MantemForumAtivo("mantemForumAtivo", null, null);
		addAction("mantemForumAtivo", mantemForumAtivo);
		
		Action mantemTutorAtivo = new MantemTutorAtivo("mantemTutorAtivo", null, null);
		addAction("mantemTutorAtivo", mantemTutorAtivo);
		
		Action tutoresParticipantes = new TutoresParticipantes("tutoresParticipantes", null, null);
		addAction("tutoresParticipantes", tutoresParticipantes);
		
		Action informarNovaDisciplina = new InformarNovaDisciplina("informarNovaDisciplina", null, null);
		addAction("informarNovaDisciplina", informarNovaDisciplina);
		
		Action notificaCoordenadorDeTutores = new NotificaCoordenadorDeTutores("notificaCoordenadorDeTutores", null, null);
		addAction("notificaCoordenadorDeTutores", notificaCoordenadorDeTutores);
		
		
		
	}
	
	
	public void setup() {
		super.setup();
		
		
		
		
		  Map<String, Action> actions = this.getAllActions();
		 
		
		if(!actions.isEmpty()){
		
			for(Map.Entry<String, Action> results : actions.entrySet()){
				addBehaviour(results.getValue());
			}
		}
		
		
		
		MessageTemplate t_protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		MessageTemplate t_performative = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		MessageTemplate t_template = MessageTemplate.and(t_protocolo, t_performative);
		addBehaviour(new ResponderAgentes(this,t_template));
		
	}


	@Override
	protected Belief nextFunction(Belief arg0, String arg1) {
		return null;
	}
	
	@Override
	public void percept(String perception) {
		Action action = perceives.get(perception);
		if(action != null){
			addBehaviour(action);
		}
	}


	public int getIdAgente() {
		return idAgente;
	}


	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}
 	
}
