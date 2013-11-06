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
		
		
		if(!ControleActions.isExibirDicasForum())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso c : cursos) {

			for (Aluno al : c.getAlunos()) {

				
				podeEnviar = isViewForum(al);
				

				if (podeEnviar) {
				
										
					BigInteger useridto = al.getId();
					String smallmessage = "Olá "+ al.getCompleteName()+ ", caso esteja com alguma dúvida em algum dos conteudo, ";
					smallmessage += "bastar clicar sobre o nome do curso que está matriculado e procurar algum fórum, ";
					smallmessage += "todos os cursos por padrão possuem uma atividade chamada Fórum de noticias. ";
					smallmessage += "Você tanto pode criar um tópico ou participar de um já existente. ";
					smallmessage += "Em um fórum os alunos podem interagir com tutores e professores com o objetivo de compatilhar conhecimentos ";
					smallmessage += "tirando dúvidas dos conteudos ou postando noticias ligadas ao assunto em discussão.";
					smallmessage += "Procure participar dos fóruns dos cursos em que está matrculado, pois alguns são avaliativos. \n ";
					
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
		ControleActions.setExibirDicasForum(false);

	}

	public boolean done() {
		return done;
	}

}
