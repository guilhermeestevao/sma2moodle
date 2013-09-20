package moodle.Org;

import jamder.agents.GenericAgent;
import jamder.agents.ModelAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import moodle.Agentes.CompanheiroAgente;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;

public class ControladorActions extends ThreadsServicos {

	private Map<Curso, List<Aluno>> alunosNotaBaixa;
	private Map<Aluno, List<Atividade>> atividadesEncerrando;
	
	public ControladorActions(MoodleEnv env) {
		super(env);
		alunosNotaBaixa = new HashMap<Curso, List<Aluno>>();
	    atividadesEncerrando = new HashMap<Aluno, List<Atividade>>();
	}

	@Override
	public void run() {

			
			try {
		
				while(mantemAtualizando){
				
				Map<Curso, List<Aluno>> alunosNotaBaixaTemp = environment.getAlunosNotaBaixaProcessado();
				Map<Aluno, List<Atividade>> atividadesEncerrandoTemp = environment.getAtividadesEncerrandoProcessado();
				
				
				
				if(!alunosNotaBaixaTemp.entrySet().equals(alunosNotaBaixa.entrySet())){
					alunosNotaBaixaTemp.entrySet().removeAll(alunosNotaBaixa.entrySet());
					alunosNotaBaixa.putAll(alunosNotaBaixaTemp);
					
				
					GenericAgent ag = environment.getAgent("CompanheiroAg");
					
					if(ag != null){
						ag.addBehaviour("comunicarFormadorAgNotasBaixas");
						ag.addBehaviour("comunicarAcompanhanteTutorAgNotasBaixas");
						ag.addBehaviour("comunicarPedagogicoAgNotasBaixas");
					}
					
					
				}
				
				if(!atividadesEncerrandoTemp.entrySet().equals(atividadesEncerrando.entrySet())){
					atividadesEncerrandoTemp.entrySet().removeAll(atividadesEncerrando.entrySet());
					atividadesEncerrando.putAll(atividadesEncerrandoTemp);
				
					GenericAgent ag = environment.getAgent("CompanheiroAg");
					
					if(ag != null){
						
						System.out.println("***** ENVIANDO MSGM TUTOR *****************************");
						
						ag.addBehaviour("ComunicarAcompanhanteTutorAgAtividadesEncerrando");
					}
					
				
				}
				
				
				
				Thread.sleep(4000);
				
			
				}
		
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
				
	}

}
