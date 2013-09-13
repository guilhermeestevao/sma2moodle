package moodle.Agentes.actions.AjudanteAgente.ativas;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

public class ExibirDicasCalendario extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8442175010742470870L;
	private boolean done = false;
	private boolean mantemAtivo;

	public ExibirDicasCalendario(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 6;
	}

	public ExibirDicasCalendario(String name) {
		super(name);
		idAction = 6;
	}

	private boolean isAddCalendar(Aluno a) {
		// Todos os logs do aluno
		List<Log> logs = a.getLogs();

		String modulo = "calendar";

		for (Log log : logs) {
			if (log.getModule().equals(modulo)) {

				return false;

			}
		}

		return true;

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
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());


		for (Curso c : cursos) {

			for (Aluno al : c.getAlunos()) {

				if(verificaControle(c.getId(), al.getId()))
					continue;
				
				
				
				podeEnviar = isAddCalendar(al);

				if (podeEnviar) {
					
					AjudanteAgente comp = (AjudanteAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId());
					
					
					
					BigInteger useridto = al.getId();
					String smallmessage = "Ol� "+ al.getCompleteName()+", esteja sempre atento ao calendario, pois ele informa todos os eventos e prazos do curso";
					smallmessage += "Para saber o que vai acontencer sobre uma determinada data, basta passar o mouse sobre o dia desejado. ";
					smallmessage += "Voc� tamb�m pode cadastar eventos de acordo com o ritimo de seus estudos clicando em NOVO EVENTO";
					
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

	@Override
	public boolean done() {
		return done;
	}
}
