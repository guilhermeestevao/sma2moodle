package moodle.Agentes.AgentesSimuladores.Dados;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "mdl_forum_discussions")
public class DiscussaoForum {

	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	private long firstpost;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getFirstPost(){
		return firstpost;
	}
	
	public void setFirstPost(long firstPost){
		this.firstpost = firstPost;
	}
	
	
	
}
