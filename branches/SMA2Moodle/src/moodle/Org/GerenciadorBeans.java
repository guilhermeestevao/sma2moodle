package moodle.Org;

import java.math.BigInteger;
import java.util.List;
import java.util.ResourceBundle.Control;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import moodle.Agentes.actions.ControleActions;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.grupos.Grupo;
import moodle.dados.grupos.MembrosGrupo;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import dao.JPAUtil;

public class GerenciadorBeans extends ThreadsServicos {

	private GerenciaCurso gerenciador;

	public GerenciadorBeans(GerenciaCurso gerenciador, MoodleEnv env) {
		super(env);
		this.gerenciador = gerenciador;
	}

	@Override
	public void run() {

		try {

			while (mantemAtualizando) {

				gerenciador.getCursos().clear();

				gerenciador.addCurso(new BigInteger("203"));
//				gerenciador.addCurso(new BigInteger("282"));
//				gerenciador.addCurso(new BigInteger("242"));
//				gerenciador.addCurso(new BigInteger("232"));
//				gerenciador.addCurso(new BigInteger("183"));
//				gerenciador.addCurso(new BigInteger("220"));
//				gerenciador.addCurso(new BigInteger("283"));
//				
				
				List<Curso> listaCursos = gerenciador.getCursos();

				try {

					JPAUtil.beginTransaction(this.getClass());

					for (Curso c : listaCursos) {
						System.out.println("Atualizando ambiente...");
						GerenciaCurso.defineAgentesPorCurso(c);
						GerenciaCurso.addTutorCurso(c);
						GerenciaCurso.addTutoresCurso(c);
						GerenciaCurso.addAlunosCurso(c);
						GerenciaCurso.addProfessorCurso(c);
						//GerenciaCurso.addLogsDoAluno(c);
						GerenciaCurso.addTags(c);
						GerenciaCurso.addAtividadeWiki(c);
						GerenciaCurso.addLicaoCurso(c);
						GerenciaCurso.addAtividadeQuestionario(c);
						GerenciaCurso.addAtividadeLicao(c);
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

						System.out.println("*** \nCurso: " + c.getId() + " -> "
								+ c.getFullName() + "\n");
						
					

					}

				} catch (Exception e) {
					JPAUtil.rollback();
					e.printStackTrace();
					System.out.println("\n************** ERRO ************\nMensagem: "+ e.getMessage());
					System.out.println("Causa: " + e.getCause());
				} finally {
					JPAUtil.closeEntityManager(this.getClass());
				}

				List<Mensagem> mensagens = environment.getMensagens();

				if (!mensagens.isEmpty()) {

					System.out.println("\n ADICIONANDO MENSAGENS \n");

					EntityManager managerDao = Dao.getEntityManager();
					managerDao.getTransaction().begin();

					synchronized (mensagens) {

						for (Mensagem msg : mensagens) {
							managerDao.persist(msg);
							
						}

					}

					managerDao.getTransaction().commit();

					mensagens.clear();
				}

				List<Grupo> grupos = environment.getGrupos();

				if (!grupos.isEmpty()) {

					System.out.println("\n ADICIONANDO GRUPOS \n");

					EntityManager managerDao = Dao.getEntityManager();
					managerDao.getTransaction().begin();

					synchronized (grupos) {

						for (Grupo g : grupos) {
							managerDao.persist(g);

							for (Aluno a : g.getMembros()) {

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

				System.out.println("******Agentes liberados*******");
				environment.getControladorActions().setMantemAtualizando(true);
				
				ControleActions.liberarActions();
				Thread.currentThread().sleep(10 * 1000);
				while (ControleActions.liberarGerenciaBeans()) {
					System.out.println("Vai dormir!");
					Thread.currentThread().sleep(10 * 1000);
					System.out.println("Acordou!");
				}
				environment.getControladorActions().setMantemAtualizando(false);
				
				System.out.println("***** Agentes bloqueados ********");
			}

		} catch (InterruptedException e) {
			System.out.println("Exception sleep thread");
			System.exit(1);
		} finally {
			

		}

	}

}
