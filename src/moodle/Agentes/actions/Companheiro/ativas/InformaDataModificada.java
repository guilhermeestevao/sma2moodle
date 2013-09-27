package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import dao.GerenciaCurso;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class InformaDataModificada extends ActionMoodle{
	
	private boolean done = false;
	private boolean mantemAtivo;
	Map<Curso, Set<Atividade>> MapaAtividadesDoAgente = new HashMap<Curso, Set<Atividade>>();
	
	public InformaDataModificada(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 21;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		if(!ControleActions.isInformaDataModificada())
			return;
		
		
		MoodleEnv envir = (MoodleEnv) env;

		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy - hh:mm:ss");

		GerenciaCurso manager = envir.getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		for(Curso c : manager.getCursos()){
			
			Set<Atividade> atividadesDoAmbiente = c.getAllAtividades();
			
			if(MapaAtividadesDoAgente.isEmpty()){
				iniciaMapaDoagente(manager);
			}
			
			Set<Atividade> ativadadesDoAgente = null;
			try{
				ativadadesDoAgente = MapaAtividadesDoAgente.get(c);
			}catch(NullPointerException e){
				ativadadesDoAgente = iniciaMapaDoagente(c);
			}
			
			//Percorrendo ativiades do ambiente para comparar se ouveram mudan�as de datas
			for(Atividade atividade : atividadesDoAmbiente){
				//Verifica se a atividade � puramente avaliativa
				if(atividade instanceof AtividadeNota){
					AtividadeNota atAmbiente = (AtividadeNota) atividade;
					//verifica se a atividade corrente est� no set do agente
					if(!ativadadesDoAgente.contains(atAmbiente)){
						//sen�o estiver ela � add no set
						ativadadesDoAgente.add(atAmbiente);
					}else{
						//se estiver ele ir� comparar as datas 
						for(Atividade atAgente : ativadadesDoAgente){
							//Procura a atividade 
							if(atAgente.equals(atAmbiente)){
								
								//Armazena as datas do agente e do ambiente para serem comparadas
								Date dataDoAgente = atAgente.getDataFinal();
								Date dataDoAmbiente = atAmbiente.getDataFinal();
								
								//Se o horario tiver mudado a set da agente ser� atualizado com o do ambiente
								if(!dataDoAgente.equals(dataDoAmbiente)){
									//JOptionPane.showMessageDialog(null, "A atividade "+atAgente.getName()+" mudou \n De: "+dateFormat.format(dataDoAgente)+" para "+dateFormat.format(dataDoAmbiente));
									enviarMensgem(env, c.getAlunos(), atAgente, atAmbiente, c.getId());
									ativadadesDoAgente.remove(dataDoAgente);
									ativadadesDoAgente.add(atAmbiente);
								}
							}
						}
					}
				}else{
					AtividadeParticipacao atAmbiente = (AtividadeParticipacao) atividade;
					if(atAmbiente.isAvaliativo()){
						if(!ativadadesDoAgente.contains(atAmbiente)){
							//sen�o estiver ela � add no set
							ativadadesDoAgente.add(atAmbiente);
						}else{
							//se estiver ele ir� comparar as datas 
							for(Atividade atAgente : ativadadesDoAgente){
								//Procura a atividade 
								if(atAgente.equals(atAmbiente)){
									
									//Armazena as datas do agente e do ambiente para serem comparadas
									Date dataDoAgente = atAgente.getDataFinal();
									Date dataDoAmbiente = atAmbiente.getDataFinal();
									
									//Se o horario tiver mudado a set da agente ser� atualizado com o do ambiente
									if(!dataDoAgente.equals(dataDoAmbiente)){
										//JOptionPane.showMessageDialog(null, "A atividade "+atAgente.getName()+" mudou \n De: "+dateFormat.format(dataDoAmbiente)+" para "+dateFormat.format(dataDoAmbiente));
										enviarMensgem(env, c.getAlunos(), atAgente, atAmbiente, c.getId());
										ativadadesDoAgente.remove(dataDoAgente);
										ativadadesDoAgente.add(atAmbiente);
									}
								}
							}
						}
					}
				}
				
				MapaAtividadesDoAgente.put(c, ativadadesDoAgente);
				
			} 
			
		}
		
		ControleActions.setInformaDataModificada(false);
	}
	
	public void iniciaMapaDoagente(GerenciaCurso manager){
		
		for(Curso c : manager.getCursos()){
			Set<Atividade> atividadesDoAmbiente = c.getAllAtividades();
			MapaAtividadesDoAgente.put(c, atividadesDoAmbiente);
		}
	}
	
	public Set<Atividade> iniciaMapaDoagente(Curso c){
		return c.getAllAtividades();
	}
	
	public void enviarMensgem(Environment env, Set<Aluno> alunos, Atividade at1, Atividade at2, BigInteger idCurso){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		for (Aluno aluno : alunos) {
			  String smallmessage = "Prezado " + aluno.getCompleteName() + ", \n";
			  smallmessage+="A atividade "+at1.getName() + " teve sua data final alterada de: " + formato.format(at1.getDataFinal())+" \n";
			  smallmessage+="para "+formato.format(at2.getDataFinal()); 
			  
			  
				CompanheiroAgente comp = (CompanheiroAgente)myAgent;
				if(verificaMens(idCurso, aluno.getId(), smallmessage))
					continue;
				else{
					Timestamp atual = new Timestamp(System.currentTimeMillis());
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), aluno.getId(), idCurso,atual,smallmessage);
				}
			  BigInteger useridfrom = new BigInteger("2");
			  BigInteger useridto = aluno.getId();
			  
			  String fullmessage = smallmessage;
				fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para voc� em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
				Long time = System.currentTimeMillis();						
				Mensagem msg = new Mensagem();
				msg.setSubject("Nova mensagem do Administrador");
				msg.setUseridfrom(useridfrom);
				msg.setUseridto(useridto);
				msg.setSmallmessage(smallmessage);
				msg.setFullmessage(fullmessage);
				msg.setTimecreated(time);
				((MoodleEnv)env).addMensagem(msg);
		}
	}
}