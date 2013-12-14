package moodle.Agentes.actions.BuscadorAgente.ativas;

import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.Licao;
import moodle.dados.atividades.Questionario;
import moodle.dados.atividades.Tarefa;
import moodle.dados.contexto.Contexto;
import moodle.dados.contexto.ModuloCurso;
import moodle.dados.contexto.Topico;
import moodle.dados.Files;
import moodle.dados.Folder;
import moodle.dados.Material;
import moodle.dados.Url;
import moodle.dados.Curso;
import moodle.dados.Assunto;
import jamder.Environment;
import jamder.behavioural.Condition;

import java.io.File;
import java.io.FileInputStream;
import org.postgresql.util.PSQLException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;

public class CriarFolderFiles extends ActionMoodle {

	//private static final long serialVersionUID = -2896538484818688710L; serialVersionUID de acordo com o que esta no banco
	private boolean done = false;
	private boolean mantemAtivo;
	private Map<Curso, HashSet<Licao>> licaoCurso = new HashMap<Curso, HashSet<Licao>>();
	private Map<Curso, HashSet<Url>> urlCurso = new HashMap<Curso, HashSet<Url>>();
	private Date dataAtual = new Date();
	private GregorianCalendar d_Atual = new GregorianCalendar();
	private GregorianCalendar d_Inicio = new GregorianCalendar();
	private GregorianCalendar d_Final = new GregorianCalendar();
	private int condicao = 0;

	public CriarFolderFiles(String name) {
		super(name);
		idAction = 21;// atribuir ao idAction o id da action no banco
	}

	public CriarFolderFiles(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 21;// atribuir ao idAction o id da action no banco
	}

