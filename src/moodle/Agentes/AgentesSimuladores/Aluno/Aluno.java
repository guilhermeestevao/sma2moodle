package moodle.Agentes.AgentesSimuladores.Aluno;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import dao.DiscussaoForumDAO;
import dao.DiscussaoForumJpaDAO;
import dao.JPAUtil;


import moodle.Agentes.AgentesSimuladores.Dados.DiscussaoForum;
import moodle.dados.Curso;

import jamder.Environment;
import jamder.agents.GenericAgent;
import jamder.agents.ReflexAgent;
import jamder.roles.AgentRole;
import jade.core.AID;
import jade.core.Agent;

public abstract class Aluno extends ReflexAgent {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2439992103556989697L;
	protected long idAluno;
	private Map<BigInteger, BigInteger> itemIdCurso;
	private Map<BigInteger, List<BigInteger>> idsForumCurso;
	
	public Aluno(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		
	}

	
	@Override
	public void setup() {
		super.setup();
		
		itemIdCurso = new HashMap<BigInteger, BigInteger>();
		
		recuperaItemIdCurso();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<BigInteger> recuperaItemIdsAtividades(BigInteger idCurso){
		
		Query query = JPAUtil.getEntityManager().createNativeQuery("SELECT id FROM mdl_grade_items WHERE courseid = ?1 and itemtype = 'mod' ");
		query.setParameter(1, idCurso);
		List<BigInteger> ids = query.getResultList();
		
		return ids;
		
	}
	
	public Map<BigInteger, BigInteger> getItemIdCurso(){
		return itemIdCurso;
	}
	
	@SuppressWarnings("unchecked")
	public void recuperaItemIdCurso(){
		
		
		
		List<BigInteger> cursos = JPAUtil.getEntityManager().createNativeQuery("SELECT id FROM mdl_course").getResultList();
		
		Query query = JPAUtil.getEntityManager().createNativeQuery("SELECT id FROM mdl_grade_items WHERE courseid = ?1 and itemtype = 'course' ");
		
		for(BigInteger c : cursos){
			
			query.setParameter(1, c);
			
			try{
				BigInteger itId = (BigInteger) query.getSingleResult();
			
				itemIdCurso.put(c, itId);
			}catch(NoResultException e){
				System.out.println("Nao encontrou itemId do curso");
			}
			
			
			
		}
		
	}
	
	public List<DiscussaoForum> recuperaDiscussoesForum(){
		
		//Pega id das discussoes existens para poder fazer um post no forum
		DiscussaoForumDAO d = new DiscussaoForumJpaDAO();
		return d.findAll();
		
	}
	

	public long getId() {
		return idAluno;
	}


	public void setId(long id) {
		this.idAluno = id;
	}
	
	public double calculaMedia(BigInteger idCurso, BigInteger userid) {
		//Recebe os ids da atividade do curso passado como parametro
		List<BigInteger> atividades = recuperaItemIdsAtividades(idCurso);
		//Armazena a soma de todas as notas encontradas até então e divide pela quantidaade
		BigDecimal somaDasNotas = new BigDecimal(0);
		int totalDeNotas = 0;
		
		for (BigInteger id : atividades) {
			try {
		
				//Procura a nota pra caada id da lista e soma
				Query query = JPAUtil.getEntityManager().createNativeQuery("SELECT finalgrade FROM mdl_grade_grades WHERE itemid = ?1 AND userid = ?2");
				query.setParameter(1, id);
				query.setParameter(2, userid);
				BigDecimal nota = (BigDecimal) query.getSingleResult();
				somaDasNotas = somaDasNotas.add(nota);
				totalDeNotas++;
			} catch (NoResultException e) {

			}
		}
		//Obtem a média
		
		double media = 0;
		
		if(totalDeNotas > 0)
			media = somaDasNotas.doubleValue()/totalDeNotas;
			
		
			
		return media;
		
		
	}
	
	
}
