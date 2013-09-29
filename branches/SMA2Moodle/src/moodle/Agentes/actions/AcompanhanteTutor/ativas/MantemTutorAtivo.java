package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;
import org.joda.time.Days;

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
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;

public class MantemTutorAtivo extends ActionMoodle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8858135525199547292L;
	private boolean done = false;
	private boolean mantemAtivo;
	
	public MantemTutorAtivo(String name) {
		super(name);
		
		idAction = 3;
	}
	
	public MantemTutorAtivo(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		
		idAction = 3;
	}

	@Override
	public void execute(Environment env, Object[] params) {
	
		if(!ControleActions.isManteTutorAtivo())
			return;
		
		System.out.println(myAgent.getLocalName()+" -- "+this.getClass());
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			List<Tutor> tutores = curso.getTutores();
			
			for(Tutor tutor : tutores){
			if(tutor == null)
				continue;
		
			try{
			BigInteger useridto = tutor.getId();
			
			podeEnviar = false;
			
			String smallmessage = "Prezado(a) "+tutor.getLastName() +", \n";
			
			smallmessage+="Na disciplina "+curso.getFullName()+", existem os seguintes fóruns onde ocorreram publicações pelos alunos e não foi constatada a sua participação nos últimos dias: \n\n\n";
			
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
					
					
						//1� verificar se o tutor participou do forum
						if(forum.isTutorParticipa()){	
							//Calcula os dias da ultima participa��o do tutor
							
							DateTime hoje = new DateTime(new Date());
							DateTime ultimaPartTutor = new DateTime(forum.getUltimaPartipacao());
							int diasPassadosPartTutor = Days.daysBetween(ultimaPartTutor, hoje).getDays();
							
							if(forum.isAvaliativo()){
								//Verifica se h� topicos ou posts no forum
								if(!forum.getAlunosComNotas().isEmpty()){
									//Data do ultimo post
									DateTime ultimoPostAluno = new DateTime(forum.getUltimoPost());
									int diaPassadosUltimoPost = Days.daysBetween(ultimoPostAluno, hoje).getDays();
									//verifica se houve partici��o do tutor desde o ultimo post feito pelos alunos 
									
									if(diasPassadosPartTutor >= diaPassadosUltimoPost && (diasPassadosPartTutor - diaPassadosUltimoPost) >= 0){
										podeEnviar = true;
										smallmessage+=forum.getName();
									}
								}
							
							}
														
						}
				}
			}
			
			smallmessage +="\n Analise as postagens dos alunos, realizando comentários, sugestões ou criticas.";
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
				ControleActions.setManteTutorAtivo(false);
			}
		}
		}
		ControleActions.setManteTutorAtivo(false);
	}
	

	public boolean done(){
		return done;
	}
}
