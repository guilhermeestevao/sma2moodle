package moodle.Org;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jamder.Environment;
import jamder.Organization;
import jamder.roles.*;
import jamder.structural.*;
import jamder.behavioural.*;

public class MoodleOrg extends Organization {
   //Constructor 
   public MoodleOrg (String name, Environment env, ProactiveAgentRole agRole) {
     super(name, env, agRole);
     
   }
   
}

