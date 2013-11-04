package moodle.papeis;

import jamder.structural.*;
import jade.core.behaviours.Behaviour;
import jamder.Organization;
import jamder.agents.GenericAgent;
import jamder.roles.*;
import jamder.behavioural.*;

public class PedagogicoAgRole extends ProactiveAgentRole {
   //Constructor 
   public PedagogicoAgRole (String name, Organization owner, GenericAgent player) {
   super(name, owner, player);
   
   addBelief("crencasPedagogico.pl", new Belief("crencasPedagogico.pl", "String", ""));
   
   addGoal("acompanharInterdisciplinaridadeAluno", new LeafGoal("acompanharInterdisciplinaridadeAluno", "String", ""));
   
   addRight("sugerirDisciplinasRelacionadas", new Right());
   addRight("sugerirCursosRelacionados", new Right());
   addRight("informarAlunoDescricaoCursos", new Right());
   addRight("informarAlunoDescricaoDisciplinas", new Right());
   
   addDuty("relacionarDisciplinas", new Duty());
   addDuty("relacionarCursos", new Duty());
    
   initialize(); 
   }
}
