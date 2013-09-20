package moodle.Agentes.actions.AcompanhanteDeProfessores;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;

import moodle.Agentes.AcompanhanteDeProfessores;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.Professor;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;

public class InformarNotasAtrasadas extends ActionMoodle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8599080239312931795L;
	private boolean done = false;
	private boolean mantemAtivo;
	
	public InformarNotasAtrasadas(String name) {
		super(name);
		idAction = 19;
	}

	public InformarNotasAtrasadas(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 19;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		block(20 * 1000L);

		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();
		
		if (!mantemAtivo)
			return;

		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		boolean podeEnviar = false;

		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {
			
			for(Atividade atividade : curso.getAtividadesNota()){
				
				Date data = new Date();
				DateTime hoje = new DateTime(data);
				DateTime fimDaAvaliacao = new DateTime(atividade.getDataFinal());
				
				//Quantidades de dias que se passaram desde o fim da avalia��o
				int diferenca =  Days.daysBetween(fimDaAvaliacao, hoje).getDays();
			
				if(atividade.getAlunosComNotas().isEmpty() && diferenca >= 14)
					podeEnviar = true;
				
				if(podeEnviar){
					
					try{
						
						Professor professor = curso.getProfessor();
						BigInteger useridto = professor.getId();
						BigInteger useridfrom = new BigInteger("2");
						
						
						if(verificaControle(curso.getId(), useridto))
							continue;
						else{
							
							AcompanhanteDeProfessores comp = (AcompanhanteDeProfessores)myAgent;
							AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), useridto, curso.getId());

						}
						
						
						String smallmessage = "Prezado "+professor.getLastName() +", \n";
						smallmessage+="J� fazem duas semanas que a atidade "+atividade.getName()+" no curo "+curso.getFullName()+" teve seu perido de avalia��o encerrado, por�m as notas dos alunos n�o foram postadas \n\n";
						smallmessage+="Procure postar as notas dos alunos o quanto antes.";
						smallmessage +="\n";
						
						String fullmessage = smallmessage;
						fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para voc� em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
						
						Long time = System.currentTimeMillis();						
						
						Mensagem msg = new Mensagem();
						msg.setSubject("Nova mensagem do Administrador");
						msg.setUseridfrom(useridfrom);
						msg.setUseridto(useridto);
						msg.setSmallmessage(smallmessage);
						msg.setFullmessage(fullmessage);
						msg.setTimecreated(time);
						
						((MoodleEnv)env).addMensagem(msg);
						
					}catch(NullPointerException e){
						e.printStackTrace();
					}
					
				}
			}
			
		}
	}
}
