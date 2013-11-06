package moodle.dados;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "mdl_user")
@NamedQuery(name="Aluno.findById", query="SELECT aluno FROM Aluno aluno WHERE id = ?1")
public class Aluno extends Pessoa{
	
	@Transient
	private List<Log> logs = new ArrayList<Log>();

	@Transient
	private List<Tag> tags = new ArrayList<Tag>();
	
	public List<Tag> getTags(){
		return tags;
	}
	
	public void addTags(List<Tag> tags){
		this.tags.addAll(tags);
	}
	
	public List<Log> getLogs() {
		return logs;
	}

	public void addLogs(List<Log> logs) {
		this.logs.addAll(logs);
	}
	
	
}
