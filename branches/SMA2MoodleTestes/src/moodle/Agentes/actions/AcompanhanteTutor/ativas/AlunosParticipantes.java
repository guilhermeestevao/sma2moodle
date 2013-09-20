package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
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
		block(21 * 1000L);

		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();

		if (!mantemAtivo)
			return;

		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {

			Tutor tutor = curso.getTutor();
			
			if(tutor == null)
				continue;
			
			if(verificaControle(curso.getId(), tutor.getId()))
				continue;
			

			Set<Aluno> alunos = curso.getAlunos();
			try {
				BigInteger useridto = tutor.getId();

				podeEnviar = false;

				String smallmessage = "Prezado " + tutor.getCompleteName()+ ", \n";

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
				smallmessage += "\nn�o possuem nenhuma participa��o nos respectivos foruns ou n�o receberam suas devidas notas referentes a postagens realizadas \n";
				smallmessage += "Seria interessante que voc� incentivasse essas alunos especificos a participarem dos f�runs, mesmo que tais n�o sejam avaliativos.";

				if (podeEnviar) {
			
					AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), tutor.getId(), curso.getId());
					
					smallmessage += "\n";
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

			}
		}

	}


	public boolean done() {
		return done;
	}
}
