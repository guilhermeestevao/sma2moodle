package moodle.dados;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;


@Entity
@Table(name="mdl_url")
@NamedQuery(name="UrlByCurso", query="SELECT url FROM Url url WHERE course= ?1")
public class Url implements Serializable{

	@Id
	private BigInteger id;
	private BigInteger course;
	private String name;
	private String externalurl;
	private int display=0;
	private String displayoptions="a:2:{s:12:\"printheading\";i:0;s:10:\"printintro\";i:0;}";
	private String parameters="a:0:{}";
	private Long timemodified;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExternalurl() {
		return externalurl;
	}
	public void setExternalurl(String externalurl) {
		this.externalurl = externalurl;
	}
	
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	public String getDisplayoptions() {
		return displayoptions;
	}
	public void setDisplayoptions(String displayoptions) {
		this.displayoptions = displayoptions;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public Long getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Long timemodified) {
		this.timemodified = timemodified;
	}
	
	
	
}
