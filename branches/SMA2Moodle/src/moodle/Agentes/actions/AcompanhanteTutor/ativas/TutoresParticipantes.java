package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;
import jamder.Environment;
import jamder.behavioural.Condition;
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

//CODIFICA��O

public class TutoresParticipantes extends ActionMoodle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042947606582526164L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public TutoresParticipantes(String name, BigInteger id) {
		super(name);
		idAction = 5;
		idAgente = id;
	}

	public TutoresParticipantes(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 5;
		idAgente = id;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		

		if(!ControleActions.isTutoresPArticipantes())
			return;
		
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		boolean podeEnviar = false;
		
		
		//JPAUtil.beginTransaction();
		
		List<Forum> forunsSemTotor = new ArrayList<Forum>();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
			
			List<Tutor> tutores = curso.getTutores();
			
			for(Tutor tutor: tutores){
			
			if(tutor == null)
				continue;
		
			
			
			try{
			BigInteger useridto = tutor.getId();
			
			podeEnviar = false;
			
			forunsSemTotor.clear();
			
			EntityManager entManager = JPAUtil.getEntityManager();
			
			Query ss = entManager.createNamedQuery("byMensagemCustomizada");
			BigInteger ac = new BigInteger(""+this.getId_action());
			ss.setParameter(1, this.getIdAgente());
			ss.setParameter(2, ac);
			
			MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
			String smallmessage = mensC.getMensagem();
			smallmessage = smallmessage.replaceAll("<nome do tutor>", tutor.getCompleteName());
			smallmessage = smallmessage.replaceAll("<nome da disciplina>", curso.getFullName());
			
			//String smallmessage = "Prezado(a) "+tutor.getCompleteName() +". \n";
			
			//smallmessage+="Na disciplina "+curso.getFullName()+",  existe(m) o(s) seguinte(s) fórum(s) onde sua participação não foi identificada: \n  \n\n";
			String foruns="";
			for(AtividadeParticipacao atividade : curso.getAtividadesParticipacao()){
			
				if(atividade instanceof Forum){
				
					Forum forum = (Forum) atividade ;
						//Adiciona o nome dos furuns em q o tutor n�o participou
						if(!forum.isTutorParticipa()){
					
							podeEnviar = true;
							foruns+=forum.getName()+"\n";
							//smallmessage += "> " +forum.getName()+"\n";
						}
				}
		
			}
			smallmessage = smallmessage.replaceAll("<nome do fórum>", foruns);
			//smallmessage +="\n  É necessário que você participe para motivar a interação entre os alunos \n";

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
				
				//((MoodleEnv)env).addMensagem(msg);
				
				ControleEnvio.enviar(msg, env, idAction);
				
			}
			
			}catch(NullPointerException e){
			//	JPAUtil.closeEntityManager();
				ControleActions.setTutoresPArticipantes(false);
			}
			}
		}

		//JPAUtil.closeEntityManager();
		ControleActions.setTutoresPArticipantes(false);
		
	}
	
	public boolean done(){
		return done;
	}

	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}

}
