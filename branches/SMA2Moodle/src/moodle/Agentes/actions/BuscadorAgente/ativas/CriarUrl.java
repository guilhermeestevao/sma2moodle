package moodle.Agentes.actions.BuscadorAgente.ativas;

import jamder.Environment;
import jamder.behavioural.Condition;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

import Util.SalvarLog;
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
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import dao.GerenciaCurso;
import dao.JPAUtil;

public class CriarUrl extends ActionMoodle{
	
	//private static final long serialVersionUID = -2896538484818688710L; serialVersionUID de acordo com o que esta no banco
	private boolean done = false;
	private boolean mantemAtivo;

	private Map<Curso, HashSet<Licao>> licaoCurso = new HashMap<Curso, HashSet<Licao>>();
	private Map<Curso, HashSet<Questionario>> questionarioCurso = new HashMap<Curso, HashSet<Questionario>>();
	private Map<Curso, HashSet<Tarefa>> tarefaCurso = new HashMap<Curso, HashSet<Tarefa>>();
	
	private Map<Curso, HashSet<Url>> urlCurso = new HashMap<Curso, HashSet<Url>>();
	private Date dataAtual = new Date();
	private GregorianCalendar d_Atual = new GregorianCalendar();
	private GregorianCalendar d_Inicio = new GregorianCalendar();
	private GregorianCalendar d_Final = new GregorianCalendar();
	
	public CriarUrl(String name) {
		super(name);
		idAction = 23;// atribuir ao idAction o id da action no banco
	}
	
	public CriarUrl(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 23;// atribuir ao idAction o id da action no banco
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
	
		if(!ControleActions.isCriarUrl())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();
		licaoCurso = ((MoodleEnv) env).getLicaoCursoProcessado();
		questionarioCurso = ((MoodleEnv) env).getQuestionarioCursoProcessado();
		tarefaCurso = ((MoodleEnv) env).getTarefaCursoProcessado();
		urlCurso = ((MoodleEnv) env).getUrlCursoProcessado();
		d_Atual.setTime(dataAtual);
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso c: cursos){

			try{
			//criando urls para lições
			HashSet<Licao> licoes = licaoCurso.get(c);
			if(!licoes.isEmpty()){
				for(Licao l: licoes){
					d_Inicio.setTime(l.getDataInicio());
					d_Final.setTime(l.getDataFinal());


					if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
						BigInteger m_l = new BigInteger("11");
						inserirUrl(c,l,m_l);
					}
				}
			}
			//criando urls para questionarios
			HashSet<Questionario> questionarios = questionarioCurso.get(c);
			if(!questionarios.isEmpty()){
				for(Questionario q: questionarios){
					d_Inicio.setTime(q.getDataInicio());
					d_Final.setTime(q.getDataFinal());
					
						if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
							BigInteger m_q= new BigInteger("13");
							inserirUrl(c,q,m_q);
						}
			    	
				}
			}
			
			
			//criando urls para os restantes das atividades com nota
			HashSet<Tarefa> tarefas = tarefaCurso.get(c);
			if(!tarefas.isEmpty()){
				for(Tarefa t: tarefas){
					d_Inicio.setTime(t.getDataInicio());
					d_Final.setTime(t.getDataFinal());
					
						if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
							BigInteger m_q= new BigInteger("21");
							inserirUrl(c,t,m_q);
						}
			    	
				}
			}
					
	    }catch (NullPointerException e) {
			ControleActions.setCriarUrl(false);;
		}

	}
	ControleActions.setCriarUrl(false);
}
	public boolean done(){
		return done;
	}
	
	// Metodos para persistir a URL.
	public void inserirUrl(Curso c, Questionario qst, BigInteger m){
		
		JPAUtil.beginTransaction(this.getClass());
		EntityManager entManager = JPAUtil.getEntityManager();
		
			for(Material mate : qst.getMateriais()){
				
				
				if(mate.getLink().isEmpty()){
					continue;
				}
				
				Query q = entManager.createQuery("SELECT url FROM Url url WHERE name = ?1");
				q.setParameter(1,mate.getNome()+" - "+qst.getName());
			    List<Url> urls = q.getResultList();
			    
			    if(!urls.isEmpty()){
			    	continue;
			    }
				
				Url url = new Url();
				url.setCourse(c.getId());
				url.setName(mate.getNome()+" - "+qst.getName());
				url.setExternalurl(mate.getLink());
				url.setTimemodified(new Date().getTime());
				entManager.persist(url);
				
				ModuloCurso mc = new ModuloCurso();
				mc.setCourse(c.getId());
				BigInteger moduloid = new BigInteger("17");
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

				c.setSectioncache(" ");
				entManager.merge(c);	
		  }
		JPAUtil.closeEntityManager(this.getClass());
	}
	

