package moodle.dados;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
@Entity
@Table(name="mdl_tag")
@NamedQuery(name = "Tag.findById", query = "SELECT tag FROM Tag tag WHERE id = ?1")
public class Tag implements Serializable{
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	
}
