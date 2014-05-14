package moodle.Agentes.actions.Formador.ativas;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jamder.Environment;
import jamder.agents.GenericAgent;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import dao.JPAUtil;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;

public class OrientarAlunoNotaBaixa extends Action {

	private boolean done = false;
	private boolean mantemAtivo;
	private Map<Curso, List<Aluno>> alunosNotaBaixa;
	private BigInteger idAgente;
	private int idAction; 
	
	public OrientarAlunoNotaBaixa(String name, BigInteger id) {
		super(name);
		idAgente = id;
		idAction = 35;
	}
	
	public OrientarAlunoNotaBaixa(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAgente = id;
		idAction = 35;
	}
	
	public OrientarAlunoNotaBaixa(String name, Map<Curso, List<Aluno>> als) {
		super(name);
		alunosNotaBaixa = als; 
		
	}
	
	
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		MoodleEnv envir = (MoodleEnv) env;
		System.out.println("Action Orientar chamada");
		
		
			for(Map.Entry<Curso, List<Aluno>> results : alunosNotaBaixa.entrySet()){
				
				if(results.getValue().isEmpty())
					continue;
				
				
				for(Aluno al : results.getValue()){
					
					EntityManager entManager = JPAUtil.getEntityManager();	
					Query ss = entManager.createNamedQuery("byMensagemCustomizada");
					BigInteger ac = new BigInteger(""+getIdAction());
					ss.setParameter(1, this.getIdAgente());
					ss.setParameter(2, ac);
					MensagemCustomizada mensC = (MensagemCustomizada)ss.getResultList().get(0);
					String smallmessage =  mensC.getMensagem();
					
					smallmessage = smallmessage.replaceAll("<nome do aluno>", al.getCompleteName());
					smallmessage = smallmessage.replaceAll("<nome da disciplina>", results.getKey().getFullName());
					
					Long time = System.currentTimeMillis();
					
					BigInteger useridfrom = new BigInteger("2");
					BigInteger useridto = new BigInteger(al.getId().toString());
					
					StringBuilder fullmessage = new StringBuilder(smallmessage);
					fullmessage.append("\n--------------------------------------------------------------------- \nEste e-mail é a cópia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder.");
		
					
					Mensagem msg = new Mensagem();
					msg.setSubject("Nova mensagem do Administrador");
					msg.setUseridfrom(useridfrom);
					msg.setUseridto(useridto);
					msg.setSmallmessage(smallmessage.toString());
					msg.setFullmessage(fullmessage.toString());
					msg.setTimecreated(time);
					
					envir.addMensagem(msg);
		
				}
							
				
			}
	
		
		
		
		done = true;
		 
	}
	
	@Override
	public boolean done() {
		return done;
	}

	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}

	public int getIdAction() {
		return idAction;
	}

	public void setIdAction(int idAction) {
		this.idAction = idAction;
	}
	
	
	
}
