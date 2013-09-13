package dao;


import java.awt.JobAttributes;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Coordenador;
import moodle.dados.Curso;
import moodle.dados.Log;
import moodle.dados.Professor;
import moodle.dados.Tag;
import moodle.dados.Tutor;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.atividades.Chat;
import moodle.dados.atividades.DataBase;
import moodle.dados.atividades.Escolha;
import moodle.dados.atividades.FerramentaExterna;
import moodle.dados.atividades.Forum;
import moodle.dados.atividades.Glossario;
import moodle.dados.atividades.LaboratorioDeAvaliacao;
import moodle.dados.atividades.Licao;
import moodle.dados.atividades.Questionario;
import moodle.dados.atividades.Tarefa;
import moodle.dados.atividades.Wiki;
import moodle.dados.grupos.Grupo;

public class GerenciaCurso {

	private List<Curso> cursos;
	 
	
	public GerenciaCurso() {
		cursos = new ArrayList<Curso>();
		
	}
	
	public List<Curso> getCursos(){
		return cursos;
	}
	
	
	public void addCursos(){
		CursoDAO dao = new CursoJpaDAO();
		cursos = dao.findAll();
	}

	public static <T extends AtividadeParticipacao> List<T> addAtividadeParticipacaoCurso(String query, Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		Query q = manager.createNamedQuery(query);
		q.setParameter(1, curso.getId());
		
		List<T> atividades = q.getResultList();
		
		curso.addAtividadeParticipacao(atividades);
		
		
		return atividades;
	}
	
	public static <T extends AtividadeNota> List<T> addAtividadeNotaCurso(String query, Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		Query q = manager.createNamedQuery(query);
		q.setParameter(1, curso.getId());
		
		List<T> atividades = q.getResultList();
		
		curso.addAtividadeNota(atividades);
		
		
		return atividades;
	}
	
	
	public static void addAlunosCurso(Curso curso){

		EntityManager manager = JPAUtil.getEntityManager();
		
		Query query = manager.createNativeQuery("SELECT userid FROM mdl_role_assignments WHERE roleid=5 and contextid = (SELECT id FROM mdl_context WHERE contextlevel=50 and instanceid = ?1)");
		query.setParameter(1, curso.getId());
		
		
		List<BigInteger> usersIds = query.getResultList();
		
		Query queryAluno = manager.createNamedQuery("Aluno.findById");
		
		for(BigInteger idUser : usersIds){
			
			try{

				queryAluno.setParameter(1, idUser);
				Aluno a = (Aluno)queryAluno.getSingleResult();
				curso.addAluno(a);

			}catch(NoResultException e){
				
				continue;
				
			}
			
		}
		
	}
	
	public static void calculaNotaGeralAlunos(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		for(Aluno aluno : curso.getAlunos()){
			
			try{
				
				Query query2 = manager.createNativeQuery("SELECT finalgrade FROM mdl_grade_grades WHERE userid = ?1 and itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?2 and itemtype = 'course')");
				query2.setParameter(1, aluno.getId());
				query2.setParameter(2, curso.getId());
								
				curso.getNotaGeralAlunos().put(aluno,(BigDecimal)query2.getSingleResult());
				
			}catch(NoResultException e){
				continue;
			}
			
		}
		
		
		
		
	}
	
	public static void addGruposCurso(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		Query query = manager.createNamedQuery("Grupo.findByCourse");
		query.setParameter(1, curso.getId());
		
		
		List<Grupo> grupos = query.getResultList();
		
		
		if(!grupos.isEmpty()){
			curso.addGrupo(grupos);
			
			for(Grupo g : grupos){
				
				
				Query queryIdAluno = manager.createNativeQuery("SELECT userid FROM mdl_groups_members WHERE groupid = ?1");
				queryIdAluno.setParameter(1, g.getId());
				
				List<BigInteger> ids = queryIdAluno.getResultList();
				
				if(!ids.isEmpty()){
					
					for(BigInteger id : ids){
						Query queryAluno = manager.createNamedQuery("Aluno.findById");
						queryAluno.setParameter(1, id);
						
						Aluno al = (Aluno)queryAluno.getSingleResult();
						
						g.addMembro(al);
					}
					
				}
				
			}
		}
		
		
	}
	
