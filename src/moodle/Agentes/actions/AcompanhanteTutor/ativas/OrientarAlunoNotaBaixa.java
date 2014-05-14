package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import dao.JPAUtil;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class OrientarAlunoNotaBaixa extends Action {
	
	private boolean done = false;
	private boolean mantemAtivo;
	private Map<Curso, List<Aluno>> alunosNotaBaixa;
	private BigInteger idAgente;
	private int idAction; 
	
	public OrientarAlunoNotaBaixa(String name, BigInteger id) {
		super(name);
		idAction = 34;
	}
	
	public OrientarAlunoNotaBaixa(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 34;
		idAgente = id;
	}
	
	public OrientarAlunoNotaBaixa(String name, Map<Curso, List<Aluno>> aluno){
		super(name);
		alunosNotaBaixa = aluno;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		MoodleEnv envir = (MoodleEnv) env;

		
		/*
		 * Guilherme, está mandando mensagem para o tutor mesmo nao havendo aluno para informar.
		 * 
		 */
		
		Long time = System.currentTimeMillis();
		
		BigInteger useridfrom = new BigInteger("2");
		
		
			for(Map.Entry<Curso, List<Aluno>> results : alunosNotaBaixa.entrySet()){
				
				if(results.getKey().getTutores().isEmpty())
					continue;
				
				if(results.getValue().isEmpty())
					continue;
			
				List<Tutor> tutores = results.getKey().getTutores();
				
			for(Tutor tutor : tutores){
				EntityManager entManager = JPAUtil.getEntityManager();
				Query ss = entManager.createNamedQuery("byMensagemCustomizada");
				BigInteger ac = new BigInteger(""+getIdAction());
				ss.setParameter(1, this.getIdAgente());
				ss.setParameter(2, ac);
				
				MensagemCustomizada mensC = (MensagemCustomizada)ss.getResultList().get(0);
				String smallmessage =  mensC.getMensagem();
				
				smallmessage = smallmessage.replaceAll("<nome do tutor>", tutor.getCompleteName());
				smallmessage = smallmessage.replaceAll("<nome da disciplina>", results.getKey().getFullName());
				
				BigInteger useridto = new BigInteger(tutor.getId().toString());		
				
				
				String als ="";
				for(Aluno al : results.getValue()){
					als+=al.getCompleteName()+"\n";
				}
				smallmessage = smallmessage.replaceAll("<nome do aluno>", als);
				
				
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
