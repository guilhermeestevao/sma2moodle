 package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
















import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import org.joda.time.DateTime;
import org.joda.time.Days;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.atividades.Forum;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import jamder.Environment;
import jamder.behavioural.Condition;

public class MantemForumAtivo extends ActionMoodle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -668154302162160035L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public MantemForumAtivo(String name, BigInteger id) {
		super(name);
		idAction = 3;
		idAgente = id;
	}
	
	public MantemForumAtivo(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 3;
		idAgente = id;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		

		if(!ControleActions.isMantemForumAtivo())
			return;
			
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		
		
		
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		 
		
		for(Curso curso : cursos){
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
			
			System.out.println(">"+curso.getFullName());
			
			List<Tutor> tutores = curso.getTutores();
			for(Tutor tutor : tutores){
				
			if(tutor == null || tutor.getId() == null)
				continue;
			System.out.println("Tutor: "+tutor.getCompleteName() +" ID "+tutor.getId());
			
			//JOptionPane.showMessageDialog(null,tutor.getFirstName());
			
			try{
			BigInteger useridto = tutor.getId();
			
			EntityManager entManager = JPAUtil.getEntityManager();
			
			podeEnviar = false;
		
			Query ss = entManager.createNamedQuery("byMensagemCustomizada");
			BigInteger ac = new BigInteger(""+this.getId_action());
			ss.setParameter(1, this.getIdAgente());
			ss.setParameter(2, ac);
			
			MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
			String smallmessage = mensC.getMensagem();
			smallmessage = smallmessage.replaceAll("<nome tutor>", tutor.getCompleteName());
			smallmessage = smallmessage.replaceAll("<nome disciplina>", curso.getFullName());
			
			//String smallmessage = "Prezado(a) "+tutor.getCompleteName() +". \n";
			
			//smallmessage+="Na disciplina "+curso.getFullName()+", existe(m) o(s) seguinte(s) fórum(s): \n\n";
			String foruns ="";
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
					
					System.out.println("Fórum: "+forum.getName());
					System.out.println("Participa ? : "+forum.isTutorParticipa());
						//1� verificar se o tutor participou do forum
						if(forum.isTutorParticipa()){
							
							//Cacula o periodo em dias da �ltima participa��o de alunos
							DateTime hoje = new DateTime(new Date());
							DateTime ultimoPost = new DateTime(forum.getUltimoPost());
							int diasDoUltimoPost = Days.daysBetween(ultimoPost, hoje).getDays();	
							
							//Caso tenha passado mais de dois dias � liberado o envio da mensagem
							if(forum.isAvaliativo() && diasDoUltimoPost >= 0){
								podeEnviar = true;
								foruns+=forum.getName();
								//smallmessage+=forum.getName()+"\n";
							}
							
						}
				}
		
			}
			smallmessage = smallmessage.replaceAll("<nome forum>", foruns);
		
			//smallmessage +="\nnão foram encontradas publicações dos alunos nos últimos dias. Motive os alunos para que continuem participando desses fóruns, pois são avaliativos.";
		
			if(podeEnviar){
				
				//Timestamp atual = new Timestamp(System.currentTimeMillis());
				//AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
				//AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), tutor.getId(), curso.getId(),atual);
				
				smallmessage +="\n";
				
				AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
				if(verificaMens(curso.getId(), tutor.getId(), smallmessage))
					continue;
				else{
					Timestamp atual = new Timestamp(System.currentTimeMillis());
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), tutor.getId(), curso.getId(),atual,smallmessage);
				}
				
				String fullmessage = smallmessage;
				fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para voc� em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
				
				Long time = System.currentTimeMillis();
				
				Mensagem msg = new Mensagem();
				msg.setSubject("Nova mensagem do Administrador");
				msg.setUseridfrom(useridfrom);
				msg.setUseridto(useridto);
				msg.setSmallmessage(smallmessage);
				msg.setFullmessage(fullmessage);
				msg.setTimecreated(time);
				
				//((MoodleEnv)env).addMensagem(msg);

				ControleEnvio.enviar(msg, env, idAction);
				
			}
			}catch(NullPointerException e){
				JPAUtil.closeEntityManager(this.getClass());
				ControleActions.setMantemForumAtivo(false);
			}
			
		}
		}
		JPAUtil.closeEntityManager(this.getClass());
		ControleActions.setMantemForumAtivo(false);
	}
	
	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
	
	public boolean done(){
		return done;
	}

}