	public static void addCursosPreRequisito(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		Query queryIdsPreRequisito = manager.createNativeQuery("SELECT courseInstance FROM mdl_course_completion_criteria WHERE course = ?1");
		queryIdsPreRequisito.setParameter(1, curso.getId());
		
		List<BigInteger> ids = queryIdsPreRequisito.getResultList();
		
		if(!ids.isEmpty()){
		
			for(BigInteger id : ids){
				Query q = manager.createNamedQuery("Curso.findById");
				q.setParameter(1, id);
				
				try{
				
					Curso c = (Curso)q.getSingleResult();
					curso.getCursosPreRequisito().add(c);
				}catch(NoResultException e){
					continue;
				}
				
				
			}
		}
		
		
	}
	
	
	
	public static void addLogsDoAluno(Curso c){

		EntityManager manager = JPAUtil.getEntityManager();
		
		Set<Aluno> alunos = c.getAlunos();
	
		for (Aluno aluno : alunos) {
			Query query = manager.createNamedQuery("Log.findByCourse");
			query.setParameter(1, c.getId());
			query.setParameter(2, aluno.getId());
			List<Log> logs = query.getResultList();
			
			if(!aluno.getLogs().containsAll(logs))
				aluno.addLogs(logs);
			
			Query query1 = manager.createNamedQuery("Log.findByUm");
			query1.setParameter(1, aluno.getId());
			List<Log> logs1 = query1.getResultList();
			
			if(!aluno.getLogs().containsAll(logs1))
				aluno.addLogs(logs1);
		}
	
	}
	
	public static void addTags(Curso c) {
		Set<Aluno> alunos = c.getAlunos();

		EntityManager manager = JPAUtil.getEntityManager();
		
		for (Aluno a : alunos) {

			Query query = manager.createNativeQuery("SELECT tagid FROM mdl_tag_instance WHERE itemid = ?1");
			query.setParameter(1, a.getId());

			List<BigInteger> idsItens = query.getResultList();
			List<Tag> tags = new ArrayList<Tag>();
			
			for (BigInteger item : idsItens) {

				Query query2 = manager.createNamedQuery("Tag.findById");
				query2.setParameter(1, item.longValue());
				try {
					Tag tag = (Tag) query2.getSingleResult();
					tags.add(tag);
				} catch (NoResultException e) {

				}
			}

			if (!a.getTags().containsAll(tags)) {
				a.addTags(tags);
			}

		}

	}
	

	public static void addProfessorCurso(Curso curso) {
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		try {

			Query query2 = manager.createNativeQuery("SELECT userid FROM mdl_role_assignments WHERE roleid=4 and contextid = (SELECT id FROM mdl_context WHERE contextlevel=50 and instanceid = ?1)");
			query2.setParameter(1, curso.getId());
			BigInteger idProfessor = null;
			
			idProfessor = (BigInteger) query2.getSingleResult();
			
			Query queryProfessor = manager.createNamedQuery("Professor.findById");
				
			queryProfessor.setParameter(1, idProfessor);
				
			Professor p = (Professor) queryProfessor.getSingleResult();
			
			curso.addProfessor(p);
		

		} catch (NoResultException e) {
			System.out.println("Problema ao adicionar Professor do Curso");
		} catch (NonUniqueResultException e) {
			System.out.println("Mais de um professor no Curso");
		}
		
	}
	
	public static void addTutorCurso(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		try{
			Query query = manager.createNativeQuery("SELECT userid FROM mdl_role_assignments WHERE roleid=10 and contextid = (SELECT id FROM mdl_context WHERE contextlevel=50 and instanceid = ?1)");
			query.setParameter(1, curso.getId());
			
			BigInteger idTutor = (BigInteger) query.getSingleResult();
			
			Query queryTutor = manager.createNamedQuery("Tutor.findById");
			queryTutor.setParameter(1, idTutor);
			Tutor t = (Tutor) queryTutor.getSingleResult();
			curso.addTutor(t);
			
		}catch(NonUniqueResultException e){
	
			return;
		}catch(NoResultException e){

		}
		
		
		
		/*
		Query query = manager.createNativeQuery("SELECT id FROM mdl_context WHERE contextlevel=50 and instanceid = ?1");
		query.setParameter(1, curso.getId());
		
		
		
		
		try{
			
			Query query2 = manager.createNativeQuery("SELECT userid FROM mdl_role_assignments WHERE roleid=4 and contextid = ?1");
			query2.setParameter(1, query.getSingleResult());
			BigInteger idTutor = null;
			try{
				idTutor = (BigInteger) query2.getSingleResult();
			}catch(NonUniqueResultException e){
				return;
			}
			
			Query queryTutor = manager.createNamedQuery("Tutor.findById");

			queryTutor.setParameter(1, idTutor);

			Tutor t = (Tutor) queryTutor.getSingleResult();
			
			curso.addTutor(t);
		
		}catch(NoResultException e){
			
		
		
		}
		
		*/
		
	}
	
	

