package moodle.dados;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.atividades.Licao;
import moodle.dados.grupos.Grupo;

@Entity
@Table(name = "mdl_course")
@NamedQuery(name="Curso.findById", query="SELECT curso FROM Curso curso WHERE id =?1")
public class Curso implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2933651153564657169L;
	@Id
	private BigInteger id;	
	private BigInteger category;
	private String fullname;
	private Long timecreated;
	private String sectioncache;
	
	@Transient
	private Professor professor ;
	@Transient
	private List<Tutor> tutores;
	@Transient
	private Set<Aluno> alunos;
	@Transient
	private Set<AtividadeNota> atividadesNota;
	@Transient
	private Set<AtividadeParticipacao> atividadesParticipacao;
	@Transient
	private BigInteger itemid;
	@Transient 
	private Map<Aluno, BigDecimal> notaGeralAlunos;
	@Transient
	private Collection<Curso> cursosPreRequisito; 
	@Transient
	private Collection<Grupo> grupos;	
	@Transient
	private Collection<Licao> licoes;
	@Transient
	private Collection<Url> url;
	@Transient
	private Collection<Folder> folder;
	
	public Curso(){
		alunos = new HashSet<Aluno>();
		tutores = new ArrayList<Tutor>();
		atividadesNota = new HashSet<AtividadeNota>();
		atividadesParticipacao = new HashSet<AtividadeParticipacao>();
		grupos = new HashSet<Grupo>();
		notaGeralAlunos = new HashMap<Aluno, BigDecimal>();
		cursosPreRequisito = new HashSet<Curso>();
		licoes = new HashSet<Licao>();
		url = new HashSet<Url>();
		folder = new HashSet<Folder>();
	}
	
	public void setSectioncache(String sectioncache) {
		this.sectioncache = sectioncache;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void addProfessor(Professor professor) {
		this.professor = professor;
	}

	
	public List<Tutor> getTutores(){
		return this.tutores;
	}
	
	public void addTutor(Tutor tutor){
		tutores.add(tutor);
	}
	
	public BigInteger getId() {
		return id;
	}
	
	public String getFullName() {
		return fullname;
	}
	
	public Set<Aluno> getAlunos(){
		return alunos;
	}
	
	public void addAluno(Aluno aluno){
		alunos.add(aluno);
	}
	
	public void addAluno(List<Aluno> alunos){
		alunos.addAll(alunos);
	}
	
	public void setItemid(BigInteger itemid){
		this.itemid = itemid;
	}
	
	public BigInteger getItemid(){
		return itemid;
	}
	
	public void addAtividadeNota(AtividadeNota atividade){
		atividadesNota.add(atividade);
	}
	
	public void addAtividadeNota(List<? extends AtividadeNota> atividades){
		atividadesNota.addAll(atividades);
	}
	
	
	public void addAtividadeParticipacao(AtividadeParticipacao atividade){
		atividadesParticipacao.add(atividade);
	}
	
	public void addAtividadeParticipacao(List<? extends AtividadeParticipacao> atividades){
		atividadesParticipacao.addAll(atividades);
	}

	public Set<AtividadeNota> getAtividadesNota() {
		return atividadesNota;
	}

	public Set<AtividadeParticipacao> getAtividadesParticipacao() {
		return atividadesParticipacao;
	}
	
	public Set<Atividade> getAllAtividades(){
		Set<Atividade> all =  new HashSet<Atividade>();
		all.addAll(atividadesParticipacao);
		all.addAll(atividadesNota);
		return all;
	}
	
	public Collection<Grupo> getGrupos(){
		return grupos;
	}
	
	public void addGrupo(Grupo grupo){
		grupos.add(grupo);
	}
	
	public void addGrupo(Collection<Grupo> grupos){
		this.grupos.addAll(grupos);
	}
	
	public Map<Aluno, BigDecimal> getNotaGeralAlunos(){
		return notaGeralAlunos;
	}
	
	public Collection<Curso> getCursosPreRequisito(){
		return cursosPreRequisito;
	}

	public void addLicao(Licao licao){
		licoes.add(licao);
	}
	public void addLicao(Collection<Licao> licao){
		licoes.addAll(licao);
	}
	
	public Collection<Licao> getLicaoCurso(){
		return licoes;
	}
	
	public void addUrl(Url url){
		this.url.add(url);
	}
	
	public void addUrl(Collection<Url> urls){
		this.url.addAll(urls);
	}
	
	public Collection<Url> getUrlCurso(){
		return this.url;
	}
	
	public void addFolder(Folder fold){
		this.folder.add(fold);
	}
	public void addFolder(Collection<Folder> folders){
		folder.addAll(folders);
	}
	public Collection<Folder> getFolderCurso(){
		return folder;
	}
	
	public void setCategory(BigInteger category){
		this.category = category;
	}
	
	public BigInteger getCategory(){
		return category;
	}
	
	public Date getDataCriacao(){
		long milisec = 1000;
		return new Date(timecreated*milisec); 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}