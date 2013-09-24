package moodle.Agentes.actions;

public class ControleActions {
	
	
	
	//Action companheiro de aprendizagem 
	private static boolean criaChat = false;
	private static boolean informaAndamento = false;
	private static boolean mostraNovaDisciplinaAluno = false;
	private static boolean pesquisaData = false;
	
	
	//Agente companheiro de tutores
	private static boolean alunosParticipantes = false;
	private static boolean informaNovaDisciplinaTutor = false;
	private static boolean mantemForumAtivo = false;
	private static boolean manteTutorAtivo = false;
	private static boolean notificaCoordenadorDeTutores = false;
	private static boolean tutoresPArticipantes = false;
	
	//Agente ompanheiro de professores
	private static boolean informarNotasAtrasadas = false;
	
	//agente pedagogico 
	private static boolean informaAtividadeDisciplina = false;
	private static boolean informaPreRequisito = false;
	
	
	public static boolean isCriaChat() {
		return criaChat;
	}
	
	public static void setCriaChat(boolean criaChat) {
		ControleActions.criaChat = criaChat;
	}
	
	public static boolean isInformaAndamento() {
		return informaAndamento;
	}
	
	public static void setInformaAndamento(boolean informaAndamento) {
		ControleActions.informaAndamento = informaAndamento;
	}
	
	public static boolean isMostraNovaDisciplinaAluno() {
		return mostraNovaDisciplinaAluno;
	}
	
	public static void setMostraNovaDisciplinaAluno(boolean mostraNovaDisciplinaAluno) {
		ControleActions.mostraNovaDisciplinaAluno = mostraNovaDisciplinaAluno;
	}
	
	public static boolean isPesquisaData() {
		return pesquisaData;
	}
	
	public static void setPesquisaData(boolean pesquisaData) {
		ControleActions.pesquisaData = pesquisaData;
	}
	
	public static boolean isAlunosParticipantes() {
		return alunosParticipantes;
	}
	
	public static void setAlunosParticipantes(boolean alunosParticipantes) {
		ControleActions.alunosParticipantes = alunosParticipantes;
	}
	
	public static boolean isInformaNovaDisciplinaTutor() {
		return informaNovaDisciplinaTutor;
	}
	
	public static void setInformaNovaDisciplinaTutor(boolean informaNovaDisciplinaTutor) {
		ControleActions.informaNovaDisciplinaTutor = informaNovaDisciplinaTutor;
	}
	
	public static boolean isMantemForumAtivo() {
		return mantemForumAtivo;
	}
	
	public static void setMantemForumAtivo(boolean mantemForumAtivo) {
		ControleActions.mantemForumAtivo = mantemForumAtivo;
	}
	
	public static boolean isManteTutorAtivo() {
		return manteTutorAtivo;
	}
	
	public static void setManteTutorAtivo(boolean manteTutorAtivo) {
		ControleActions.manteTutorAtivo = manteTutorAtivo;
	}
	
	public static boolean isNotificaCoordenadorDeTutores() {
		return notificaCoordenadorDeTutores;
	}
	
	public static void setNotificaCoordenadorDeTutores(boolean notificaCoordenadorDeTutores) {
		ControleActions.notificaCoordenadorDeTutores = notificaCoordenadorDeTutores;
	}
	
	public static boolean isTutoresPArticipantes() {
		return tutoresPArticipantes;
	}
	
	public static void setTutoresPArticipantes(boolean tutoresPArticipantes) {
		ControleActions.tutoresPArticipantes = tutoresPArticipantes;
	}
	
	public static boolean isInformarNotasAtrasadas() {
		return informarNotasAtrasadas;
	}
	
	public static void setInformarNotasAtrasadas(boolean informarNotasAtrasadas) {
		ControleActions.informarNotasAtrasadas = informarNotasAtrasadas;
	}
	
	public static boolean isInformaAtividadeDisciplina() {
		return informaAtividadeDisciplina;
	}
	public static void setInformaAtividadeDisciplina(boolean informaAtividadeDisciplina) {
		ControleActions.informaAtividadeDisciplina = informaAtividadeDisciplina;
	}
	
	public static boolean isInformaPreRequisito() {
		return informaPreRequisito;
	}
	
	public static void setInformaPreRequisito(boolean informaPreRequisito) {
		ControleActions.informaPreRequisito = informaPreRequisito;
	}
	
	public static void liberarActions(){
		//Action companheiro de aprendizagem 
		criaChat = true;
		informaAndamento = true;
		mostraNovaDisciplinaAluno = true;
		pesquisaData = true;
		
		//Agente companheiro de tutores
		alunosParticipantes = true;
		informaNovaDisciplinaTutor = true;
		mantemForumAtivo = true;
		manteTutorAtivo = true;
		notificaCoordenadorDeTutores = true;
		tutoresPArticipantes = true;
		
		//Agente ompanheiro de professores
		informarNotasAtrasadas = true;
		
		//agente pedagogico 
		informaAtividadeDisciplina = true;
		informaPreRequisito = true;
		
	}
	
	public static boolean liberarGerenciaBeans(){
		if((criaChat = false) &&
		(informaAndamento = false) &&
		(mostraNovaDisciplinaAluno = false) &&
		(pesquisaData = false) && 
		(alunosParticipantes = true) && 
		(informaNovaDisciplinaTutor = true) &&
		(mantemForumAtivo = true) &&
		(manteTutorAtivo = true) &&
		(notificaCoordenadorDeTutores = true) &&
		(tutoresPArticipantes = true)  &&
		(informarNotasAtrasadas = true) &&
		(informaAtividadeDisciplina = true) &&
		(informaPreRequisito = true)){
			return true;
		}else{
			return false;
		}
	}
}
