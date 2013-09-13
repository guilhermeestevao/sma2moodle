package moodle.Agentes;
import jamder.behavioural.*;
import jamder.Environment;
import jamder.roles.AgentRole;
import jamder.structural.*;
import java.util.List;
import java.util.Map;

//import moodle.Agentes.actions.BuscadorAgente.ExibirPessoasRelacionadasAc;
import jamder.agents.*;

public class BuscadorAgente extends MASMLAgent {
  
	private int idAgente;
	
	//Constructor 
   public BuscadorAgente (String name, Environment env, AgentRole agRole) {
   super(name, env, agRole);
   
   setIdAgente(3);
   
   addBelief("crencasBuscador.pl", new Belief("crencasBuscador.pl", "String", ""));
   //Action exibirPessoasRelacionadasAc = new ExibirPessoasRelacionadasAc("exibirPessoasRelacionadasAc", null, null);
   //addAction("exibirPessoasRelacionadasAc", exibirPessoasRelacionadasAc);
   
   
   /*
   
   	
     
     Goal relacionarPessoasG = new LeafGoal("relacionarPessoasG", "Boolean", "false");
     addGoal("relacionarPessoasG", relacionarPessoasG);
     Goal relacionarDocumentosG = new LeafGoal("relacionarDocumentosG", "Boolean", "false");
     addGoal("relacionarDocumentosG", relacionarDocumentosG);
   
     Action localizarPessoasAc = new Action("localizarPessoasAc", null, null);
     addAction("localizarPessoasAc", localizarPessoasAc);
    
     Action buscarDocumentosAc = new Action("buscarDocumentosAc", null, null);
     addAction("buscarDocumentosAc", buscarDocumentosAc);
     Action exibirDocumentosRelacionadosAc = new Action("exibirDocumentosRelacionadosAc", null, null);
     addAction("exibirDocumentosRelacionadosAc", exibirDocumentosRelacionadosAc);
   
     Plan buscarInformacoesPessoasPlan = new Plan("buscarInformacoesPessoasPlan", relacionarDocumentosG);
     addPlan("buscarInformacoesPessoasPlan", buscarInformacoesPessoasPlan); 
     Plan buscarInformacoesDocumentosPlan = new Plan("buscarInformacoesDocumentosPlan", relacionarPessoasG);
     addPlan("buscarInformacoesDocumentosPlan", buscarInformacoesDocumentosPlan); 
   
   */   
   }
   
	public void setup() {
		super.setup();
		
		Map<String, Action> actions = this.getAllActions();
		
		if(!actions.isEmpty()){
		
			for(Map.Entry<String, Action> results : actions.entrySet()){
				addBehaviour(results.getValue());
			}
		}
	}

	public int getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}
   
}