	@Override
	public void execute(Environment env, Object[] params) {
		
		
		if(!ControleActions.isCriarFolderFiles())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		MoodleEnv envir = (MoodleEnv) env;	
		GerenciaCurso manager = envir.getGerenciaCurso();
		licaoCurso = envir.getLicaoCursoProcessado();
		d_Atual.setTime(dataAtual);
		
		List<Curso> cursos = manager.getCursos();
		JPAUtil.beginTransaction();
		synchronized (cursos) {
			for(Curso c: cursos){
				
				EntityManager entManager = JPAUtil.getEntityManager();
				
				HashSet<Licao> licao = licaoCurso.get(c);
				try{
				if(!licao.isEmpty()){
					/*
					for(Licao li : licao){
						d_Inicio.setTime(li.getDataInicio());
						d_Final.setTime(li.getDataFinal());
						if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
							Query q = entManager.createQuery("SELECT folder FROM Folder folder WHERE folder.name= ?1");
							String nome_p = "Pacote de Materiais - Lição "+li.getName();
							q.setParameter(1, nome_p);
							List<Folder> folder = q.getResultList();
							
							BigInteger contextoId = new BigInteger("0");
							if(folder.isEmpty()){
							   contextoId = inserirFolder(c,li,new BigInteger("13"));
							}else{
								for (Folder f : folder) {								 
									contextoId = buscaContextoid(f.getId(),c.getId());
								}
							 }
						 

								 for(Material material : li.getMateriais()){
									 
									 	String hash = new String();
										Long ta = new Long(0);
										try {
											 hash = stringHexa(gerarHash(arquivoByte(material.getCaminho()),"SHA-1"));
											 ta = enviaArquivo(material.getCaminho(), hash);
										}catch (IOException e){
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										Query query2 =  entManager.createQuery("SELECT files FROM Files files WHERE files.contextid= ?1");
										query2.setParameter(1, contextoId);
										List<Files> fil = query2.getResultList();
										Query qe = entManager.createQuery("SELECT files FROM Files files WHERE files.filename= ?1");
										qe.setParameter(1,".");
										List<Files> fp = qe.getResultList();
										if(!fil.isEmpty()){
											fil.removeAll(fp);	
										}
										
										//Variaveis constantes
										BigInteger contextoDraft = new BigInteger("5");
										String hash_draft = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
										BigInteger itemid_folder = new BigInteger("0");
										BigInteger size_draft = new BigInteger("0");
										BigInteger size = BigInteger.valueOf(ta);
										
										if(fil.isEmpty()){
										//	JOptionPane.showMessageDialog(null, "folder vazio");
											Long item;
											while(true){
												item = gerarItemid();
												if(verificaItemid(BigInteger.valueOf(item))){
													break;
												}
											}
											BigInteger itemid = BigInteger.valueOf(item); 									
											
											armazenaFiles(hash, contextoDraft, "user", "draft", itemid, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
											armazenaFiles(hash_draft, contextoDraft, "user", "draft", itemid,".",size_draft, "NULL", "NULL", "NULL", "NULL");
											armazenaFiles(hash, contextoId, "mod_folder", "content", itemid_folder, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
											armazenaFiles(hash_draft, contextoId, "mod_folder", "content", itemid_folder,".", size_draft,"document/unknown","NULL", "NULL", "NULL");
										}else{
											Long item;
											while(true){
												item = gerarItemid();
												if(verificaItemid(BigInteger.valueOf(item))){
													break;
												}
											}
											BigInteger itemid_ss = BigInteger.valueOf(item);
											//BigInteger contextoid, String componente, String filearea, BigInteger itemid, String filename
											// Se já existe algum arquivo igual a este que quero inserir
											String path = pathName(contextoId, "mod_folder", "content", itemid_folder, material.getNome());
											
											if(verificaPath(path)){
												//então pode inserir
											    for(Files file : fil){
													if(condicao == 0){
														armazenaFiles(file.getContenthash(),contextoDraft,"user","draft",itemid_ss, file.getFilename(), file.getFilesize(),file.getMimetype(), file.getSource(), file.getAuthor(), file.getLicense());
														armazenaFiles(hash_draft, contextoDraft,"user","draft",itemid_ss,".",size_draft,"NULL","NULL","NULL","NULL");											
														condicao=1;
														continue;
													}
													armazenaFiles(file.getContenthash(),contextoDraft,"user","draft",itemid_ss, file.getFilename(), file.getFilesize(),file.getMimetype(), file.getSource(), file.getAuthor(), file.getLicense());
												}
												condicao=0;
												// aqui vai ser inserido as duas tuplas novas.
												armazenaFiles(hash, contextoDraft, "user", "draft", itemid_ss, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												armazenaFiles(hash, contextoId, "mod_folder", "content", itemid_folder, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
											}else{
												//JOptionPane.showMessageDialog(null, "ja existe esse material no folder");
											}
											
									  }
								 }
							 }
						}
					*/
				}
				for(AtividadeNota atividade : c.getAtividadesNota()){
					/*
					if(atividade instanceof Questionario){
						Questionario questionario = (Questionario) atividade;
						d_Inicio.setTime(questionario.getDataInicio());
						d_Final.setTime(questionario.getDataFinal());
						
							if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
								Query query = entManager.createQuery("SELECT folder FROM Folder folder WHERE folder.name= ?1");
								String nome_p = "Pacote de Materiais - Questionário "+questionario.getName();
								query.setParameter(1, nome_p);
								List<Folder> folder = query.getResultList();
								
								BigInteger contextoId = new BigInteger("0");
								if(folder.isEmpty()){
								   contextoId = inserirFolder(c,questionario,new BigInteger("16"));
								}else{
									for (Folder f : folder) {								 
										contextoId = buscaContextoid(f.getId(),c.getId());
									}
								 }
							 
									 for(Material material : questionario.getMateriais()){
										 
										 	String hash = new String();
											Long ta = new Long(0);
											try {
												 hash = stringHexa(gerarHash(arquivoByte(material.getCaminho()),"SHA-1"));
												 ta = enviaArquivo(material.getCaminho(), hash);
											}catch (IOException e){
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											Query query2 = entManager.createQuery("SELECT files FROM Files files WHERE files.contextid= ?1");
											query2.setParameter(1, contextoId);
											List<Files> fil = query2.getResultList();
											
											Query qe = entManager.createQuery("SELECT files FROM Files files WHERE files.filename= ?1");
											qe.setParameter(1,".");
											List<Files> fp = qe.getResultList();
											if(!fil.isEmpty()){
												fil.removeAll(fp);	
											}
											
											//Variaveis constantes
											BigInteger contextoDraft = new BigInteger("5");
											String hash_draft = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
											BigInteger itemid_folder = new BigInteger("0");
											BigInteger size_draft = new BigInteger("0");
											BigInteger size = BigInteger.valueOf(ta);
											
											if(fil.isEmpty()){
											//	JOptionPane.showMessageDialog(null, "folder vazio");
												Long item;
												while(true){
													item = gerarItemid();
													if(verificaItemid(BigInteger.valueOf(item))){
														break;
													}
												}
												BigInteger itemid = BigInteger.valueOf(item); 									
												
												armazenaFiles(hash, contextoDraft, "user", "draft", itemid, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												armazenaFiles(hash_draft, contextoDraft, "user", "draft", itemid,".",size_draft, "NULL", "NULL", "NULL", "NULL");
												armazenaFiles(hash, contextoId, "mod_folder", "content", itemid_folder, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												armazenaFiles(hash_draft, contextoId, "mod_folder", "content", itemid_folder,".", size_draft,"document/unknown","NULL", "NULL", "NULL");
											}else{
												Long item;
												while(true){
													item = gerarItemid();
													if(verificaItemid(BigInteger.valueOf(item))){
														break;
													}
												}
												BigInteger itemid_ss = BigInteger.valueOf(item);
												//BigInteger contextoid, String componente, String filearea, BigInteger itemid, String filename
												// Se já existe algum arquivo igual a este que quero inserir
												String path = pathName(contextoId, "mod_folder", "content", itemid_folder, material.getNome());
												
												if(verificaPath(path)){
													//então pode inserir
												    for(Files file : fil){
														if(condicao == 0){
															armazenaFiles(file.getContenthash(),contextoDraft,"user","draft",itemid_ss, file.getFilename(), file.getFilesize(),file.getMimetype(), file.getSource(), file.getAuthor(), file.getLicense());
															armazenaFiles(hash_draft, contextoDraft,"user","draft",itemid_ss,".",size_draft,"NULL","NULL","NULL","NULL");											
															condicao=1;
															continue;
														}
														armazenaFiles(file.getContenthash(),contextoDraft,"user","draft",itemid_ss, file.getFilename(), file.getFilesize(),file.getMimetype(), file.getSource(), file.getAuthor(), file.getLicense());
													}
													condicao=0;
													// aqui vai ser inserido as duas tuplas novas.
													armazenaFiles(hash, contextoDraft, "user", "draft", itemid_ss, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
													armazenaFiles(hash, contextoId, "mod_folder", "content", itemid_folder, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												}else{
													//JOptionPane.showMessageDialog(null, "ja existe esse material no folder");
												}
												
										  }
									 }
							 }
				    }
					*/
				    if(atividade instanceof Tarefa){
				    		Tarefa tarefa = (Tarefa) atividade;
				    		for(Material material : tarefa.getMateriais()){
				    			if(material.getCaminho().isEmpty()){
									 continue;
								 }
				    		}
				    		d_Inicio.setTime(tarefa.getDataInicio());
							d_Final.setTime(tarefa.getDataFinal());
				    		if(d_Atual.after(d_Inicio) && d_Atual.before(d_Final)){
								Query query = entManager.createQuery("SELECT folder FROM Folder folder WHERE folder.name= ?1");			
								String nome_p = "Pacote de Materiais - Tarefa "+tarefa.getName();
								query.setParameter(1, nome_p);
								List<Folder> folder = query.getResultList();
								
								BigInteger contextoId = new BigInteger("0");
								if(folder.isEmpty()){
								   contextoId = inserirFolder(c,tarefa,new BigInteger("21"));
								}else{
									
									for (Folder f : folder) {								 
										contextoId = buscaContextoid(f.getId(),c.getId());
									}
									
								 }
								
								 for(Material material : tarefa.getMateriais()){
										 
										 if(material.getCaminho().isEmpty()){
											 continue;
										 }
										 	String hash = new String();
											Long ta = new Long(0);
											try {
												 hash = stringHexa(gerarHash(arquivoByte(material.getCaminho()),"SHA-1"));
												 ta = enviaArquivo(material.getCaminho(), hash);
											}catch (IOException e){
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											Query query2 = entManager.createQuery("SELECT files FROM Files files WHERE files.contextid= ?1");			
											query2.setParameter(1, contextoId);
											List<Files> fil = query2.getResultList();
											Query qe = entManager.createQuery("SELECT files FROM Files files WHERE files.filename= ?1");			
											qe.setParameter(1,".");
											List<Files> fp = qe.getResultList();
											if(!fil.isEmpty()){
												fil.removeAll(fp);	
											}
											
											//Variaveis constantes
											BigInteger contextoDraft = new BigInteger("5");
											String hash_draft = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
											BigInteger itemid_folder = new BigInteger("0");
											BigInteger size_draft = new BigInteger("0");
											BigInteger size = BigInteger.valueOf(ta);
											
											if(fil.isEmpty()){
												Long item;
												while(true){
													item = gerarItemid();
													if(verificaItemid(BigInteger.valueOf(item))){
														break;
													}
												}
												BigInteger itemid = BigInteger.valueOf(item); 									
												
												armazenaFiles(hash, contextoDraft, "user", "draft", itemid, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												armazenaFiles(hash_draft, contextoDraft, "user", "draft", itemid,".",size_draft, "NULL", "NULL", "NULL", "NULL");
												armazenaFiles(hash, contextoId, "mod_folder", "content", itemid_folder, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												armazenaFiles(hash_draft, contextoId, "mod_folder", "content", itemid_folder,".", size_draft,"document/unknown","NULL", "NULL", "NULL");
											}else{
												Long item;
												while(true){
													item = gerarItemid();
													if(verificaItemid(BigInteger.valueOf(item))){
														break;
													}
												}
												BigInteger itemid_ss = BigInteger.valueOf(item);
												//BigInteger contextoid, String componente, String filearea, BigInteger itemid, String filename
												// Se já existe algum arquivo igual a este que quero inserir
												String path = pathName(contextoId, "mod_folder", "content", itemid_folder, material.getNome());
												
												if(verificaPath(path)){
													//então pode inserir
												    for(Files file : fil){
														if(condicao == 0){
															armazenaFiles(file.getContenthash(),contextoDraft,"user","draft",itemid_ss, file.getFilename(), file.getFilesize(),file.getMimetype(), file.getSource(), file.getAuthor(), file.getLicense());
															armazenaFiles(hash_draft, contextoDraft,"user","draft",itemid_ss,".",size_draft,"NULL","NULL","NULL","NULL");											
															condicao=1;
															continue;
														}
														armazenaFiles(file.getContenthash(),contextoDraft,"user","draft",itemid_ss, file.getFilename(), file.getFilesize(),file.getMimetype(), file.getSource(), file.getAuthor(), file.getLicense());
													}
													condicao=0;
													// aqui vai ser inserido as duas tuplas novas.
													armazenaFiles(hash, contextoDraft, "user", "draft", itemid_ss, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
													armazenaFiles(hash, contextoId, "mod_folder", "content", itemid_folder, material.getNome(), size, material.getTipo(), material.getNome(), "Administrador Utilizador", "allrightsreserved");
												}else{
													//JOptionPane.showMessageDialog(null, "ja existe esse material no folder");
												}
												
										  }
									 }
									 
								 }	
				    	}
				}
				
				}catch (NullPointerException e) {
					ControleActions.setCriarFolderFiles(false);
				}
			}

		}
		JPAUtil.closeEntityManager();
		
		ControleActions.setCriarFolderFiles(false);
	}
	public boolean done() {
		return done;
	}

	public BigInteger inserirFolder(Curso c, Licao l, BigInteger m){

		//JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();
				
		Folder pacote = new Folder();
		pacote.setCourse(c.getId());
		pacote.setName("Pacote de Materiais - Lição "+l.getName());
		pacote.setIntro("Pacote de Materiais para auxiliar a resolução da Lição "+l.getName());
		pacote.setIntroformat(1);
		BigInteger revision = new BigInteger("1");
		pacote.setRevison(revision);
		pacote.setTimemodified(new Date().getTime());
		entManager.persist(pacote);

		ModuloCurso mc = new ModuloCurso();
		mc.setCourse(c.getId());
		BigInteger moduloid = new BigInteger("8");
		mc.setModule(moduloid);
		mc.setInstance(pacote.getId());
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
		
		//JPAUtil.closeEntityManager();
				
		return contxt.getId();
		
	}
	public BigInteger inserirFolder(Curso c, Questionario q, BigInteger m){

		//JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();
			
		Folder pacote = new Folder();
		pacote.setCourse(c.getId());
		pacote.setName("Pacote de Materiais - Questionário "+q.getName());
		pacote.setIntro("Pacote de Materiais para auxiliar a resolução do Questionário "+q.getName());
		pacote.setIntroformat(1);
		BigInteger revision = new BigInteger("1");
		pacote.setRevison(revision);
		pacote.setTimemodified(new Date().getTime());
		entManager.persist(pacote);

		ModuloCurso mc = new ModuloCurso();
		mc.setCourse(c.getId());
		BigInteger moduloid = new BigInteger("8");
		mc.setModule(moduloid);
		mc.setInstance(pacote.getId());
		BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course =" +c.getId()+" AND instance=" +q.getId()+ " AND module="+m).getSingleResult();
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

		//JPAUtil.closeEntityManager();
		return contxt.getId();
		
	}
	
	public BigInteger inserirFolder(Curso c, Tarefa t, BigInteger m){
		
		//JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();
		
		
		Query queryFolder = entManager.createQuery("SELECT folder FROM Folder folder");
		List<Folder> resultFolder = queryFolder.getResultList();
		
		BigInteger maiorFolder = new BigInteger("0");
		for(Folder fol : resultFolder){
			BigInteger v = fol.getId();
			maiorFolder = v.max(maiorFolder);
		}
		
		maiorFolder = maiorFolder.add(new BigInteger("1"));
		
		Folder pacote = new Folder();
		pacote.setId(maiorFolder);
		pacote.setCourse(c.getId());
		pacote.setName("Pacote de Materiais - Tarefa "+t.getName());
		pacote.setIntro("Pacote de Materiais para auxiliar a resolução do Tarefa "+t.getName());
		pacote.setIntroformat(1);
		BigInteger revision = new BigInteger("2");
		pacote.setRevison(revision);
		pacote.setTimemodified(new Date().getTime());
		entManager.persist(pacote);

		
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
		BigInteger moduloid = new BigInteger("6");
		mc.setModule(moduloid);
		mc.setInstance(pacote.getId());
		BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course =" +c.getId()+" AND instance=" +t.getId()+ " AND module="+m).getSingleResult();
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
		//JPAUtil.closeEntityManager();
		return contxt.getId();
		
	}
	
	public BigInteger buscaContextoid(BigInteger id_folder, BigInteger c) {
		//JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();
		BigInteger idM = (BigInteger) entManager.createNativeQuery("SELECT id FROM mdl_course_modules WHERE instance = "+id_folder + " AND course =" + c).getSingleResult();
		BigInteger idC = (BigInteger) entManager.createNativeQuery("SELECT id FROM mdl_context WHERE instanceid = "+idM + " AND contextlevel = 70").getSingleResult();
		//JPAUtil.closeEntityManager();
		return idC;
	}
	public static byte[] gerarHash(byte[] arquivo, String algoritmo) {
		  try {
		    MessageDigest md = MessageDigest.getInstance(algoritmo);
		    md.update(arquivo);
		    return md.digest();
		  } catch (NoSuchAlgorithmException e) {
		    return null;
		  }
	}

	private static String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
				int parteBaixa = bytes[i] & 0xf;
				if (parteAlta == 0) s.append('0');
				s.append(Integer.toHexString(parteAlta | parteBaixa));
			}
		return s.toString();
	}
	
	public byte[] arquivoByte(String file) throws IOException{
		File origem= new File(file);
		int tamanho = 0;
		tamanho = (int)origem.length();
		byte[] arquivo = new byte[tamanho];
		FileInputStream fis = new FileInputStream(origem);
		FileInputStream fisbytes = new FileInputStream(origem);
		fisbytes.read(arquivo, 0, tamanho);
		return arquivo;
	}

	public Long enviaArquivo(String file, String hash) throws IOException{
		File origem = new File(file);
		char[] hash_caractere = hash.toCharArray();
		String osName = System.getProperty("os.name"); 
		File destino_inicio = new File("");
		File destino_inter = new File("");
		File destino_inter2 = new File("");
		File destino_final = new File("");
		
		if(osName.contains("Windows") || osName.equals("Windows")){
			destino_inicio	= new File("C:\\xampp\\moodledata\\filedir");
			//primeiro diretorio
			destino_inter = new File(destino_inicio+"\\"+hash_caractere[0]+hash_caractere[1]);
			destino_inter.mkdir();
			//segundo diretorio
			destino_inter2 = new File(destino_inter+"\\"+hash_caractere[2]+hash_caractere[3]);
			destino_inter2.mkdir(); 
			//arquivo
			destino_final = new File(destino_inter2+"\\"+hash);
		
		}else if(osName.contains("Linux") || osName.equals("Linux")){
			destino_inicio	= new File("/var/moodledata/filedir");
			//primeiro diretorio
			destino_inter = new File(destino_inicio+"/"+hash_caractere[0]+hash_caractere[1]);
			destino_inter.mkdir();
			//segundo diretorio
			destino_inter2 = new File(destino_inter+"/"+hash_caractere[2]+hash_caractere[3]);
			destino_inter2.mkdir(); 
			//arquivo
			destino_final = new File(destino_inter2+"/"+hash);
		}
		 
		//Entrada
		FileInputStream fis = new FileInputStream(origem);
		//Saida
		FileOutputStream fos = new FileOutputStream(destino_final);
		
		// Obtém os canais por onde lemos/escrevemos nos arquivos
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		//copia para o destino
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		Long t = origem.length();
		return t;
	}
	public Long gerarItemid(){
		Long itemid = Long.valueOf("0");
		int n=999999999;
		Random gerador = new Random();
		itemid = (long) (gerador.nextInt(n)+1);
		return itemid;
	}
	// retorna true se não existe itemid com esse itemid gerado pelo random
	public boolean verificaItemid(BigInteger itemid) {
		//JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();
		Query query = entManager.createQuery("SELECT files FROM Files files WHERE files.itemid= ?1");
		query.setParameter(1, itemid);
		List<Files> files_item = query.getResultList();
		//JPAUtil.closeEntityManager();
		return files_item.isEmpty();
	}
	public boolean verificaPath(String path){
			//JPAUtil.beginTransaction();
			EntityManager entManager = JPAUtil.getEntityManager();
			Query query = entManager.createQuery("SELECT files FROM Files files WHERE files.pathnamehash= ?1");
			query.setParameter(1, path);
			List<Files> files_item = query.getResultList();
			//JPAUtil.closeEntityManager();
			return files_item.isEmpty();
	}
	public void armazenaFiles(String hash, BigInteger contextoId,String component,String filearea, BigInteger itemid, String filename,BigInteger size_final, String mimetype, String source, String author, String license){
		
		//JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();
		Files files = new Files();
		files.setContenthash(hash);
		files.setContextid(contextoId);
		files.setComponent(component);
		files.setFilearea(filearea);
		files.setItemid(itemid);
		files.setFilepath("/");
		files.setFilename(filename);
		BigInteger userid = new BigInteger("2");
		files.setUserid(userid);
		files.setFilesize(size_final);
		files.setMimetype(mimetype);
		BigInteger status = new BigInteger("0");
		files.setStatus(status);
		files.setSource(source);
		files.setAuthor(author);
		files.setLicense(license);
		files.setTimecreated(new Date().getTime());
		files.setTimemodified(new Date().getTime());
		String path = "/"+contextoId+"/"+component+"/"+filearea+"/"+itemid+"/"+filename;
		String pathfinal = stringHexa(gerarHash(path.getBytes(),"SHA-1"));
		
		files.setPathnamehash(pathfinal);
		entManager.persist(files);
	}
	
	public String pathName(BigInteger contextoid, String componente, String filearea, BigInteger itemid, String filename){
		String path = "/"+contextoid+"/"+componente+"/"+filearea+"/"+itemid+"/"+filename;
		String pathfinal = stringHexa(gerarHash(path.getBytes(),"SHA-1"));
		return pathfinal;
	}
}
		

