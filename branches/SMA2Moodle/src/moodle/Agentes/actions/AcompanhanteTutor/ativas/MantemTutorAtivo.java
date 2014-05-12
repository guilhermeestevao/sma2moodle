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
import dao.GerenciaCurso;
import dao.JPAUtil;
import jamder.Environment;
import jamder.behavioural.Condition;

public class MantemTutorAtivo extends ActionMoodle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8858135525199547292L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public MantemTutorAtivo(String name, BigInteger id) {
		super(name);
		idAction = 3;
		idAgente = id;
	}
	
	public MantemTutorAtivo(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 3;
		idAgente = id;
	}

	@Override
	public void execute(Environment env, Object[] params) {
	
		if(!ControleActions.isManteTutorAtivo())
			return;
		
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		//JPAUtil.beginTransaction();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
			
			System.out.println(">"+curso.getFullName());
			
			List<Tutor> tutores = curso.getTutores();
			
			for(Tutor tutor : tutores){
			if(tutor == null)
				continue;
		
			try{
			BigInteger useridto = tutor.getId();
			System.out.println("Tutor: "+tutor.getCompleteName());
			podeEnviar = false;
			
			EntityManager entManager = JPAUtil.getEntityManager();
			
			Query ss = entManager.createNamedQuery("byMensagemCustomizada");
			BigInteger ac = new BigInteger(""+this.getId_action());
			ss.setParameter(1, this.getIdAgente());
			ss.setParameter(2, ac);
			
			MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
			String smallmessage = mensC.getMensagem();
			smallmessage = smallmessage.replaceAll("<nome do tutor>", tutor.getCompleteName());
			smallmessage = smallmessage.replaceAll("<nome da disciplina>", curso.getFullName());

//			String smallmessage = "Prezado(a) "+tutor.getLastName() +", \n";
			
	//		smallmessage+="Na disciplina "+curso.getFullName()+", existem os seguintes fóruns onde ocorreram publicações pelos alunos e não foi constatada a sua participação nos últimos dias: \n\n\n";
			
			String foruns="";
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
					System.out.println("Forum: "+forum.getName());
					System.out.println("Participou ? : "+forum.isTutorParticipa());
						//1� verificar se o tutor participou do forum
						if(forum.isTutorParticipa()){	
							//Calcula os dias da ultima participa��o do tutor
							
							DateTime hoje = new DateTime(new Date());
							DateTime ultimaPartTutor = new DateTime(forum.getUltimaPartipacao());
							int diasPassadosPartTutor = Days.daysBetween(ultimaPartTutor, hoje).getDays();
							
							if(forum.isAvaliativo()){
								//Verifica se h� topicos ou posts no forum
								System.out.println("Forum avaliativo: "+forum.getName());
								if(!forum.getAlunosComNotas().isEmpty()){
									//Data do ultimo post
									System.out.println("Alunos com notas");
									DateTime ultimoPostAluno = new DateTime(forum.getUltimoPost());
									int diaPassadosUltimoPost = Days.daysBetween(ultimoPostAluno, hoje).getDays();
									//verifica se houve partici��o do tutor desde o ultimo post feito pelos alunos 
									//JOptionPane.showMessageDialog(null, "TUTOR:"+diasPassadosPartTutor);
									//JOptionPane.showMessageDialog(null, "POST:"+diaPassadosUltimoPost);
									if(diasPassadosPartTutor > diaPassadosUltimoPost && (diasPassadosPartTutor - diaPassadosUltimoPost) >3){
										podeEnviar = true;
										tutor.setContAdveretencias(tutor.getContAdveretencias()+1);
										foruns+=forum.getName()+"\n";
										System.out.println("Forum sem p: "+forum.getName());
										//smallmessage+=forum.getName();
									}
								}
							
							}
														
						}
				}
			}
			smallmessage = smallmessage.replaceAll("<nome do fórum>", foruns);
		//	smallmessage +="\n Analise as postagens dos alunos, realizando comentários, sugestões ou criticas.";
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
				fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail é uma copia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
				
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
				ControleActions.setManteTutorAtivo(false);
			}
		}
		}
		
		ControleActions.setManteTutorAtivo(false);
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
