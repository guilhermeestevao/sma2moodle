package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.service.spi.Stoppable;

import Util.SalvarLog;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
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


public class AlunosParticipantes extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8359505679128722723L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;

	public AlunosParticipantes(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		
		idAction = 1;
		idAgente = id;
	}

	public AlunosParticipantes(String name, BigInteger id) {
		super(name);
		idAction = 1;
		idAgente = id;
	}

	@Override
	public void execute(Environment env, Object[] params) {

		
		if(!ControleActions.isAlunosParticipantes())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		
		
		//JPAUtil.beginTransaction();
		
		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {

			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
			
			System.out.println(">"+curso.getFullName());
			
			List<Tutor> tutores = curso.getTutores();
			
			
			if(tutores.isEmpty())
				continue;
			
			for(Tutor tutor : tutores){
				System.out.println("Tutor -> "+tutor.getCompleteName());
			EntityManager entManager = JPAUtil.getEntityManager();
				
			Set<Aluno> alunos = curso.getAlunos();
			try {
				BigInteger useridto = tutor.getId();

				podeEnviar = false;

				Query ss = entManager.createNamedQuery("byMensagemCustomizada");
				BigInteger ac = new BigInteger(""+this.getId_action());
				ss.setParameter(1, this.getIdAgente());
				ss.setParameter(2, ac);
				MensagemCustomizada mensC = (MensagemCustomizada)ss.getResultList().get(0);
				String smallmessage =  mensC.getMensagem();
				
				smallmessage = smallmessage.replaceAll("<nome do tutor>", tutor.getCompleteName());
				smallmessage = smallmessage.replaceAll("<nome da disciplina>", curso.getFullName());
				
				//String smallmessage = "Prezado(a) " + tutor.getCompleteName()+ ". \n";

				//smallmessage += "Na disciplina " + curso.getFullName()+ ", existem os seguintes fóruns: \n\n";
				String foruns="";
				for (AtividadeParticipacao atividade : curso
						.getAtividadesParticipacao()) {

					if (atividade instanceof Forum) {
						Forum forum = (Forum) atividade;
						// Foruns n�o avaliativos
						System.out.println("Fórum: "+forum.getName());
						if (forum.isAvaliativo()) {
							System.out.println("Fórum avaliativo: "+forum.getName());
							Map<Aluno, BigDecimal> alunosComNota = forum
									.getAlunosComNotas();
							// caso faltem at� dois dias para o fim da avalia��o
							
							if (MoodleEnv.verificarData(forum.getDataFinal(), 3)) {
								if(alunosComNota.keySet().size()==alunos.size()){
									System.out.println("Não pode enviar");
									podeEnviar = false;	
								}else{
									System.out.println("pode enviar");
									podeEnviar = true;
								}
								System.out.println("Menos de 3 dias");
								foruns+=forum.getName()+":\n";
								//smallmessage += "> " + forum.getName();
								//smallmessage += "\nOnde o(s) aluno(s): \n";
								String alunosForum ="";
								for (Aluno aluno : alunos) {
									System.out.println("Aluno -> "+aluno.getCompleteName());
									if (!alunosComNota.containsKey(aluno)) {
										System.out.println("Aluno N Nota -> "+aluno.getCompleteName());
										alunosForum+=aluno.getCompleteName(); 
										//smallmessage += aluno.getCompleteName()+ "\n";
									}
								}
								foruns+=alunosForum;
							}
						}
					}
				}
				smallmessage = smallmessage.replaceAll("<nome do fórum - alunos do fórum>",foruns);
			//	smallmessage += "\nnão possue(m) nenhuma participação no(s) respectivo(s) fórum(ns) ou não receberam suas devidas notas referentes a postagens realizadas. \n";
				//smallmessage += "É necessário que você incentive e acompanhe a participação desse(s) aluno(s) nos respectivos fórum(s).";

				
				JPAUtil.closeEntityManager();
				
					if (podeEnviar) {
						//Timestamp atual = new Timestamp(System.currentTimeMillis());
						//AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
						//AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), tutor.getId(), curso.getId(),atual);
						
						smallmessage += "\n";
						
						AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
						if(verificaMens(curso.getId(), tutor.getId(), smallmessage))
							continue;
						else{
							Timestamp atual = new Timestamp(System.currentTimeMillis());
							AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), tutor.getId(), curso.getId(),atual,smallmessage);
						}
					String fullmessage = smallmessage;
					fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail é uma copia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user="
							+ useridto
							+ "&id= "
							+ useridfrom
							+ " para responder. ";
					Long time = System.currentTimeMillis();
					Mensagem msg = new Mensagem();
					msg.setSubject("Nova mensagem do Administrador");
					msg.setUseridfrom(useridfrom);
					msg.setUseridto(useridto);
					msg.setSmallmessage(smallmessage);
					msg.setFullmessage(fullmessage);
					msg.setTimecreated(time);

					//((MoodleEnv) env).addMensagem(msg);

					ControleEnvio.enviar(msg, env, idAction);
					
				}
				
			} catch (NullPointerException e) {
				ControleActions.setAlunosParticipantes(false);
			}
			
		}
		
		}
		
		ControleActions.setAlunosParticipantes(false);

	}


	public boolean done() {
		return done;
	}
	
	
	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
	
	
}
