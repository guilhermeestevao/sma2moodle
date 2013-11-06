package moodle.Agentes.actions.Formador.comunicacao;

import java.util.List;
import java.util.Map;

import moodle.dados.Aluno;
import moodle.dados.Curso;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class ResponderAgentes extends AchieveREResponder {

	public ResponderAgentes(Agent a, MessageTemplate mt) {
		super(a, mt);
		
	}
	
	@Override
	protected ACLMessage prepareResponse(ACLMessage request)
			throws NotUnderstoodException, RefuseException {
		
		
		
		
		if(request.getSender().getLocalName().equals("CompanheiroAg")){
		
			
			
			if(request.getConversationId().equals("AlunosNotaBaixa")){
				
				try {
					Map<Curso, List<Aluno>> alunosNotaBaixa = (Map<Curso, List<Aluno>>) request.getContentObject();
				
					if(alunosNotaBaixa.isEmpty())
						throw new RefuseException("Map vazio");
					
					myAgent.addBehaviour(new OrientarAlunoNotaBaixa("orientarAlunoNotaBaixa", alunosNotaBaixa));
					
					
					
					
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		
		
		
		ACLMessage agree = request.createReply();
		agree.setPerformative(ACLMessage.AGREE);
		
		return agree;
		
		
		
		//PARA RETORNAR REFUSE
		// throw new RefuseException();
		
		//PARA RETORNAR NAO-ENTENDEU
		//throw new NotUnderstood();
		
		
	}
	
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,
			ACLMessage response) throws FailureException {
		
		ACLMessage resposta = request.createReply();
		resposta.setPerformative(ACLMessage.INFORM);

		
		return resposta;
		
		
		
	}

}
