package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.ResourceBundle.Control;

import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
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
	
		
		if(!ControleActions.isMostraNovaDisciplinaAluno())
			return;
				
		System.out.println(ControleActions.isMostraNovaDisciplinaAluno()+" "+this.getClass());
		
		
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

			for(Curso c : manager.getCursos()){
				
				
				for (Curso cn : novosCursos) {
					//Se os dois cursos estiverem na mesma categoria
					if(c.getCategory() == cn.getCategory()){
					
					for (Aluno al : c.getAlunos()) {
						
						try {
							BigInteger useridto = al.getId();

							String smallmessage = "Prezado(a)  " + al.getCompleteName() + ". \n";

							smallmessage += " Em "+ formato.format(c.getDataCriacao())+ " foi criado uma nova disciplina";
			
							smallmessage += " chamada " + cn.getFullName() + ".";
							
							smallmessage += " Você deve ler o conteúdo  que está disponível na página inicial da disciplina do moodle. \n ";

								if (podeEnviar) {
						
									smallmessage += "\n";
									CompanheiroAgente comp = (CompanheiroAgente)myAgent;
									if(verificaMens(c.getId(), al.getId(), smallmessage))
										continue;
									else{
										Timestamp atual = new Timestamp(System.currentTimeMillis());
										AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId(),atual,smallmessage);
									}
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
							ControleActions.setMostraNovaDisciplinaAluno(false);
						}

					}
				}
				}
				
				
				
			}
		}
		
		ControleActions.setMostraNovaDisciplinaAluno(false);

	}

	private int difDias(Date data) {
		long dataEntrada = data.getTime();
		long dataAtual = new Date().getTime();
		long dif = dataEntrada - dataAtual;
		int dias = (int) (dif / 86400000);
		return dias;
	}

}
