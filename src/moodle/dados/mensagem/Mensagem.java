package moodle.dados.mensagem;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "mdl_message")
public class Mensagem {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigInteger useridfrom;
	private BigInteger useridto;
	private String subject;
	private String fullmessage;
	private String smallmessage;
	private Long timecreated;
	
	public Mensagem(){
		
	}
	
	public Long getId() {
		return id;
	}
	
	public BigInteger getUseridfrom() {
		return useridfrom;
	}
	
	public void setUseridfrom(BigInteger useridfrom) {
		this.useridfrom = useridfrom;
	}
	
	public BigInteger getUseridto() {
		return useridto;
	}
	
	public void setUseridto(BigInteger useridto) {
		this.useridto = useridto;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}
	
	public String getSubject(){
		return subject;
	}
	
	public String getFullmessage() {
		return fullmessage;
	}
	public void setFullmessage(String fullmessage) {
		this.fullmessage = fullmessage;
	}
	public String getSmallmessage() {
		return smallmessage;
	}
	public void setSmallmessage(String smallmessage) {
		this.smallmessage = smallmessage;
	}
	public Long getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Long timecreated) {
		this.timecreated = timecreated;
	}
	
	
}
