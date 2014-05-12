package moodle.Agentes.actions.AcompanhanteTutor.comunicacao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import dao.JPAUtil;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class InformarAtividadesEncerrando extends Action {

	private boolean done = false;
	private Map<Aluno,List<Atividade>> atividadesEncerrando;
	private BigInteger idAgente;
	private int idAction; 
	
	public InformarAtividadesEncerrando(String name, BigInteger id) {
		super(name);
		idAgente = id;
		idAction = 30;
	}
	
	public InformarAtividadesEncerrando(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAgente = id;
		idAction = 30;
	}
	
	public InformarAtividadesEncerrando(String name, Map<Aluno, List<Atividade>> als) {
		super(name);
		atividadesEncerrando = als; 
	}
	
	@Override
	public void execute(Environment env, Object[] params) {

		MoodleEnv envir = (MoodleEnv) env;
		
		List<Curso> cursos = envir.getGerenciaCurso().getCursos();
		Map<Aluno, List<Atividade>> atividades = atividadesEncerrando;
		
		for(Curso curso : cursos){
		
			if(curso.getTutores().isEmpty())
				continue;
			
			
		for(Tutor tutor : curso.getTutores()){
			
			EntityManager entManager = JPAUtil.getEntityManager();
			Query ss = entManager.createNamedQuery("byMensagemCustomizada");
			BigInteger ac = new BigInteger(""+getIdAction());
			ss.setParameter(1, this.getIdAgente());
			ss.setParameter(2, ac);
			
			MensagemCustomizada mensC = (MensagemCustomizada)ss.getResultList().get(0);
			String smallmessage =  mensC.getMensagem();
		
			smallmessage = smallmessage.replaceAll("<nome do tutor>", tutor.getCompleteName());
			smallmessage = smallmessage.replaceAll("<nome da disciplina>", curso.getFullName());
			
			String alusativs="";
			for(Map.Entry<Aluno, List<Atividade>> results : atividades.entrySet()){
				
				if(!curso.getAlunos().contains(results.getKey()))
					continue;
				
				if(results.getValue().isEmpty())
					continue;
				alusativs+=results.getKey().getCompleteName()+"\n";
				
				for(Atividade at : results.getValue()){
					
					if((!curso.getAtividadesNota().contains(at)) && (!curso.getAtividadesParticipacao().contains(at)))
						continue;
					
					alusativs+=at.getName()+"\n";
				}
			
			  }
			
				smallmessage = smallmessage.replaceAll("<nome do aluno - atividades>", alusativs);
				
				Long time = System.currentTimeMillis();
				BigInteger useridfrom = new BigInteger("2");
				BigInteger useridto = new BigInteger(tutor.getId().toString());
				
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
	
	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
	public String retornaMensagem(List<MensagemCustomizada> mensagens, String tipo){
		String ativ="";
		
		for(int i=0;i<mensagens.size();i++){	
			if(mensagens.get(i).getDestinatario().equals(tipo)){	
				//ativ = mensagens.get(i).getMensagem();
			}
		}
		return ativ;
	}
	
	@Override
	public boolean done() {
		return done;
	}

	public int getIdAction() {
		return idAction;
	}

	public void setIdAction(int idAction) {
		this.idAction = idAction;
	}

	
}
