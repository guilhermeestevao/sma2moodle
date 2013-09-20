package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;


//CODIFICA�AO

public class MostraNovaDisciplina extends ActionMoodle{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8692038967852542326L;
	private boolean done = false;
	private boolean mantemAtivo;

	public MostraNovaDisciplina(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 14;
	}

	public MostraNovaDisciplina(String name) {
		super(name);
		idAction = 14;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		block(23 * 1000L);

		
		/*
		 * Verificar com o guilherme sobre essa action
		 * Há repetição de action:
		 * Existe essa do Agente Acompanhante Tutor
		 * E existe a MostraNovaDisciplina do Agente Acompanhante
		 */
		
		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();

		if (!mantemAtivo)
			return;

		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		int dias;
		List<Curso> novosCursos = new ArrayList<Curso>();
		List<Aluno> alunos = new ArrayList<Aluno>();
		for (Curso curso : manager.getCursos()) {
		
			dias = difDias(curso.getDataCriacao());

			if (dias == 0)
				novosCursos.add(curso);
		}

		if (!novosCursos.isEmpty()) {
			podeEnviar = true;

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			for (Curso c : novosCursos) {
				for (Aluno al : c.getAlunos()) {

					if(verificaControle(c.getId(), al.getId()))
						continue;
					
					
					
					try {
						BigInteger useridto = al.getId();

						String smallmessage = "Prezado " + al.getCompleteName() + ", \n";

						smallmessage += " em "+ formato.format(c.getDataCriacao())+ " foi criado uma nova disciplina";
		
						smallmessage += " chamada " + c.getFullName() + ".";
						
						smallmessage += " Procure ler o conte�do dessa disciplina";

						if (podeEnviar) {
					
							CompanheiroAgente comp = (CompanheiroAgente)myAgent;
							AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId());
							
							
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
