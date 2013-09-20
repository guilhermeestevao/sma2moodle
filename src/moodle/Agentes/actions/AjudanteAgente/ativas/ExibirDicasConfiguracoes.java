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

public class ExibirDicasConfiguracoes extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4235799585488236120L;
	private boolean done = false;
	private boolean mantemAtivo;

	public ExibirDicasConfiguracoes(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 7;
	}

	public ExibirDicasConfiguracoes(String name) {
		super(name);
		idAction = 7;
	}

	private boolean isUpdate(Aluno a) {

		List<Log> logs = a.getLogs();
		String module = "user";
		String action = "update";

		for (Log log : logs) {
			if (log.getModule().equals(module)
					&& log.getAction().equals(action)) {
				
				return false;

			}
		}

		return true;

	}

	@Override
	public void execute(Environment env, Object[] params) {
		block(23 * 1000L);

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
				
				
				podeEnviar = isUpdate(al);
				
				if (podeEnviar) {
					
					AjudanteAgente comp = (AjudanteAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId());
					
				
					BigInteger useridto = al.getId();
					String smallmessage = "Ol� " + al.getCompleteName()+ " voc� pode fazer altera��es de seu perfil ";
					smallmessage += "clicando no menu CONFIGURA��ES. Nesse menu � possivel alterar sua senha, informa��es de contato, ";
					smallmessage += "informa��es pessoais, etc. \n";
					
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
