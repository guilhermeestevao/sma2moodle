package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import dao.GerenciaCurso;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Condition;


//CODIFICA��O

public class InformarAndamento extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2896538484818688710L;
	private boolean done = false;
	private boolean mantemAtivo;
	List<Atividade> alunoSemNota;
	
	public InformarAndamento(String name){
		super(name);
		idAction = 13;
	}
	
	public InformarAndamento(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 13;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		

		
		MoodleEnv envir = (MoodleEnv) env;
		
		if(!ControleActions.isInformaAndamento())
			return;
		
		System.out.println(ControleActions.isInformaAndamento()+" "+this.getClass());
		
		GerenciaCurso manager = envir.getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		
		List<Atividade> atividadesAlunoSemNota = new ArrayList<Atividade>();
			
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			
			
			for(Aluno aluno : curso.getAlunos()){
				
				atividadesAlunoSemNota.clear();
				
				BigInteger useridto = aluno.getId();
				
				String smallmessage = "Prezado(a)" + aluno.getCompleteName() + ", \n";
				smallmessage += "Na disciplina " + curso.getFullName() + " ,  seu atual desempenho é: \n\n";
				
				
					for(AtividadeNota at : curso.getAtividadesNota()){
						
						if(at.getAlunosComNotas().containsKey(aluno)){
						
							smallmessage += "Atividade " + at.getName() + ": \n";
							smallmessage += "Sua nota: " + at.getAlunosComNotas().get(aluno) + " / Nota máxima: " + at.getNotaMaxima() + "\n\n";
						
						}else{
							
							Date dataAtual = new Date();
							
							//Adiciono a atividade apenas se a data final ainda for anterior a data atual
							if(at.getDataFinal().before(dataAtual))
								atividadesAlunoSemNota.add(at);
						}		
						
						 
					}
					
					for(AtividadeParticipacao at : curso.getAtividadesParticipacao()){
						
						if(at.isAvaliativo()){
							
							if(at.getAlunosComNotas().containsKey(aluno)){
								
								smallmessage += "Atividade " + at.getName() + ": \n";
								smallmessage += "Sua nota: " + at.getAlunosComNotas().get(aluno) + " / Nota máxima: " + at.getNotaMaxima() + "\n\n";
							
							}else{
								Date dataAtual = new Date();
								
								//Adiciono a atividade apenas se a data final ainda for anterior a data atual
								if(at.getDataFinal().before(dataAtual))
									atividadesAlunoSemNota.add(at);
							}
							
						}
					}
				
					BigDecimal nota = curso.getNotaGeralAlunos().get(aluno);
					
					
					
					if(nota != null){
					
						smallmessage += "\n Sua média atual é:: " + nota.toString();
						
						if(nota.intValue() < 70){
							smallmessage += "\n Procure estudar mais, peça ajuda aos colegas e ao tutor e busque se envolver mais nas atividades";
							
						}else{
							smallmessage += "\n Parabéns! Continue estudando e buscando evoluir em seus estudos. \n";
						}
					
					}
					
						
				
				if(!atividadesAlunoSemNota.isEmpty()){
				
					smallmessage += "\n\nFaltam notas suas nas seguintes atividades:\n";
					
					for(Atividade at : atividadesAlunoSemNota){
						
						smallmessage +=" - " + at.getName() + "\n";
					}

				}
								
				
				if(nota == null && atividadesAlunoSemNota.isEmpty()){
					//smallmessage += "\n\n Sem atividades no curso até o momento.";
					continue;
				}
					
				smallmessage += "\n"; 	
				String fullmessage = smallmessage;
					
				fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail é a cópia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
					
				Long time = System.currentTimeMillis();
				
				CompanheiroAgente comp = (CompanheiroAgente)myAgent;
				if(verificaMens(curso.getId(), aluno.getId(), smallmessage))
					continue;
				else{
					Timestamp atual = new Timestamp(System.currentTimeMillis());
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), aluno.getId(), curso.getId(),atual,smallmessage);
				}
				
				Mensagem msg = new Mensagem();
				msg.setSubject("Nova mensagem do Administrador");
				msg.setUseridfrom(useridfrom);
				msg.setUseridto(useridto);
				msg.setSmallmessage(smallmessage);
				msg.setFullmessage(fullmessage);
				msg.setTimecreated(time);
				
				((MoodleEnv)env).addMensagem(msg);
				
				
			}
	
		}
		
		ControleActions.setInformaAndamento(false);
		
	}
	
	@Override
	public boolean done() {
		return done;
	}

}
