

import jade.core.Service;

import java.math.BigInteger;

import dao.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import moodle.dados.atividades.Chat;
import moodle.dados.contexto.Contexto;
import moodle.dados.contexto.ModuloCurso;
import moodle.dados.contexto.Topico;



public class TestePersistecia {
	
	 private static final EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("moodlePU");
	
	public static void main(String[] args) {
		
		EntityManager entManager = JPAUtil.getEntityManager();
		
		//Id do curso onde será inserido o chat
		BigInteger idCurso = new BigInteger("2");
		
		//Id da avaliação que irá gerar o chat
		BigInteger idAvaliacao = new BigInteger("1");
		
		
		Chat chat = new Chat();
		
		chat.setName("Chat criado via código");
		chat.setIntro("Esse chat foi criado automoticamente via código");
		chat.setChattime(System.currentTimeMillis());
		chat.setCourse(idCurso);
		chat.setTimemodified(System.currentTimeMillis());
		
		entManager.getTransaction().begin();
		entManager.persist(chat);
		entManager.getTransaction().commit();
		//	entManager.close();
		
		ModuloCurso mc = new ModuloCurso();
		mc.setCourse(idCurso);
		mc.setAdded(System.currentTimeMillis());
		BigInteger moduluChat = new BigInteger("4");
		mc.setModule(moduluChat);
		mc.setInstance(chat.getId());
		Query query = entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course = :curso AND instance = :questionario AND module = 16");
		query.setParameter("curso", idCurso);
		query.setParameter("questionario", idAvaliacao);
		BigInteger section = (BigInteger) query.getSingleResult();
		mc.setSection(section);
		
		entManager.getTransaction().begin();
		entManager.persist(mc);
		entManager.getTransaction().commit();
		
		
		Contexto contexto = new Contexto();
		BigInteger contextLevel = new BigInteger("70");
		contexto.setContextlevel(contextLevel);
		contexto.setInstanceid(mc.getId());
		query = entManager.createNativeQuery("SELECT id FROM mdl_context WHERE contextlevel = 50 AND instanceid = :curso");
		query.setParameter("curso", idCurso);
		BigInteger contextCurso = (BigInteger) query.getSingleResult();
		String path = "/1/3/" + contextCurso + "/";
		contexto.setPath(path);
		
		entManager.getTransaction().begin();
		entManager.persist(contexto);
		entManager.getTransaction().commit();
		
		String newPath = path+contexto.getId();
		
		Contexto contextoTemp = entManager.find(Contexto.class, contexto.getId());
    	contextoTemp.setPath(newPath);
    	
    	query = entManager.createNativeQuery("SELECT sequence FROM mdl_course_sections WHERE id= :cursoSection");
    	query.setParameter("cursoSection", section);
    	String sequence = (String) query.getSingleResult();
    	String newSequence = sequence + "," +mc.getId();
    	Topico top = entManager.find(Topico.class, section);
    	top.setSequence(newSequence);
    	
    	entManager.getTransaction().begin();
    	entManager.persist(top);
    	entManager.getTransaction().commit();
    	
		entManager.close();
		
	}

}
