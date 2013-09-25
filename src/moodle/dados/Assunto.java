package moodle.dados;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.*;


@Entity
@Table(name = "ag_assunto")
@NamedQuery(name = "AssuntoByMaterial", query = "SELECT assunto FROM Assunto assunto")
public class Assunto implements Serializable{
	@Id @GeneratedValue
	private Integer id;
	private String nome;
	@OneToMany(cascade={CascadeType.PERSIST})
	@JoinTable(name="ag_assunto_material", joinColumns=@JoinColumn(name="id_assunto"), 
	inverseJoinColumns=@JoinColumn(name="id_material"))
	private Collection<Material> materiais = new HashSet<Material>();


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Collection<Material> getMaterial() {
		return materiais;
	}
	public void addAMaterial(Collection<Material> mate) {
		materiais.addAll(mate);
	}
	public void addMaterial(Material mate){
		materiais.add(mate);
	}
	
}
