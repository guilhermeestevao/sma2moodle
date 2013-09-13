package moodle.Agentes.actions.AjudanteAgente.ativas;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import moodle.Agentes.AgenteUtil;
import moodle.Agentes.AjudanteAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Log;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;

//CODIFICA��O

public class ExibirDicasParticipantes extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8530674308196288375L;
	private boolean done = false;
	private boolean mantemAtivo;

	public ExibirDicasParticipantes(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 9;
	}

	public ExibirDicasParticipantes(String name) {
		super(name);
		idAction = 9;
	}

	private boolean isViewAll(Aluno a) {
		List<Log> logs = a.getLogs();

		String module = "user";
		String action = "view all";

		for (Log log : logs) {
			if (log.getModule().equals(module)
					&& log.getAction().equals(action)) {
				JOptionPane.showMessageDialog(null, a.getCompleteName());
				return false;

			}
		}

		return true;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		block(24 * 1000L);

		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();

		if (!mantemAtivo)
			return;

		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		

		for (Curso c : cursos) {

			for (Aluno al : c.getAlunos()) {

				if(verificaControle(c.getId(), al.getId()))
					continue;
				
				
				
				podeEnviar = isViewAll(al);
				
				if (podeEnviar) {
				
					AjudanteAgente comp = (AjudanteAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId());
					
					
					BigInteger useridto = al.getId();
					String smallmessage = "Ol� "+ al.getCompleteName()+ " para saber quem mais est�o matriculado no mesmo curso que voc�, ";
					smallmessage += "ou mesmos quem seja o respomsavel pelo andamento do curso como tutores e professres, basta ir no menu de navega��o no canto direito da tela ";
					smallmessage += "clique sobre o nome do curso depois clique em PARTICIPANTES,";
					smallmessage += "voc� encontrar� uma lista com todos os integrantes do curso. Apartir disso voc� poder� entrar em cntato com qualquer um clicanco em ENVIAR MMENSAGEM, \n";

					
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
			}
		}
	}
}
