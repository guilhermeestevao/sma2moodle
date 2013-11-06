package moodle.dados.contexto;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdl_course_modules")
public class ModuloCurso {
	
	@Id
	@GeneratedValue
	private BigInteger id;
	private BigInteger course;
	private BigInteger module;
	private BigInteger instance;
	private String idnumber = null;
	private BigInteger section;
	private Long added;
	
	
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
	
	public BigInteger getModule() {
		return module;
	}
	
	public void setModule(BigInteger module) {
		this.module = module;
	}
	
	public BigInteger getInstance() {
		return instance;
	}
	
	public void setInstance(BigInteger instance) {
		this.instance = instance;
	}
	
	public BigInteger getSection() {
		return section;
	}
	
	public void setSection(BigInteger section) {
		this.section = section;
	}
	
	public Long getAdded() {
		return added;
	}
	
	public void setAdded(Long added) {
		this.added = added;
	}

}
