package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.ResourceBundle.Control;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Util.SalvarLog;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import dao.GerenciaCurso;
import dao.JPAUtil;
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
	private BigInteger idAgente;

	public MostraNovaDisciplina(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 14;
		idAgente = id;
	}

	public MostraNovaDisciplina(String name, BigInteger id) {
		super(name);
		idAction = 14;
		idAgente = id;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
	
		
		if(!ControleActions.isMostraNovaDisciplinaAluno())
			return;
				
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		int dias;
		List<Curso> novosCursos = new ArrayList<Curso>();
		
		for (Curso curso : manager.getCursos()) {
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
			
			
			System.out.println("N>"+curso.getFullName());
		
			dias = difDias(curso.getDataCriacao());

			if (dias == 0)
				novosCursos.add(curso);
		}
		
		if (!novosCursos.isEmpty()) {
			podeEnviar = true;
			
			//JPAUtil.beginTransaction();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			for(Curso c : manager.getCursos()){
				
				for (Curso cn : novosCursos) {
					//Se os dois cursos estiverem na mesma categoria
				
					if(c.getCategory().equals(cn.getCategory())){
					
					for (Aluno al : c.getAlunos()) {
						
						EntityManager entManager = JPAUtil.getEntityManager();
						try {
							
							Query ss = entManager.createNamedQuery("byMensagemCustomizada");
							BigInteger ac = new BigInteger(""+this.getId_action());
							ss.setParameter(1, this.getIdAgente());
							ss.setParameter(2, ac);
							
							
							BigInteger useridto = al.getId();
							MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
							String smallmessage = mensC.getMensagem();
							smallmessage = smallmessage.replaceAll("<nome do aluno>", al.getCompleteName());
							smallmessage = smallmessage.replaceAll("<nome da disciplina>", cn.getFullName());
							smallmessage = smallmessage.replaceAll("<data>", formato.format(cn.getDataCriacao()));
			
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
								fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail é uma copia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user="+ useridto+ "&id= "+ useridfrom+ " para responder. ";
								Long time = System.currentTimeMillis();
								Mensagem msg = new Mensagem();
								msg.setSubject("Nova mensagem do Administrador");
								msg.setUseridfrom(useridfrom);
								msg.setUseridto(useridto);
								msg.setSmallmessage(smallmessage);
								msg.setFullmessage(fullmessage);
								msg.setTimecreated(time);

								//((MoodleEnv) env).addMensagem(msg);

								ControleEnvio.enviar(msg, env, idAction);
								
							}

						} catch (NullPointerException e) {
							ControleActions.setMostraNovaDisciplinaAluno(false);
						}

					}
				}else{
					System.out.println("Categorias diferentes."+"Velho: "+c.getCategory()+" Novo: "+cn.getCategory());
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
	

	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}

}
