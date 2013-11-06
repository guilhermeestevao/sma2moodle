package moodle.dados;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.*;

import moodle.dados.Files;
import moodle.dados.atividades.Licao;
import moodle.dados.grupos.Grupo;

@Entity
@Table(name="mdl_Folder")
@NamedQuery(name="FolderByCurso", query="SELECT folder FROM Folder folder WHERE course= ?1")
public class Folder implements Serializable{
	@Id@GeneratedValue
	private BigInteger id;
	private BigInteger course;
	private String name;
	private String intro;
	private int introformat;
	private BigInteger revison;
	private Long timemodified;
	@Transient
	private Collection<Files> files;
	
	public Folder(){
		files = new HashSet<Files>();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getIntroformat() {
		return introformat;
	}
	public void setIntroformat(int introformat) {
		this.introformat = introformat;
	}
	public BigInteger getRevison() {
		return revison;
	}
	public void setRevison(BigInteger revison) {
		this.revison = revison;
	}
	public Long getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Long timemodified) {
		this.timemodified = timemodified;
	}

	public Collection<Files> getFiles(){
		return files;
	}
	
	public void addFiles(Files files){
		this.files.add(files);
	}
	
	public void addFiles(Collection<Files> files){
		this.files.addAll(files);
	}
	
}
