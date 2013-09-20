package moodle.Org;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.GerenciaCurso;
import jamder.Environment;
import jamder.Organization;
import jamder.roles.AgentRole;
import jamder.roles.ModelAgentRole;
import jamder.roles.ProactiveAgentRole;
import jamder.roles.ReflexAgentRole;
import jamder.agents.GenericAgent;
import jamder.behavioural.Action;
import moodle.Agentes.*;
import moodle.dados.Aluno;
import moodle.Agentes.AgentesSimuladores.Aluno.AlunoBom;
import moodle.Agentes.AgentesSimuladores.Aluno.AlunoMedio;
import moodle.Agentes.AgentesSimuladores.Aluno.AlunoRuim;
import moodle.Agentes.AgentesSimuladores.Tutor.TutorBom;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.controleag.ActionAgente;
import moodle.dados.grupos.Grupo;
import moodle.dados.mensagem.Mensagem;

public class MoodleEnv extends Environment {
	
	private GerenciaCurso gerenciadorCurso;
	private ExecutorService executorThread;
	private GerenciadorBeans gerenciadorBeans;
	private ControladorActions controladorActions;
	private boolean mantemAgentesAtivos;
	private List<Mensagem> mensagens;
	private List<ActionAgente> controleAg;
	private List<Grupo> grupos;
	private Map<Curso, List<Aluno>> alunosNotaBaixa;
	private Map<Aluno, List<Atividade>> atividadesEncerrando;
	
	public MoodleEnv (String name, String host, String port) {
	    super(name, host, port);
	    
	    mantemAgentesAtivos = false;
	    
	    Organization MoodleOrg = new Organization("MoodleOrg", this, null);
		addOrganization("MoodleOrg", MoodleOrg);
		
		GenericAgent CompanheiroAg = new CompanheiroAgente("CompanheiroAg", this, null);
	    AgentRole CompanheiroAgRole = new ModelAgentRole("CompanheiroAgRole", MoodleOrg, CompanheiroAg); 
	    //addAgent("CompanheiroAg", CompanheiroAg); 
		
		GenericAgent FormadorAg = new FormadorAgente("FormadorAg", this, null);
	    AgentRole FormadorAgRole = new ProactiveAgentRole("FormadorAgRole", MoodleOrg, FormadorAg); 
	    //addAgent("FormadorAg", FormadorAg);
		
	    GenericAgent AcompanhanteTutorAg = new AcompanhanteTutorAgente("AcompanhanteTutorAg", this, null);
	    AgentRole AcompanhanteTutorAgRole = new ModelAgentRole("AcompanhanteTutorAgRole", MoodleOrg, AcompanhanteTutorAg); 
	    //addAgent("AcompanhanteTutorAg", AcompanhanteTutorAg);
	    
		GenericAgent PedagogicoAg = new PedagogicoAgente("PedagogicoAg", this, null);
	    AgentRole PedagogicoAgRole = new ProactiveAgentRole("PedagogicoAgRole", MoodleOrg, PedagogicoAg); 
	    //addAgent("PedagogicoAg", PedagogicoAg);   
	    
		GenericAgent AjudanteAg = new AjudanteAgente("AjudanteAg", this, null);
	    AgentRole AjudanteAgRole = new AgentRole("AjudanteAgRole", MoodleOrg, AjudanteAg); 
	    //addAgent("AjudanteAg", AjudanteAg);   
	    
	    GenericAgent AcompanhanteDeProfessoresAg = new AcompanhanteDeProfessores("AcompanhanteDeProfessoresAg", this, null);
	    AgentRole AcompanhanteDeProfessoresAgAgRole = new ProactiveAgentRole("AcompanhanteDeProfessoresAgRole", MoodleOrg, AjudanteAg); 
	    //addAgent("AcompanhanteDeProfessoresAg", AjudanteAg);
	    
		GenericAgent alunoBom = new AlunoBom("alunoBom", this, null);
		AgentRole alunoBomRole = new ReflexAgentRole("alunoBomRole", MoodleOrg, alunoBom);
		//addAgent("alunoBom", alunoBom);
		
		GenericAgent alunoMedio = new AlunoMedio("alunoMedio", this, null);
		AgentRole alunoMedioRole = new ReflexAgentRole("alunoMedioRole", MoodleOrg, alunoMedio);
		//addAgent("alunoMedio", alunoMedio);
		
		GenericAgent alunoRuim = new AlunoRuim("alunoRuim", this, null);
		AgentRole alunoRuimRole = new ReflexAgentRole("alunoRuimRole", MoodleOrg, alunoRuim);
		//addAgent("alunoRuim", alunoRuim);
		
		GenericAgent tutorBom = new TutorBom("tutorBom", this, null);
		AgentRole tutorBomRole = new ReflexAgentRole("tutorBomRole", MoodleOrg, tutorBom);
		//addAgent("tutorBom", tutorBom);
		
		
	    /* REGIAO RELACIONADA AS BEANS */
	    
	    gerenciadorCurso = new GerenciaCurso();
	 
	    mensagens = Collections.synchronizedList(new ArrayList<Mensagem>());
	    controleAg = Collections.synchronizedList(new ArrayList<ActionAgente>());
	    grupos = Collections.synchronizedList(new ArrayList<Grupo>());
	    alunosNotaBaixa = Collections.synchronizedMap(new HashMap<Curso, List<Aluno>>());
	    atividadesEncerrando = Collections.synchronizedMap(new HashMap<Aluno, List<Atividade>>());
	    
	    gerenciadorBeans = new GerenciadorBeans(gerenciadorCurso, this);
	    controladorActions = new ControladorActions(this);
	    executorThread = Executors.newFixedThreadPool(2);
	    executorThread.execute(gerenciadorBeans);
	    executorThread.execute(controladorActions);
	
	    
	    
	}
	
