package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;






import javax.swing.JOptionPane;

import org.joda.time.DateTime;
import org.joda.time.Days;

import dao.GerenciaCurso;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.atividades.Forum;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Condition;

public class MantemForumAtivo extends ActionMoodle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -668154302162160035L;
	private boolean done = false;
	private boolean mantemAtivo;

	public MantemForumAtivo(String name) {
		super(name);
		
		idAction = 3;
	}
	
	public MantemForumAtivo(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
	
		idAction = 3;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		

		if(!ControleActions.isMantemForumAtivo())
			return;
			
		System.out.println(myAgent.getLocalName()+" -- "+this.getClass());
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		 
		
		for(Curso curso : cursos){
			
			List<Tutor> tutores = curso.getTutores();
			for(Tutor tutor : tutores){
				
			if(tutor == null || tutor.getId() == null)
				continue;
			
			
			//JOptionPane.showMessageDialog(null,tutor.getFirstName());
			
			try{
			BigInteger useridto = tutor.getId();
			
			podeEnviar = false;
		
			String smallmessage = "Prezado(a) "+tutor.getCompleteName() +". \n";
			
			smallmessage+="Na disciplina "+curso.getFullName()+", existe(m) o(s) seguinte(s) fórum(s): \n\n";
			
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
						//1� verificar se o tutor participou do forum
						if(forum.isTutorParticipa()){
							
							//Cacula o periodo em dias da �ltima participa��o de alunos
							DateTime hoje = new DateTime(new Date());
							DateTime ultimoPost = new DateTime(forum.getUltimoPost());
							int diasDoUltimoPost = Days.daysBetween(ultimoPost, hoje).getDays();	
							
							//Caso tenha passado mais de dois dias � liberado o envio da mensagem
							if(forum.isAvaliativo() && diasDoUltimoPost >= 0){
								podeEnviar = true;
								smallmessage+=forum.getName()+"\n";
							}
							
						}
				}
		
			}
			
			smallmessage +="\nnão foram encontradas publicações dos alunos nos últimos dias. Motive os alunos para que continuem participando desses fóruns, pois são avaliativos.";
		
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
				
				((MoodleEnv)env).addMensagem(msg);
			}
			}catch(NullPointerException e){
				ControleActions.setMantemForumAtivo(false);
			}
			
		}
		}
		ControleActions.setMantemForumAtivo(false);
	}
	
	
	public boolean done(){
		return done;
	}

}
