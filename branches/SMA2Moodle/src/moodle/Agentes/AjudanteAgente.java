package moodle.Agentes;

import jamder.Environment;
import jamder.roles.AgentRole;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import moodle.Agentes.actions.AjudanteAgente.ativas.ExibirDicasCalendario;
import moodle.Agentes.actions.AjudanteAgente.ativas.ExibirDicasConfiguracoes;
import moodle.Agentes.actions.AjudanteAgente.ativas.ExibirDicasForuns;
import moodle.Agentes.actions.AjudanteAgente.ativas.ExibirDicasParticipantes;
import jamder.agents.*;
import jamder.behavioural.Action;

public class AjudanteAgente extends ReflexAgent {

	private int idAgente;
	
   //Constructor 
   public AjudanteAgente (String name, Environment env, AgentRole agRole) {
   super(name, env, agRole);
   
   setIdAgente(2);
   
   Action exibirDicasForuns = new ExibirDicasForuns("exibirDicasForuns", null, null, new BigInteger(""+idAgente));
   addAction("exibirDicasForuns", exibirDicasForuns);
   Action exibirDicasCalendario = new ExibirDicasCalendario("exibirDicasCalendario", null, null, new BigInteger(""+idAgente));
   addAction("exibirDicasCalendario", exibirDicasCalendario);
   Action exibirDicasConfiguracoes = new ExibirDicasConfiguracoes("exibirDicasConfiguracoes", null, null, new BigInteger(""+idAgente));
   addAction("exibirDicasConfiguracoes", exibirDicasConfiguracoes);
   Action exibirDicasParticipantes = new ExibirDicasParticipantes("exibirDicasParticipantes", null, null,  new BigInteger(""+idAgente));
   addAction("exibirDicasParticipantes", exibirDicasParticipantes);
   
 
   }
   
   
   
	public void setup() {
	
		
		
		Map<String, Action> actions = this.getAllActions();
		
		if(!actions.isEmpty()){
		
			for(Map.Entry<String, Action> results : actions.entrySet()){
				addBehaviour(results.getValue());
			}
		}
		
		
		
		
	}
	
	public static int difDias(Date data) {
		long dataEntrada = data.getTime();
		long dataAtual = new Date().getTime();
		long dif = dataEntrada-dataAtual;
		int dias = (int) (dif/86400000);
		return dias;
		
	}
	
	@Override
	public void percept(String perception) {
		Action action = getPerceive(perception);
		if(action != null){
			addBehaviour(action);
		}
		
	}



	public int getIdAgente() {
		return idAgente;
	}



	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}
	
}