	public GerenciadorBeans getGerenciadorBeans(){
		return gerenciadorBeans;
	}
	
	public GerenciaCurso getGerenciaCurso(){
		return gerenciadorCurso;
	}
	
	public ControladorActions getControladorActions(){
		return controladorActions;
	}
	
	public void setMantemAgentesAtivos(boolean mantem){
		mantemAgentesAtivos = mantem;
	}
	
	public boolean getMantemAgentesAtivos(){
		return mantemAgentesAtivos;
	}
	
	public void addMensagem(Mensagem msg){
		getMensagens().add(msg);
	}
	
	public List<Mensagem> getMensagens(){
		return mensagens;
	}
	
	public List<Grupo> getGrupos(){
		return grupos;
	}
	
	public void addAlunosNotaBaixa(Curso c, List<Aluno> als){
		getAlunosNotaBaixa().put(c, als);
	}
	
	public Map<Curso, List<Aluno>> getAlunosNotaBaixaProcessado(){
		
		if(!getAlunosNotaBaixa().isEmpty())
			getAlunosNotaBaixa().clear();
		
		setAlunosNotaBaixa();
		return getAlunosNotaBaixa();
	}
	
	public Map<Curso, List<Aluno>> getAlunosNotaBaixa(){
		
		return alunosNotaBaixa;
	}
	
	
	
	private void setAlunosNotaBaixa(){
		
		for(Curso curso : getGerenciaCurso().getCursos()){
			
			if(!alunosNotaBaixa.containsKey(curso)){
				alunosNotaBaixa.put(curso, new ArrayList<Aluno>());
			}
			
				
			Map<Aluno, BigDecimal> alunos = curso.getNotaGeralAlunos();
			
			for(Map.Entry<Aluno, BigDecimal> alunosNota : alunos.entrySet()){
				
				BigDecimal nota = alunosNota.getValue();
				
				if(nota != null){
					
					if(nota.intValue() < 70){
						
						
						List<Aluno> als = alunosNotaBaixa.get(curso);
						als.add(alunosNota.getKey());
						
						alunosNotaBaixa.put(curso, als);
						
					}
						
				}
				
			}
			
		}
		
	}
	
	
	public Map<Aluno, List<Atividade>> getAtividadesEncerrandoProcessado(){
		
		if(!getAtividadesEncerrando().isEmpty())
			getAtividadesEncerrando().clear();
		
		setAtividadesEncerrando();
		return getAtividadesEncerrando();
	}
	
