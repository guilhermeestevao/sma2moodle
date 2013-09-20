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

public class ExibirDicasForuns extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 162089551253081886L;
	private boolean done = false;
	private boolean mantemAtivo;

	public ExibirDicasForuns(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 8;
	}

	public ExibirDicasForuns(String name) {
		super(name);
		idAction = 8;
	}

	private boolean isViewForum(Aluno a) {

		// Todos os logs do aluno
		List<Log> logsDoCurso = a.getLogs();

		String modulo = "forum";

		for (Log log : logsDoCurso) {
			if (log.getModule().equals(modulo)) {
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
				
				
				
				podeEnviar = isViewForum(al);
				

				if (podeEnviar) {
				
					AjudanteAgente comp = (AjudanteAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId());
										
					BigInteger useridto = al.getId();
					String smallmessage = "Ol� "+ al.getCompleteName()+ ", caso esteja com alguma d�vida em algum dos conteudo, ";
					smallmessage += "bastar clicar sobre o nome do curso que est� matriculado e procurar algum f�rum, ";
					smallmessage += "todos os cursos por padr�o possuem uma atividade chamada F�rum de noticias. ";
					smallmessage += "Voc� tanto pode criar um t�pico ou participar de um j� existente. ";
					smallmessage += "Em um f�rum os alunos podem interagir com tutores e professores com o objetivo de compatilhar conhecimentos ";
					smallmessage += "tirando d�vidas dos conteudos ou postando not�cias ligadas ao assunto em discuss�o.";
					smallmessage += "Procure participar dos f�runs dos cursos em que est� matrculado, pois alguns s�o avaliativos. \n ";
					
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
		done = true;

	}

	public boolean done() {
		return done;
	}

}
