package moodle.Agentes.actions.AjudanteAgente.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import moodle.Agentes.AgenteUtil;
import moodle.Agentes.AjudanteAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
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
		
		if(!ControleActions.isExibirDicasConfiguracao())
			return;
		
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		

		for (Curso c : cursos) {

			for (Aluno al : c.getAlunos()) {

			    
				podeEnviar = isUpdate(al);
				
				if (podeEnviar) {
					
				
				
					BigInteger useridto = al.getId();
					String smallmessage = "Olá " + al.getCompleteName()+ " você pode fazer alterações de seu perfil ";
					smallmessage += "clicando no menu CONFIGURAÇÕES. Nesse menu é possivel alterar sua senha, informações de contato, ";
					smallmessage += "informações pessoais, etc. \n";
					
					smallmessage += "\n";
					
					AjudanteAgente comp = (AjudanteAgente)myAgent;
					if(verificaMens(c.getId(), al.getId(), smallmessage))
						continue;
					else{
						Timestamp atual = new Timestamp(System.currentTimeMillis());
						AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId(),atual,smallmessage);
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
			}
		}
		
		ControleActions.setExibirDicasConfiguracao(false);
	}
}
