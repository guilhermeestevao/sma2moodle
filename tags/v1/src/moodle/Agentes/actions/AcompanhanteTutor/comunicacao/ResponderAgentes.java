package moodle.Agentes.actions.AcompanhanteTutor.comunicacao;

import java.util.List;
import java.util.Map;

import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class ResponderAgentes extends AchieveREResponder{
	public ResponderAgentes(Agent a, MessageTemplate msg){
		super(a,msg);
		
	}
	@Override
	protected ACLMessage prepareResponse(ACLMessage msg)
			throws NotUnderstoodException, RefuseException{
		
		
		
		if(msg.getSender().getLocalName().equals("CompanheiroAg")){
			
			
			
			if(msg.getConversationId().equals("AlunosNotasBaixas")){
				
				try{
					
					Map<Curso, List<Aluno>> alunosNotaBaixa = (Map<Curso, List<Aluno>>)msg.getContentObject();
					
					
					if(alunosNotaBaixa.isEmpty()){
						throw new RefuseException("Mapa vazio");
					}
					
					
					myAgent.addBehaviour(new OrientarAlunoNotaBaixa("orientarAlunoNotaBaixa", alunosNotaBaixa));
					
					
				}catch(UnreadableException e){
					e.printStackTrace();
				}
				
			}else if (msg.getConversationId().equals("AtividadesEncerrando")) {

				try {
					
					
					Map<Aluno, List<Atividade>> atividadesEncerrando = (Map<Aluno, List<Atividade>>) msg.getContentObject();
						
					myAgent.addBehaviour(new InformarAtividadesEncerrando("informarAtividadesEncerrandos",atividadesEncerrando));

					if (atividadesEncerrando.isEmpty()) {
						System.out.println("Mapa vazio para essa action");
						throw new RefuseException("Mapa vazio");
						
					}

				} catch (UnreadableException e) {

					e.printStackTrace();
				}

			}
			
		} 
		
		ACLMessage agree = msg.createReply();
		agree.setPerformative(ACLMessage.AGREE);
		return agree;

	}

	

	protected ACLMessage prepareResultNotification(ACLMessage request,
			ACLMessage response) throws FailureException {
		
		ACLMessage resposta = request.createReply();
		resposta.setPerformative(ACLMessage.INFORM);

		
		return resposta;
		
		
		
	}
}
