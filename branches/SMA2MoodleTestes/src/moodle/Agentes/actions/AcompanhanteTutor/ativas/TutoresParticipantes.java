package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
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
		
		block(21 * 1000L);
		
		mantemAtivo = ((MoodleEnv)env).getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		List<Forum> forunsSemTotor = new ArrayList<Forum>();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			Tutor tutor = curso.getTutor();
			
			if(tutor == null)
				continue;
			
			if(verificaControle(curso.getId(), tutor.getId()))
				continue;
			
			
			
			
			try{
			BigInteger useridto = tutor.getId();
			
			podeEnviar = false;
			
			forunsSemTotor.clear();
			
			String smallmessage = "Prezado "+tutor.getCompleteName() +", \n";
			
			smallmessage+="Na disciplina "+curso.getFullName()+", existem os seguintes f�runs: \n\n";
			
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
						//Adiciona o nome dos furuns em q o tutor n�o participou
						if(!forum.isTutorParticipa()){
					
							podeEnviar = true;
	
							smallmessage += "> " +forum.getName()+"\n";
							
						}
				}
		
			}
			
			smallmessage +="\n\nOnde sua participa��o n�o foi encontrada. ";
		
			smallmessage +="� necessario que voc� participe para incentivar os alunos desse curso";
		
			if(podeEnviar){
				
				AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
				AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), tutor.getId(), curso.getId());
				
				smallmessage +="\n";
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
				
			}
		}
	}
	
	public boolean done(){
		return done;
	}

}