	public static void addTutoresCurso(Curso c){
		
		EntityManager manager = JPAUtil.getEntityManager();
		try{
			Query query = manager.createNativeQuery("SELECT userid FROM mdl_role_assignments WHERE roleid=10 and contextid = (SELECT id FROM mdl_context WHERE contextlevel=50 and instanceid = ?1)");
			query.setParameter(1, c.getId());
			
			List<BigInteger> idsTutores = query.getResultList();
			
			Query qyeryTutor = manager.createNamedQuery("Tutor.findById");
			
			for(BigInteger idTutor : idsTutores){
			
				qyeryTutor.setParameter(1, idTutor);
				Tutor t = (Tutor) qyeryTutor.getSingleResult();
				c.addTutor(t);
			}
			
		}catch(NoResultException e){
			
		}
	
	}

	public static void addContatoDoCoordenador(Curso c){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		try{
		
			Query query = manager.createNamedQuery("Coordenador.findByCourse");
			query.setParameter(1, c.getId());
		
			Coordenador coo = (Coordenador) query.getSingleResult();
		
			Tutor t = c.getTutor();
			
			
			t.setCoordenador(coo);
		
		}catch(NoResultException|NullPointerException e){
			System.out.println("Não há coordenador no curso");
		}
	}
	
	
	/*
	
	public static void addAtividadePesquisaAvaliacao(Curso curso){
		
		List<PesquisaAvaliacao> pesquisas = addAtividadeParticipacaoCurso("PesquisaAvaliacao.findByCourse", curso);
		
		List<Aluno> alunosCurso = curso.getAlunos();
		
		Query queryAluno = MoodleDAO.findNativeQueryParameterized("SELECT id FROM mdl_survey_answers WHERE survey = ?1 and userid = ?2 LIMIT 1");
		
		if(!pesquisas.isEmpty()){
			
			for(PesquisaAvaliacao pa : pesquisas){
				
				queryAluno.setParameter(1, pa.getId());
				
				for(Aluno a : alunosCurso){
					
					queryAluno.setParameter(2, a.getId());
					
					
					if(!queryAluno.getResultList().isEmpty() && !pa.getAlunosParticipantes().contains(a) ){
						pa.addAlunosParticipantes(a);				
					}
				}
			}			
		}
	}
	
	*/
	
	
	public static void addAtividadeBancoDeDados(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		List<DataBase> bases = addAtividadeParticipacaoCurso("DataBase.findByCourse", curso);
				
		
		if(!bases.isEmpty()){
		
			
			
			
			
			for(DataBase db : bases){	
				
								
				if(!db.isAvaliativo()){
			
					Query queryAlunoId = manager.createNativeQuery("SELECT userid FROM mdl_data_records WHERE dataid = ?1");
					
					queryAlunoId.setParameter(1, db.getId());
					List<BigInteger> ids = queryAlunoId.getResultList();
					
					if(!ids.isEmpty()){
						
						for(BigInteger id : ids){
							Query queryAluno = manager.createNamedQuery("Aluno.findById");
							queryAluno.setParameter(1, id);
							
							Aluno al = (Aluno)queryAluno.getSingleResult();
							
							db.addAlunosParticipantes(al);
						}
						
					}
					
					
					
					
					
				}else{
			
					Query queryDbgrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'data')");
					
					queryDbgrade.setParameter(1, curso.getId());
					queryDbgrade.setParameter(2, db.getId());
					
					List<Object[]> usuariosComNota = queryDbgrade.getResultList();
					
					for(Object[] tupla : usuariosComNota){
						
						Query q = manager.createNamedQuery("Aluno.findById");
						q.setParameter(1, (BigInteger)tupla[0]);
						
						Aluno al = (Aluno)q.getSingleResult();
						
						db.addAlunoComNota(al, (BigDecimal)tupla[1]);
						
					}
					
					
					
				}
					
				
			}
		}
	}
	
	
	public static void addAtividadeGlossario(Curso curso) {
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		List<Glossario> glossarios = addAtividadeParticipacaoCurso("Glossario.findByCourse", curso);
		
		
		
		if(!glossarios.isEmpty()){
		
			
			
			
			
			for(Glossario gl : glossarios){
				try{
				
				if(!gl.isAvaliativo()){
			
					
					Query queryAlunoId = manager.createNativeQuery("SELECT userid FROM mdl_glossary_entries WHERE glossaryid =?1");
					queryAlunoId.setParameter(1, gl.getId());
					List<BigInteger> ids = queryAlunoId.getResultList();
					
					if(!ids.isEmpty()){
						
						for(BigInteger id : ids){
							Query queryAluno = manager.createNamedQuery("Aluno.findById");
							queryAluno.setParameter(1, id);
							
							Aluno al = (Aluno)queryAluno.getSingleResult();
							
							gl.addAlunosParticipantes(al);
						}
						
					}
					
				}else{
			
					
					Query queryGlgrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'glossary')");
					
					queryGlgrade.setParameter(1, curso.getId());
			
					queryGlgrade.setParameter(2, gl.getId());
					
					
					
					
					
					List<Object[]> usuariosComNota = queryGlgrade.getResultList();
					
					for(Object[] tupla : usuariosComNota){
						
						Query q = manager.createNamedQuery("Aluno.findById");
					
						q.setParameter(1, (BigInteger)tupla[0]);
						
						Aluno al = (Aluno)q.getSingleResult();
						
						gl.addAlunoComNota(al, (BigDecimal)tupla[1]);
						
					}
					
					
				}
				
				}catch(NoResultException e){
					continue;
				}
				
			}
		}
		
	}
	
	
	
	
	public static void addAtividadeEscolha(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		List<Escolha> escolhas = addAtividadeParticipacaoCurso("Escolha.findByCourse", curso);
		
		if(!escolhas.isEmpty()){
			
			Query queryAlunoId = manager.createNativeQuery("SELECT userid FROM mdl_choice_answers WHERE choiceid =?1");
			
			for(Escolha esc : escolhas){
				
				try{	
				queryAlunoId.setParameter(1, esc.getId());
				List<BigInteger> ids = queryAlunoId.getResultList();
					
				if(!ids.isEmpty()){
						
				for(BigInteger id : ids){
					Query queryAluno = manager.createNamedQuery("Aluno.findById");
					queryAluno.setParameter(1, id);
							
					Aluno al = (Aluno)queryAluno.getSingleResult();
							
					esc.addAlunosParticipantes(al);
					}
						
				}
				
				}catch(NoResultException e){
					continue;
				}
					
			}
			
		}
		
		
		
	}
	
	public static void addAtividadeChat(Curso curso){
		List<Chat> chats = addAtividadeParticipacaoCurso("Chat.findByCourse", curso);
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		
		if(!chats.isEmpty()){
			
			Query query = manager.createNativeQuery("SELECT userid FROM mdl_chat_messages_current WHERE chatid = ?1");
			
			for(Chat chat : chats){
				query.setParameter(1, chat.getId());
				List<BigInteger> ids = query.getResultList();
				
				if(!ids.isEmpty()){
				
					try{
					
					for(BigInteger id : ids){
                    
						Query queryAluno = manager.createNamedQuery("Aluno.findById");
                        queryAluno.setParameter(1, id);
                        Aluno al = (Aluno)queryAluno.getSingleResult();                
                                
                        chat.addAlunosParticipantes(al);
                                        
                    }
					
					}catch(NoResultException e){
						continue;
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static void addAtividadeForum(Curso curso) {

		List<Forum> foruns = addAtividadeParticipacaoCurso("Forum.findByCourse", curso);

		EntityManager manager = JPAUtil.getEntityManager();
		
		
		BigInteger idTutor = null;
		try {
			idTutor = curso.getTutor().getId();
		} catch (NullPointerException e) {

		}

		if (!foruns.isEmpty()) {

			try{
			
			for (Forum forum : foruns) {

				if (!forum.isAvaliativo()) {
					
													
							Query query = manager.createNativeQuery("SELECT id FROM mdl_forum_posts WHERE discussion IN (SELECT id FROM mdl_forum_discussions WHERE forum = ?1)");
							query.setParameter(1, forum.getId());
							
							List<BigInteger> idsPosts = query.getResultList();
							
							for (BigInteger id2 : idsPosts) {

								
									 Query query2 = manager.createNativeQuery("SELECT userid FROM mdl_forum_posts WHERE id = ?1");
									 query2.setParameter(1, id2);
									 
									 BigInteger idAluno = (BigInteger) query2.getSingleResult();
										
									if (idAluno == idTutor) {
										
										forum.setTutorParticipa(true);
										Query query3 = manager.createNativeQuery("SELECT created FROM mdl_forum_posts WHERE id = ?1");
										query3.setParameter(1, id2);
										BigInteger dataPost = (BigInteger) query3.getSingleResult();
										long ultimaPartipacao = dataPost.longValue();
										forum.setUltimaPartipacao(ultimaPartipacao);
										
									} else {

										Query queryAluno = manager.createNamedQuery("Aluno.findById");

										queryAluno.setParameter(1, idAluno);

										Aluno al = (Aluno) queryAluno.getSingleResult();
											
										Query query3 = manager.createNativeQuery("SELECT created FROM mdl_forum_posts WHERE id = ?1");
										query3.setParameter(1, id2);
										BigInteger dataPost = (BigInteger) query3.getSingleResult();
										
										forum.setUltimoPost(dataPost.longValue());
											
										if (!forum.getAlunosParticipantes().contains(al)){
											forum.addAlunosParticipantes(al);
										}
									}
								

							}
							
						

					} else {
						
						
							Query query = manager.createNativeQuery("SELECT id FROM mdl_forum_posts WHERE discussion IN (SELECT id FROM mdl_forum_discussions WHERE forum = ?1)");
							query.setParameter(1, forum.getId());
							
							List<BigInteger> idsPosts = query.getResultList();
							
							for (BigInteger id2 : idsPosts) {
									
										Query query2 = manager.createNativeQuery("SELECT userid FROM mdl_forum_posts WHERE id = ?1");
										 query2.setParameter(1, id2);
										 
										 BigInteger idAluno = (BigInteger) query2.getSingleResult();
										 
										if (idAluno == idTutor){ 
											forum.setTutorParticipa(true);
											Query query3 = manager.createNativeQuery("SELECT created FROM mdl_forum_posts WHERE id = ?1");
											query3.setParameter(1, id2);
											BigInteger dataPost = (BigInteger) query3.getSingleResult();
											long ultimaPartipacao = dataPost.longValue();
											forum.setUltimaPartipacao(ultimaPartipacao);
										}else{
											Query query3 = manager.createNativeQuery("SELECT created FROM mdl_forum_posts WHERE id = ?1");
											query3.setParameter(1, id2);
											BigInteger dataPost = (BigInteger) query3.getSingleResult();
											forum.setUltimoPost(dataPost.longValue());
										}
									
								}
							}
			
							
							Query queryFrgrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'forum')");
							queryFrgrade.setParameter(1, curso.getId());
							queryFrgrade.setParameter(2, forum.getId());
			
								
		
							List<Object[]> usuariosComNota = queryFrgrade.getResultList();
			
								for (Object[] tupla : usuariosComNota) {
								
									Query q = manager.createNamedQuery("Aluno.findById");
			
									q.setParameter(1, (BigInteger) tupla[0]);
			
									Aluno al = (Aluno) q.getSingleResult();
								
									forum.addAlunoComNota(al, (BigDecimal) tupla[1]);
								}
			
							
						
							
				}
			
			}catch (NoResultException e) {
				
			}
															
		}
			
			
		
		
		
	}
		
	/*
	
	public static void addAtividadeScorm(Curso curso){
		
		List<Scorm> scorms = addAtividadeParticipacaoCurso("Scorm.findByCourse", curso);
		
		
	}open
	
	*/
	
	public static void addAtividadeWiki(Curso curso){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		List<Wiki> wikis = addAtividadeParticipacaoCurso("Wiki.findByCourse", curso);
		
		for(Wiki wi : wikis){
			
			try{
			
				if(wi.getWikimode().equals("collaborative")){
					
										
						Query queryUserWiki = manager.createNativeQuery("SELECT userid FROM mdl_wiki_versions WHERE pageid = (SELECT id FROM mdl_wiki_subwikis WHERE wikiid = ?1 LIMIT 1)");
						queryUserWiki.setParameter(1, wi.getId());
						
						@SuppressWarnings("unchecked")
						List<BigInteger> idAlunos = queryUserWiki.getResultList();
						
						for(BigInteger idAluno : idAlunos){
							Query cadaAluno = manager.createNamedQuery("Aluno.findById");
							cadaAluno.setParameter(1, idAluno);
							
							Aluno alunoWiki = (Aluno)cadaAluno.getSingleResult();
							
							wi.getAlunosParticipantes().add(alunoWiki);
						}
							
					
				}else{
					
					Query queryUserWiki = manager.createNativeQuery("SELECT userid FROM mdl_wiki_subwikis WHERE wikiid = ?1");
					queryUserWiki.setParameter(1, wi.getId());
					
					List<BigInteger> idAlunos = queryUserWiki.getResultList();
					
					for(BigInteger idAluno : idAlunos){
						Query cadaAluno = manager.createNamedQuery("Aluno.findById");
						cadaAluno.setParameter(1, idAluno);
						
						Aluno alunoWiki = (Aluno)cadaAluno.getSingleResult();
						
						wi.getAlunosParticipantes().add(alunoWiki);
					}
					
					
					
				}
			
			}catch(NoResultException e){
				continue;
			}
		}
	
		
	}
	
	public static void addAtividadeQuestionario(Curso curso){
	
		EntityManager manager = JPAUtil.getEntityManager();
		
		List<Questionario> questionarios = addAtividadeNotaCurso("Questionario.findByCourse", curso);
		
		
		if(!questionarios.isEmpty()){
					
			Query queryQuesgrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'quiz')");
			addAlunoNota(curso.getId(), questionarios, queryQuesgrade);
	
		}
		
	}
	
	
	
	public static void addAtividadeTarefas(Curso curso) {
		
		List<Tarefa> tarefas = addAtividadeNotaCurso("Tarefa.findByCourse", curso);
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		
		if(!tarefas.isEmpty()){
		
			Query queryTargrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'assign')");
			addAlunoNota(curso.getId(), tarefas, queryTargrade);
					
		}
		
	}
	
	
	
	public static void addAtividadeFerramentaExterna(Curso curso) {
	
	List<FerramentaExterna> ferramentas = addAtividadeNotaCurso("FerramentaExterna.findByCourse", curso);
		
	EntityManager manager = JPAUtil.getEntityManager();
	
		if(!ferramentas.isEmpty()){
		
			Query queryFergrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'lti')");
			addAlunoNota(curso.getId(), ferramentas, queryFergrade);
				
			
		}
		
	}
	
	
	public static void addAlunoNota(BigInteger idCurso, List<? extends Atividade> list, Query q1){
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		for(Atividade at : list){
			
			q1.setParameter(1, idCurso);
			q1.setParameter(2, at.getId());
			
			List<Object[]> usuariosComNota = q1.getResultList();
			
			for(Object[] tupla : usuariosComNota){
				
				Query q = manager.createNamedQuery("Aluno.findById");
				q.setParameter(1, (BigInteger)tupla[0]);
				
				Aluno al = (Aluno)q.getSingleResult();
				
				at.addAlunoComNota(al, (BigDecimal)tupla[1]);
				
			}
			
			
		
			
		}
	
		
	}
	
	
	
	
	
	public static void addAtividadeLicao(Curso curso) {
	
		List<Licao> licoes = addAtividadeNotaCurso("Licao.findByCourse", curso);
	
		EntityManager manager = JPAUtil.getEntityManager();
		
		
		if(!licoes.isEmpty()){
		
			Query queryLicgrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid = (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'lesson' )");
			addAlunoNota(curso.getId(), licoes, queryLicgrade);
				
			
		}
		
	}
	
	
	
	
	
	public static void addAtividadeLaboratorioDeAvaliacao(Curso curso) {
		
		List<LaboratorioDeAvaliacao> labs = addAtividadeNotaCurso("LaboratorioDeAvaliacao.findByCourse", curso);
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		
		
		if(!labs.isEmpty()){
			
			
			for(LaboratorioDeAvaliacao lab : labs){
				try{
				Query queryLabgrade = manager.createNativeQuery("SELECT  userid, finalgrade FROM mdl_grade_grades WHERE itemid in (SELECT id FROM mdl_grade_items WHERE courseid = ?1 and iteminstance = ?2 and itemmodule = 'workshop')");
				
				queryLabgrade.setParameter(1, curso.getId());
				queryLabgrade.setParameter(2, lab.getId());
				
				int cont = 0;
							
				List<Object[]> usuariosComNota = queryLabgrade.getResultList();
					
				for(Object[] tupla : usuariosComNota){
						
					Query q = manager.createNamedQuery("Aluno.findById");
					q.setParameter(1, (BigInteger)tupla[0]);
						
					Aluno al = (Aluno)q.getSingleResult();
						
					if(cont == 0){
							lab.addAlunoComNota(al, (BigDecimal)tupla[1]);		
						
					}else{
							lab.addNota2(al, (BigDecimal)tupla[1]);
					}
						
						
					}
					cont++;
					
				}catch(NoResultException e){
					continue;
				}
					
			}
		}
	
	}
	
	
}