package moodle.papeis;
import jamder.structural.*;
import jade.core.behaviours.Behaviour;
import jamder.Organization;
import jamder.agents.GenericAgent;
import jamder.roles.*;
import jamder.behavioural.*;

public class CompanheiroAgRole extends ModelAgentRole {
   //Constructor 
   public CompanheiroAgRole (String name, Organization owner, GenericAgent player) {
   super(name, owner, player);
   addBelief("crencasAprendizagem.pl", new Belief("crencasAprendizagem.pl", "String", ""));
   addRight("exibirMensagemApoio", new Right());
   addRight("exibirMensagemReforco", new Right());
   addDuty("compararTurma", new Duty());
   initialize(); 
   }
}

