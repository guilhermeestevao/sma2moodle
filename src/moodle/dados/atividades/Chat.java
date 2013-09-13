package moodle.dados.atividades;


import java.math.BigInteger;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "mdl_chat")
@NamedQuery(name="Chat.findByCourse", query="SELECT c FROM Chat c WHERE course = ?1")
public class Chat extends AtividadeParticipacao{

	
	private BigInteger course;
	private String intro;
	private short introformat = 1;
	private Long timemodified;
	private Long chattime; //tempo para finalizar a sessao do chat
	
	public Chat(){}
	
	public void setCourse(BigInteger course){
		this.course = course;
	}
	
	public void setIntro(String intro){
		this.intro = "<p>"+intro+"<p>";
	}
	
	
	public void setTimemodified(Long timemodified) {
		this.timemodified = timemodified;
	}

	public void setChattime(Long chattime) {
		this.chattime = chattime;
	}

	
	public Date getTimemodified(){
		return new Date(timemodified * miliSec);
	}
	
	public Date getChattime(){
		return new Date(chattime * miliSec);
	}

	@Override
	public Date getDataFinal() {
		return getChattime();
	}
		
}
