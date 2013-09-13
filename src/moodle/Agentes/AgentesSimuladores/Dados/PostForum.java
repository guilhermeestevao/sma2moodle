package moodle.Agentes.AgentesSimuladores.Dados;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="mdl_forum_posts")
public class PostForum {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private long discussion;
	private long userid;
	private String subject;
	private String message;
	private long parent;
	
	public Long getId() {
		return id;
	}
	
	public long getDiscussion() {
		return discussion;
	}
	public void setDiscussion(long discussion) {
		this.discussion = discussion;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}
	
	

}
