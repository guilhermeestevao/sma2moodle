package moodle.dados;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "mdl_user")

@NamedQuery(name="Tutor.findById", query="SELECT tutor FROM Tutor tutor WHERE id = ?1")

public class Tutor extends Pessoa {
	
	@Transient
	private int contAdveretencias;
	@Transient
	private Coordenador coordenador;

	public Coordenador getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Coordenador coordenador) {
		this.coordenador = coordenador;
	}

	public int getContAdveretencias() {
		return contAdveretencias;
	}

	public void setContAdveretencias(int contAdveretencias) {
		this.contAdveretencias = contAdveretencias;
	}
	
}
