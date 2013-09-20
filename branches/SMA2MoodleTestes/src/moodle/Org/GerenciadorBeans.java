package moodle.Org;

import java.util.List;

import javax.swing.JOptionPane;

import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import dao.GerenciaCurso;
import dao.JPAUtil;

public class GerenciadorBeans extends ThreadsServicos{
	
	private GerenciaCurso gerenciador;
	
	
	public GerenciadorBeans(GerenciaCurso gerenciador, MoodleEnv env){
		super(env);
		this.gerenciador = gerenciador;	
	}
	
	
	@Override
	public void run() {
		
		
		
		
			try {
		
				
				while(mantemAtualizando){
					
					gerenciador.getCursos().clear();
					
					
					gerenciador.addCursos();
					List<Curso> listaCursos =  gerenciador.getCursos();
					
					try {
					
						JPAUtil.beginTransaction();
						
						int i = 1;
						for(Curso c: listaCursos ){	
	
							GerenciaCurso.addTutorCurso(c);
							GerenciaCurso.addTutoresCurso(c);
							GerenciaCurso.addAlunosCurso(c);
							GerenciaCurso.addProfessorCurso(c);
							GerenciaCurso.addLogsDoAluno(c);
							GerenciaCurso.addTags(c);
							GerenciaCurso.addAtividadeWiki(c);
							GerenciaCurso.addAtividadeQuestionario(c);
							GerenciaCurso.addAtividadeTarefas(c);
							GerenciaCurso.addAtividadeFerramentaExterna(c);
							GerenciaCurso.addAtividadeGlossario(c);
							GerenciaCurso.addAtividadeLaboratorioDeAvaliacao(c);
							GerenciaCurso.addAtividadeBancoDeDados(c);
							GerenciaCurso.addAtividadeEscolha(c);
							GerenciaCurso.addAtividadeChat(c);
							GerenciaCurso.addAtividadeForum(c);
							GerenciaCurso.calculaNotaGeralAlunos(c);
							GerenciaCurso.addGruposCurso(c);
							GerenciaCurso.addCursosPreRequisito(c);
			
							GerenciaCurso.addContatoDoCoordenador(c);
					
							System.err.println("*** "+i+" \nCurso: "+c.getId() + " -> "+c.getFullName() + "\n");
							i++;
							
							if(c.getProfessor() != null)
								System.out.println("Professor: " + c.getProfessor().getCompleteName()+"\n");
						
							
							/*
							if(c.getTutores() != null)
								for(Tutor t : c.getTutores()){
									System.out.println("Tutor: " + t.getCompleteName()+"\n");
								}
								
							else
								System.out.println("Tutor: Sem tutor");
							
							
							System.out.println("\nAlunos: \n");
							for(Aluno a : c.getAlunos()){
								System.out.println("- " + a.getCompleteName());
							}
						
							System.out.println("\nAtividade Nota: \n");
							for(AtividadeNota atNota : c.getAtividadesNota()){
								System.out.println("- " + atNota.getName());
							}
							
							System.out.println("\nAtividade Participacao: \n");
							for(AtividadeParticipacao atPart : c.getAtividadesParticipacao()){
								System.out.println("- " + atPart.getName());
							}
							*/	
						
						}
						
						
					} catch (Exception e) {
						JPAUtil.rollback();
						e.printStackTrace();
						System.out.println("\n************** ERRO ************\nMensagem: " + e.getMessage());
						System.out.println("Causa: " + e.getCause());
					
					} finally {
						JPAUtil.closeEntityManager();
					}

					/*
					
					for(Curso c: listaCursos ){	
						
						System.out.println(c.getFullName());
						
						
						
						System.out.println("***********\nCurso: " + c.getFullName() + "\n");
						
						
						System.out.println("Tutor: " + c.getTutor().getCompleteName());
						
						/*
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						*/
						
						
						/*
						Set<Aluno> listaAlunos =  c.getAlunos();
					
						try{
							System.out.println("\n\nTutor: "+c.getTutor().getCompleteName());
						}catch(NullPointerException e){
							System.out.println("Tutor : N�o definido");
						}
						System.out.printf("\nAlunos:\n");
						
						for(Aluno a : listaAlunos){
							System.out.println(a.getCompleteName());
				
							System.out.println("\n");
			
								
							System.out.println("-------------------------LOGS-------------------------");
							System.out.println("\n");
							
							List<Log> logs = a.getLogs();
							for (Log log : logs) {
									System.out.println(log);
							}
							
							System.out.println("\n\n");
							
							
							System.out.println("-------------------------TAGS-------------------------");

							List<Tag> tags = a.getTags();
							
							for(Tag t : tags){
								System.out.println(t.getName());
							}
						}
						
						System.out.println("\n\nAtividades:\n");
						Set<AtividadeNota> listaAtividadeN = c.getAtividadesNota();
						Set<AtividadeParticipacao> listaAtividadeP = c.getAtividadesParticipacao();
						for(AtividadeNota a : listaAtividadeN){
						
							System.out.println("Nome: " + a.getName());
							System.out.println("Data inicio: " + a.getDataInicio().toString());
							System.out.println("Data final: " + a.getDataFinal().toString());
						
							Map<Aluno, BigDecimal> alNotasMap = a.getAlunosComNotas();
							
							if(!alNotasMap.isEmpty()){
							
								for(Map.Entry<Aluno, BigDecimal> results : alNotasMap.entrySet()){
									System.out.println(results.getKey().getFirstName() + " " + results.getValue());
								}
							}
							
							if(a instanceof LaboratorioDeAvaliacao){
								Map<Aluno, BigDecimal> alNotasMap2 = ((LaboratorioDeAvaliacao) a).getNota2();
								
								if(!alNotasMap2.isEmpty()){
								
									for(Map.Entry<Aluno, BigDecimal> results : alNotasMap2.entrySet()){
										System.out.println(results.getKey().getFirstName() + " " + results.getValue());
									}
								}
							}
						}
						
						System.out.println("--------------*********************-------------------");
						System.out.println();
						
						for(AtividadeParticipacao a : listaAtividadeP){
						try{
							if(a instanceof Forum){
								Forum f =  (Forum)a;
								System.out.println("Nome: " + f.getName()+" - Avaliativo ("+f.isAvaliativo()+")");
								System.out.println("Data inicio: " + f.getDataInicio().toString());
								System.out.println("Data final: " + f.getDataFinal().toString());	
								System.out.println("Data atual: "+(new Date().toString()));
									if(f.isTutorParticipa())
										System.out.println("\nParticipa��o do tutor: "+f.isTutorParticipa()+" "+f.getUltimaPartipacao().toString());
									else
										System.out.println("N�o h� participa��o de tutores nesse forum");
									
								
										System.out.println("�ltimo Post "+f.getUltimoPost()+" <");
									
							}else{
								System.out.println("Nome: " + a.getName());
								System.out.println("Data inicio: " + a.getDataInicio().toString());
								System.out.println("Data final: " + a.getDataFinal().toString());
							
							}
						}catch(NullPointerException e){
							System.out.println("N�o h� participa��o");
						}
							Set<Aluno> alunos = a.getAlunosParticipantes();
							for(Aluno al : alunos){
								try{
								System.out.print(al.getCompleteName() + " ");

								}catch(NullPointerException e){
									
									
									e.printStackTrace();
								}
							}
							
							
							System.out.println();
							
							Map<Aluno, BigDecimal> alNotasMap = a.getAlunosComNotas();
							
							if(!alNotasMap.isEmpty()){
							
								for(Map.Entry<Aluno, BigDecimal> results : alNotasMap.entrySet()){
									System.out.println(results.getKey().getFirstName() + " " + results.getValue());
								}
							}
							
							
							
							System.out.println("---------------------");
							System.out.println();
						}
						
						System.out.println("***************************************");
						
					
						
						
						
						
						System.out.printf("\n\n FIM CURSO \n\n");
						
						Set<Aluno> listaAlunos =  c.getAlunos();
						
						System.out.printf("\nAlunos:\n");
						
						for(Aluno a : listaAlunos){
							System.out.println(a.getCompleteName());
				
						}

						
						
						
						
						
						
						List<Mensagem> mensagens = environment.getMensagens();
						
						
						if(!mensagens.isEmpty()){
							
							System.out.println("\n ADICIONANDO MENSAGENS \n");
							
							
							EntityManager managerDao = Dao.getEntityManager();
							managerDao.getTransaction().begin();
							
							
							synchronized (mensagens) {
							
								for(Mensagem msg : mensagens){
									managerDao.persist(msg);
									
									
									
								}
							
							}
							
							managerDao.getTransaction().commit();
							
							
							
							
							mensagens.clear();
						}
						
						
						
						List<Grupo> grupos = environment.getGrupos();
						
						if(!grupos.isEmpty()){
							
							System.out.println("\n ADICIONANDO GRUPOS \n");
							
							EntityManager managerDao = Dao.getEntityManager();
							managerDao.getTransaction().begin();
							
							synchronized (grupos) {
							
								for(Grupo g : grupos){
									managerDao.persist(g);
					
									for(Aluno a : g.getMembros()){
										
										
										MembrosGrupo m = new MembrosGrupo();
										m.setGroupid(g.getId());
										m.setUserid(a.getId());
										m.setTimeadded(System.currentTimeMillis());
										
										managerDao.persist(m);
										
									}
									
									
									
									
								}
							
								
								managerDao.getTransaction().commit();
								
								grupos.clear();
								
								
							}
								
							
						}
						
						
					}
					
					*/
					
					environment.setMantemAgentesAtivos(true);
					Thread.sleep(10 * 1000);
					environment.setMantemAgentesAtivos(false);
				}
				
				
				
			} catch (InterruptedException e) {
				System.out.println("Exception sleep thread");
				System.exit(1);
			}finally{
				environment.setMantemAgentesAtivos(false);
				
			}
			
		
				
	}

}
