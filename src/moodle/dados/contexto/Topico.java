package moodle.dados.contexto;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mdl_course_sections")
public class Topico {

	@Id @GeneratedValue
	private BigInteger id;
	private BigInteger course;
	private BigInteger section;
	private String name= "NULL";
	private String sequence;
	
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public BigInteger getId() {
		return id;
	}
	
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public BigInteger getCourse() {
		return course;
	}
	
	public void setCourse(BigInteger course) {
		this.course = course;
	}
	
	public BigInteger getSection() {
		return section;
	}
	
	public void setSection(BigInteger section) {
		this.section = section;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	
}