public void inserirUrl(Curso c, Tarefa trf, BigInteger m){
		
	JPAUtil.beginTransaction(this.getClass());
	EntityManager entManager = JPAUtil.getEntityManager();
	
			for(Material mate : trf.getMateriais()){
				
				if(mate.getLink().isEmpty()){
					continue;
				}
				
				Query q = entManager.createQuery("SELECT url FROM Url url WHERE name = ?1");
				q.setParameter(1,mate.getNome()+" - "+trf.getName());
			    List<Url> urls = q.getResultList();
			    
			    if(!urls.isEmpty()){
			    	continue;
			    }
			    
				Query queryUrl = entManager.createQuery("SELECT url FROM Url url");
			    List<Url> resultUrl = queryUrl.getResultList();
				
				BigInteger maiorUrl = new BigInteger("0");
				for(Url u : resultUrl){
					BigInteger v = u.getId();
					maiorUrl = v.max(maiorUrl);
				}
				
				maiorUrl = maiorUrl.add(new BigInteger("1"));
				
				Url url = new Url();	
				url.setId(maiorUrl);
				url.setCourse(c.getId());
				url.setName(mate.getNome()+" - "+trf.getName());
				url.setExternalurl(mate.getLink());
				url.setTimemodified(new Date().getTime());
				entManager.persist(url);
				
				
				Query queryModulo = entManager.createQuery("SELECT moduloCurso FROM ModuloCurso moduloCurso");
				List<ModuloCurso> resultModulo = queryModulo.getResultList();
				
				BigInteger maiorModulo = new BigInteger("0");
				for(ModuloCurso mod : resultModulo){
					BigInteger v = mod.getId();
					maiorModulo = v.max(maiorModulo);
				}
				
				maiorModulo = maiorModulo.add(new BigInteger("1"));		
				
				
				ModuloCurso mc = new ModuloCurso();
				mc.setId(maiorModulo);
				mc.setCourse(c.getId());
				BigInteger moduloid = new BigInteger("17");
				mc.setModule(moduloid);
				mc.setInstance(url.getId());
				BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course =" +c.getId()+" AND instance=" +trf.getId()+ " AND module="+m).getSingleResult();												
				mc.setSection(section);
				mc.setAdded(new Date().getTime());
				entManager.persist(mc);
				
				Query queryContexto = entManager.createQuery("SELECT contexto FROM Contexto contexto");
				List<Contexto> resultContexto = queryContexto.getResultList();
				
				BigInteger maiorContexto = new BigInteger("0");
				for(Contexto cont : resultContexto){
					BigInteger v = cont.getId();
					maiorContexto = v.max(maiorContexto);
				}
				
				maiorContexto = maiorContexto.add(new BigInteger("1"));
				
				
				Contexto contxt = new Contexto();
				contxt.setId(maiorContexto);
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

				c.setSectioncache(" ");
				entManager.merge(c);

			}
			JPAUtil.closeEntityManager(this.getClass());
		
	}
	
public void inserirUrl(Curso c, Licao l, BigInteger m){

	//JPAUtil.beginTransaction();
	EntityManager entManager = JPAUtil.getEntityManager();

		for(Material mate: l.getMateriais()){

			    if(mate.getLink().isEmpty()){
					continue;
				}
				
				Query q = entManager.createQuery("SELECT url FROM Url url WHERE name = ?1");
				q.setParameter(1,mate.getNome()+" - "+l.getName());
			    List<Url> urls = q.getResultList();
			    
			    if(!urls.isEmpty()){
			    	continue;
			    }
			    
				Url url = new Url();
				url.setCourse(c.getId());
				url.setName(mate.getNome()+" - "+l.getName());
				url.setExternalurl(mate.getLink());
				url.setTimemodified(new Date().getTime());
				entManager.persist(url);
				
				ModuloCurso mc = new ModuloCurso();
				mc.setCourse(c.getId());
				BigInteger moduloid = new BigInteger("17");
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
				
				c.setSectioncache(" ");
				entManager.merge(c);

				
			}
		//JPAUtil.closeEntityManager();
	}

}