	public Map<Aluno, List<Atividade>> getAtividadesEncerrando(){
		
		return atividadesEncerrando;
	}
	
	public void addAtividadeEncerrando(Aluno al, List<Atividade> ats){
		atividadesEncerrando.put(al, ats);
	}
	
	private void setAtividadesEncerrando(){
		
		GerenciaCurso manager = getGerenciaCurso();
		
		for(Curso curso : manager.getCursos()){
			
			for(Aluno aluno : curso.getAlunos()){
				
				for(AtividadeNota at : curso.getAtividadesNota()){
					
					
					if(!verificarData(at.getDataFinal()))
						continue;
					
					if(at.getAlunosComNotas().containsKey(aluno))
						continue;
					
					if(atividadesEncerrando.containsKey(aluno)){
						atividadesEncerrando.get(aluno).add(at);
					}else{
						List<Atividade> ats = new ArrayList<Atividade>();
						ats.add(at);
						addAtividadeEncerrando(aluno, ats );
					}
				
				}
				
				for(AtividadeParticipacao at : curso.getAtividadesParticipacao()){
					
					if(!verificarData(at.getDataFinal()))
						continue;
					
					if(at.getAlunosParticipantes().contains(aluno) || at.getAlunosComNotas().containsKey(aluno))
						continue;
									
					if(atividadesEncerrando.containsKey(aluno)){
						atividadesEncerrando.get(aluno).add(at);
					}else{
						List<Atividade> ats = new ArrayList<Atividade>();
						ats.add(at);
						addAtividadeEncerrando(aluno, ats );
					}
					
				}
			}
			
		}
		
	}
			
	private boolean verificarData(Date dataAtividade){
				
				 
		Date dataAtual = new Date();
				
				
		if(dataAtividade.after(dataAtual)){
					
					
			Calendar calDataAtual = Calendar.getInstance();
			calDataAtual.setTime(dataAtual);
			
			Calendar calDataAtividade = Calendar.getInstance();
			calDataAtividade.setTime(dataAtividade);
					
			if(calDataAtividade.get(Calendar.MONTH) == calDataAtual.get(Calendar.MONTH)){
						
						
				if(calDataAtividade.get(Calendar.DAY_OF_MONTH) - calDataAtual.get(Calendar.DAY_OF_MONTH) <= 7 )
						return true;
				
				}
					
					
					
		}
				
		return false;
				
				
	}
	
	public static boolean verificarData(Date dataAtividade, int dias){
		
		 
		Date dataAtual = new Date();
				
				
		if(dataAtividade.after(dataAtual)){
					
					
			Calendar calDataAtual = Calendar.getInstance();
			calDataAtual.setTime(dataAtual);
			
			Calendar calDataAtividade = Calendar.getInstance();
			calDataAtividade.setTime(dataAtividade);
					
			if(calDataAtividade.get(Calendar.MONTH) == calDataAtual.get(Calendar.MONTH)){
						
						
				if(calDataAtividade.get(Calendar.DAY_OF_MONTH) - calDataAtual.get(Calendar.DAY_OF_MONTH) <= dias )
						return true;
				
				}
					
					
					
		}
				
		return false;
				
				
	}
	
	
	public List<ActionAgente> getControleAg() {
		return controleAg;
	}

	public void setControleAg(List<ActionAgente> controleAg) {
		this.controleAg = controleAg;
	}
  
	public void addControleAg(ActionAgente actAg){
		getControleAg().add(actAg);
	}
  
	public static void main(String args[]) {
		final MoodleEnv env = new MoodleEnv("MoodleEnv", "127.0.0.1", "8888");
		System.out.println("Executou");
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			
			public void run(){
				
				env.getGerenciadorBeans().setMantemAtualizando(false);
				env.getControladorActions().setMantemAtualizando(false);
			}
		});
		
		
	}


}
