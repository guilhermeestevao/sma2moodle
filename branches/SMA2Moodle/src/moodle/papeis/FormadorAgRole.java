package moodle.papeis;

import jamder.structural.*;
import jade.core.behaviours.Behaviour;
import jamder.Organization;
import jamder.agents.GenericAgent;
import jamder.roles.*;
import jamder.behavioural.*;

public class FormadorAgRole extends ProactiveAgentRole {
   //Constructor 
   public FormadorAgRole (String name, Organization owner, GenericAgent player) {
   super(name, owner, player);
   addBelief("crencasFormaGrupos.pl", new Belief("crencasFormaGrupos.pl", "String", ""));
   
   addGoal("formarGrupoPorAfinidade", new LeafGoal("formarGrupoPorAfinidade", "String", ""));
   addGoal("formarGrupoPorPerfisAprendizagem", new LeafGoal("formarGrupoPorPerfisAprendizagem", "String", ""));
   
   addRight("exibirDicasDeFormacao", new Right());
   addRight("criarGrupoPorPerfil", new Right());
   addRight("criarGrupoAfinidade", new Right());
   addRight("integrarGrupoAfinidade", new Right());
   addRight("integrarGrupoPerfil", new Right());
   addRight("requisitaCoordenadorAcao", new Right());
   
   initialize(); 
   }
}
