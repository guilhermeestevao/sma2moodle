package moodle.papeis;

import jamder.structural.*;
import jade.core.behaviours.Behaviour;
import jamder.Organization;
import jamder.agents.GenericAgent;
import jamder.roles.*;
import jamder.behavioural.*;

public class CoordenadorAgRole extends ProactiveAgentRole {
   //Constructor 
   public CoordenadorAgRole (String name, Organization owner, GenericAgent player) {
   super(name, owner, player);
   addBelief("crencasCoordenador.pl", new Belief("crencasCoordenador.pl", "String", ""));
   
   addGoal("requisitarAcaoAgente", new LeafGoal("requisitarAcaoAgente", "String", ""));
   
   addRight("verificaAcaoDoAgente", new Right());
   addRight("requisitaAcao", new Right());
   
   initialize(); 
   }
}
