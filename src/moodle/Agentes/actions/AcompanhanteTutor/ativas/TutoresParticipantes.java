package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;
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

//CODIFICA��O

public class TutoresParticipantes extends ActionMoodle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042947606582526164L;
	private boolean done = false;
	private boolean mantemAtivo;
	
	public TutoresParticipantes(String name) {
		super(name);
		idAction = 5;
	}

	public TutoresParticipantes(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 5;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		

		if(!ControleActions.isTutoresPArticipantes())
			return;
		
		System.out.println(myAgent.getLocalName()+" -- "+this.getClass());
		
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		List<Forum> forunsSemTotor = new ArrayList<Forum>();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			List<Tutor> tutores = curso.getTutores();
			
			for(Tutor tutor: tutores){
			
			if(tutor == null)
				continue;
		
			
			
			try{
			BigInteger useridto = tutor.getId();
			
			podeEnviar = false;
			
			forunsSemTotor.clear();
			
			String smallmessage = "Prezado(a) "+tutor.getCompleteName() +". \n";
			
			smallmessage+="Na disciplina "+curso.getFullName()+",  existe(m) o(s) seguinte(s) fórum(s) onde sua participação não foi identificada: \n  \n\n";
			
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
						//Adiciona o nome dos furuns em q o tutor n�o participou
						if(!forum.isTutorParticipa() && forum.isAvaliativo()){
					
							podeEnviar = true;
	
							smallmessage += "> " +forum.getName()+"\n";
							
						}
				}
		
			}
	
			smallmessage +="\n  É necessário que você participe para motivar a interação entre os alunos \n";

				smallmessage +="\n";
				
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
				ControleActions.setTutoresPArticipantes(false);
			}
			}
		}
		ControleActions.setTutoresPArticipantes(false);
	}
	
	public boolean done(){
		return done;
	}

}
