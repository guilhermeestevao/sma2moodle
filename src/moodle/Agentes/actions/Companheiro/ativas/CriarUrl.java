package moodle.Agentes.actions.Companheiro.ativas;

import jamder.Environment;
import jamder.behavioural.Condition;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import javax.persistence.EntityManager;

import moodle.dados.Assunto;
import moodle.dados.Curso;
import moodle.dados.Url;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.Licao;
import moodle.dados.atividades.Questionario;
import moodle.dados.atividades.Tarefa;
import moodle.dados.contexto.Contexto;
import moodle.dados.contexto.Topico;
import moodle.dados.Material;
import moodle.dados.contexto.ModuloCurso;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import dao.GerenciaCurso;
import dao.JPAUtil;

public class CriarUrl extends ActionMoodle{
	
	//private static final long serialVersionUID = -2896538484818688710L; serialVersionUID de acordo com o que esta no banco
	private boolean done = false;
	private boolean mantemAtivo;

	private Map<Curso, HashSet<Licao>> licaoCurso = new HashMap<Curso, HashSet<Licao>>();
	private Map<Curso, HashSet<Url>> urlCurso = new HashMap<Curso, HashSet<Url>>();
	private Date dataAtual = new Date();
	private GregorianCalendar d_Atual = new GregorianCalendar();
	private GregorianCalendar d_Inicio = new GregorianCalendar();
	private GregorianCalendar d_Final = new GregorianCalendar();
	
	public CriarUrl(String name) {
		super(name);
		idAction = 20;// atribuir ao idAction o id da action no banco
	}
	
	public CriarUrl(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 20;// atribuir ao idAction o id da action no banco
	}
	
		@Override
	public void execute(Environment env, Object[] params) {
	
		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();

		block(8*1000L);
		
		if(!mantemAtivo)
			return;
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();
		licaoCurso = ((MoodleEnv) env).getLicaoCursoProcessado();
		urlCurso = ((MoodleEnv) env).getUrlCursoProcessado();
		d_Atual.setTime(dataAtual);
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso c: cursos){
			
			//criando urls para lições
			HashSet<Licao> licoes = licaoCurso.get(c);
			if(!licoes.isEmpty()){
				for(Licao l: licoes){
					d_Inicio.setTime(l.getDataInicio());
					d_Final.setTime(l.getDataFinal());
					
					if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
						BigInteger m_l = new BigInteger("13");
						inserirUrl(c,l,m_l);
					}
				}
			}
			
			//criando urls para os restantes das atividades com nota
			for(AtividadeNota atividade : c.getAtividadesNota()){
				if(atividade instanceof Questionario){
					Questionario questionario = (Questionario) atividade;
					d_Inicio.setTime(questionario.getDataInicio());
					d_Final.setTime(questionario.getDataFinal());
						if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
							BigInteger m_q= new BigInteger("16");
							inserirUrl(c,questionario,m_q);
						}
			    }
			    if(atividade instanceof Tarefa){
			    		Tarefa tarefa = (Tarefa) atividade;
			    		d_Inicio.setTime(tarefa.getDataInicio());
						d_Final.setTime(tarefa.getDataFinal());
			    		if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
			    			BigInteger m_t= new BigInteger("1");	
			    			inserirUrl(c,tarefa,m_t);
			    		}
			    }
	      }		
	}
}

	public boolean done(){
		return done;
	}
	
	// Metodos para persistir a URL.
	public void inserirUrl(Curso c, Questionario qst, BigInteger m){
		
		for(Assunto assunto : qst.getAssuntos()){
			
			for(Material mate : assunto.getMaterial()){
				
				JPAUtil.beginTransaction();
				EntityManager entManager = JPAUtil.getEntityManager();
				
				Url url = new Url();
				url.setCourse(c.getId());
				url.setName(mate.getNome());
				url.setExternalurl(mate.getLink());
				url.setTimemodified(new Date().getTime());
				entManager.persist(url);
				
				ModuloCurso mc = new ModuloCurso();
				mc.setCourse(c.getId());
				BigInteger moduloid = new BigInteger("20");
				mc.setModule(moduloid);
				mc.setInstance(url.getId());
				BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course =" +c.getId()+" AND instance=" +qst.getId()+ " AND module="+m).getSingleResult();												
				mc.setSection(section);
				mc.setAdded(new Date().getTime());
				entManager.persist(mc);
				
				Contexto contxt = new Contexto();
				BigInteger ctxtLevel = new BigInteger("70");
				contxt.setContextlevel(ctxtLevel);
				contxt.setInstanceid(mc.getId());
				BigInteger idC = (BigInteger) entManager.createNativeQuery("SELECT id FROM mdl_context WHERE instanceid = " +c.getId()+" AND contextlevel = 50").getSingleResult();
				contxt.setPath("/1/3/"+idC+"/");
				entManager.persist(contxt);
		
				BigInteger idContexto = contxt.getId();
				String newPath = contxt.getPath() + contxt.getId();
				
				Contexto contextoTemp = entManager.find(Contexto.class, contxt.getId());
				contextoTemp.setPath(newPath);
				
				String sequence = (String) entManager.createNativeQuery("SELECT sequence FROM mdl_course_sections WHERE id="+ section).getSingleResult();
				String newSequence = sequence + "," + mc.getId();
				
				Topico top = entManager.find(Topico.class, section);
				top.setSequence(newSequence);

				c.setSectioncache("NULL");
				JPAUtil.closeEntityManager();
				
				
			}
		}
	}
	
