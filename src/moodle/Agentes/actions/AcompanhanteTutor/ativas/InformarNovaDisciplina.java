package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;


public class InformarNovaDisciplina extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7818579743279287872L;
	private boolean done = false;
	private boolean mantemAtivo;

	public InformarNovaDisciplina(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 2;
	}

	public InformarNovaDisciplina(String name) {
		super(name);
		idAction = 2;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		block(22 * 1000L);

		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();

		if (!mantemAtivo)
			return;

		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		int dias;
		List<Curso> novosCursos = new ArrayList<Curso>();
		List<Tutor> tutores = new ArrayList<Tutor>();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {
			tutores.add(curso.getTutor());

			dias = difDias(curso.getDataCriacao());

			if (dias == 0)
				novosCursos.add(curso);
		}

		if (!novosCursos.isEmpty()) {
			podeEnviar = true;

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			for (Curso c : novosCursos) {
				for (Tutor t : tutores) {

					
					if(verificaControle(c.getId(), t.getId()))
						continue;
					
					
					try {
						BigInteger useridto = t.getId();

						String smallmessage = "Prezado " + t.getLastName()+ ", \n";

						smallmessage += " em "+ formato.format(c.getDataCriacao())+ " foi criado uma nova disciplina";
		
						smallmessage += " chamada " + c.getFullName() + ".";
						
						smallmessage += " Procure ler o conte�do dessa disciplina";

						if (podeEnviar) {
							
							AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
							
							AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), t.getId(), c.getId());
						
							
							smallmessage += "\n";
							String fullmessage = smallmessage;
							fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para voc� em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user="+ useridto+ "&id= "+ useridfrom+ " para responder. ";
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
		}

	}

	private int difDias(Date data) {
		long dataEntrada = data.getTime();
		long dataAtual = new Date().getTime();
		long dif = dataEntrada - dataAtual;
		int dias = (int) (dif / 86400000);
		return dias;
	}

}
