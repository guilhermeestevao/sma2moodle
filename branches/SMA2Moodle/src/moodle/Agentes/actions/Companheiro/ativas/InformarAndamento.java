package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;
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
import moodle.dados.atividades.Licao;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
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
	private GregorianCalendar d_atual = new GregorianCalendar();
	private GregorianCalendar d_inicio = new GregorianCalendar();
	private GregorianCalendar d_final = new GregorianCalendar();
	private BigInteger idAgente;
	
	public InformarAndamento(String name, BigInteger id){
		super(name);
		idAction = 13;
		idAgente = id;
	}
	
	public InformarAndamento(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 13;
		idAgente = id;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
				
		MoodleEnv envir = (MoodleEnv) env;
		
		if(!ControleActions.isInformaAndamento())
			return;
		
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = envir.getGerenciaCurso();
		d_atual.setTime(new Date());
		BigInteger useridfrom = new BigInteger("2");
		
		JPAUtil.beginTransaction();
		List<Atividade> atividadesAlunoSemNota = new ArrayList<Atividade>();
			
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			
			
			for(Aluno aluno : curso.getAlunos()){
				
				atividadesAlunoSemNota.clear();
				
				BigInteger useridto = aluno.getId();
				
				
				EntityManager entManager = JPAUtil.getEntityManager();
				
				String smallmessage = "";//(String) entManager.createNativeQuery("SELECT tipo FROM ag_mensagens WHERE agente = " +this.getIdAgente()+" AND action = "+this.getId_action()).getSingleResult();
				
				Query ss = entManager.createNamedQuery("byMensagemCustomizada");
				BigInteger ac = new BigInteger(""+this.getId_action());
				ss.setParameter(1, this.getIdAgente());
				ss.setParameter(2, ac);
				
				List<MensagemCustomizada> mensagens = ss.getResultList();
				smallmessage = retornaMensagem(mensagens, "introducao");
	
				smallmessage = smallmessage.replaceAll("<nome do aluno>", aluno.getCompleteName());
				smallmessage = smallmessage.replaceAll("<nome da disciplina>", curso.getFullName());
				smallmessage+="\n\n";
				
					for(AtividadeNota at : curso.getAtividadesNota()){
						
						d_final.setTime(at.getDataFinal());
						if(at.getAlunosComNotas().containsKey(aluno) && d_atual.before(d_final)){
							String ativ = retornaMensagem(mensagens, "atividades");
							ativ = ativ.replaceAll("<nome da atividade>", at.getName());
							ativ = ativ.replaceAll("<nota>", ""+at.getAlunosComNotas().get(aluno));
							ativ+="\n";
							smallmessage+=ativ;
						
						}else{
							
							Date dataAtual = new Date();
							
							//Adiciono a atividade apenas se a data final ainda for anterior a data atual
							if(at.getDataFinal().before(dataAtual))
								atividadesAlunoSemNota.add(at);
						}		
						
						 
					}
					
					for(AtividadeParticipacao at : curso.getAtividadesParticipacao()){
						
						if(at.isAvaliativo()){
							d_final.setTime(at.getDataFinal());
							if(at.getAlunosComNotas().containsKey(aluno) && d_atual.before(d_final)){
								String ativ = retornaMensagem(mensagens, "atividades");
								ativ = ativ.replaceAll("<nome da atividade>", at.getName());
								ativ = ativ.replaceAll("<nota>", ""+at.getAlunosComNotas().get(aluno));
								ativ+="\n";
								smallmessage+=ativ;
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
						smallmessage+="\n";
						String media =retornaMensagem(mensagens, "media");
						media = media.replaceAll("<média>", nota.toString());
						smallmessage+=media;
						smallmessage+="\n\n";
						if(nota.intValue() < 70){
							String mediar = retornaMensagem(mensagens, "media ruim");
							smallmessage += mediar;	
							smallmessage+="\n\n";
						}else{
							String mediab =retornaMensagem(mensagens, "media boa");
							smallmessage += mediab;	
							smallmessage+="\n\n";
						}
					
					}
				
				if(!atividadesAlunoSemNota.isEmpty()){
					String falta =retornaMensagem(mensagens, "falta nota");
					smallmessage +=falta;
					smallmessage+="\n\n";
					
					for(Atividade at : atividadesAlunoSemNota){
						String atv =retornaMensagem(mensagens, "atividade");
						atv = atv.replaceAll("<nome da atividade> ", at.getName());
						atv+="\n";
						smallmessage+=atv;
					}

				}
				JPAUtil.closeEntityManager();				
				
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

	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
	public String retornaMensagem(List<MensagemCustomizada> mensagens, String tipo){
		String ativ="";
		
		for(int i=0;i<mensagens.size();i++){	
			if(mensagens.get(i).getTipo().equals(tipo)){	
				ativ = mensagens.get(i).getMensagem();
			}
		}
		return ativ;
	}

}