public void inserirUrl(Curso c, Tarefa trf, BigInteger m){
		
		for(Assunto assunto : trf.getAssuntos()){
			
			for(Material mate : assunto.getMaterial()){
				
				JPAUtil.beginTransaction();
				EntityManager entManager = JPAUtil.getEntityManager();
				
				Url url = new Url();
				url.setCourse(c.getId());
				url.setName(mate.getNome());
				url.setExternalurl(mate.getLink());
				url.setTimemodified(new Date().getTime());
				entManager.persist(url);
				
				ModuloCurso mc = new ModuloCurso();
				mc.setCourse(c.getId());
				BigInteger moduloid = new BigInteger("20");
				mc.setModule(moduloid);
				mc.setInstance(url.getId());
				BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course =" +c.getId()+" AND instance=" +trf.getId()+ " AND module="+m).getSingleResult();												
				mc.setSection(section);
				mc.setAdded(new Date().getTime());
				entManager.persist(mc);
				
				Contexto contxt = new Contexto();
				BigInteger ctxtLevel = new BigInteger("70");
				contxt.setContextlevel(ctxtLevel);
				contxt.setInstanceid(mc.getId());
				BigInteger idC = (BigInteger) entManager.createNativeQuery("SELECT id FROM mdl_context WHERE instanceid = " +c.getId()+" AND contextlevel = 50").getSingleResult();
				contxt.setPath("/1/3/"+idC+"/");
				entManager.persist(contxt);
		
				BigInteger idContexto = contxt.getId();
				String newPath = contxt.getPath() + contxt.getId();
				
				Contexto contextoTemp = entManager.find(Contexto.class, contxt.getId());
				contextoTemp.setPath(newPath);
				
				String sequence = (String) entManager.createNativeQuery("SELECT sequence FROM mdl_course_sections WHERE id="+ section).getSingleResult();
				String newSequence = sequence + "," + mc.getId();
				
				Topico top = entManager.find(Topico.class, section);
				top.setSequence(newSequence);

				c.setSectioncache("NULL");
				JPAUtil.closeEntityManager();
							}
		}
	}
	
public void inserirUrl(Curso c, Licao l, BigInteger m){
		

	for(Assunto assunto : l.getAssuntos()){
		for(Material mate: assunto.getMaterial()){
				JPAUtil.beginTransaction();
				EntityManager entManager = JPAUtil.getEntityManager();
				
				Url url = new Url();
				url.setCourse(c.getId());
				url.setName(mate.getNome());
				url.setExternalurl(mate.getLink());
				url.setTimemodified(new Date().getTime());
				entManager.persist(url);
				
				ModuloCurso mc = new ModuloCurso();
				mc.setCourse(c.getId());
				BigInteger moduloid = new BigInteger("20");
				mc.setModule(moduloid);
				mc.setInstance(url.getId());
				BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course =" +c.getId()+" AND instance=" +l.getId()+ " AND module="+m).getSingleResult();												
				mc.setSection(section);
				mc.setAdded(new Date().getTime());
				entManager.persist(mc);
				
				Contexto contxt = new Contexto();
				BigInteger ctxtLevel = new BigInteger("70");
				contxt.setContextlevel(ctxtLevel);
				contxt.setInstanceid(mc.getId());
				BigInteger idC = (BigInteger) entManager.createNativeQuery("SELECT id FROM mdl_context WHERE instanceid = " +c.getId()+" AND contextlevel = 50").getSingleResult();
				contxt.setPath("/1/3/"+idC+"/");
				entManager.persist(contxt);
		
				BigInteger idContexto = contxt.getId();
				String newPath = contxt.getPath() + contxt.getId();
				
				Contexto contextoTemp = entManager.find(Contexto.class, contxt.getId());
				contextoTemp.setPath(newPath);
				
				String sequence = (String) entManager.createNativeQuery("SELECT sequence FROM mdl_course_sections WHERE id="+ section).getSingleResult();
				String newSequence = sequence + "," + mc.getId();
				
				Topico top = entManager.find(Topico.class, section);
				top.setSequence(newSequence);

				c.setSectioncache("NULL");
				JPAUtil.closeEntityManager();
				
			}
		}
}

}
