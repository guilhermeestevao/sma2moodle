package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.service.spi.Stoppable;

import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.atividades.Forum;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;


public class AlunosParticipantes extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8359505679128722723L;
	private boolean done = false;
	private boolean mantemAtivo;

	public AlunosParticipantes(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		
		idAction = 1;
		
	}

	public AlunosParticipantes(String name) {
		super(name);
		idAction = 1;
	}

	@Override
	public void execute(Environment env, Object[] params) {

		
		if(!ControleActions.isAlunosParticipantes())
			return;

		System.out.println(myAgent.getLocalName()+" -- "+this.getClass());
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {

			List<Tutor> tutores = curso.getTutores();
			
			
			if(tutores.isEmpty())
				continue;
			
			for(Tutor tutor : tutores){
		

			Set<Aluno> alunos = curso.getAlunos();
			try {
				BigInteger useridto = tutor.getId();

				podeEnviar = false;

				String smallmessage = "Prezado(a) " + tutor.getCompleteName()+ ". \n";

				smallmessage += "Na disciplina " + curso.getFullName()+ ", existem os seguintes f�runs: \n\n";

				for (AtividadeParticipacao atividade : curso
						.getAtividadesParticipacao()) {

					if (atividade instanceof Forum) {
						Forum forum = (Forum) atividade;
						// Foruns n�o avaliativos

						if (forum.isAvaliativo()) {

							Map<Aluno, BigDecimal> alunosComNota = forum
									.getAlunosComNotas();
							// caso faltem at� dois dias para o fim da avalia��o

							if (MoodleEnv.verificarData(forum.getDataFinal(), 3)) {
								podeEnviar = true;
								smallmessage += "> " + forum.getName();
								smallmessage += "\nOnde o(s) aluno(s): \n";
								for (Aluno aluno : alunos) {
									if (!alunosComNota.containsKey(aluno)) {
										smallmessage += aluno.getCompleteName()+ "\n";
									}
								}
							}
						}
					}
				}
				smallmessage += "\nnão possue(m) nenhuma participação no(s) respectivo(s) fórum(ns) ou não receberam suas devidas notas referentes a postagens realizadas. \n";
				smallmessage += "É necessário que você incentive e acompanhe a participação desse(s) aluno(s) nos respectivos fórum(s).";

						
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
					fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para voc� em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user="
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

					((MoodleEnv) env).addMensagem(msg);
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
}
