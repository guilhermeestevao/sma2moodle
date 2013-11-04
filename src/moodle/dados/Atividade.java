package moodle.dados;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Atividade implements Serializable {
	
	@Id
	@GeneratedValue
	private BigInteger id;
	private String name;
	
	@Transient
	protected final static int miliSec = 1000;
	
	@Transient
	protected Map<Aluno, BigDecimal> notas;
	
	public BigInteger getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void addAlunoComNota(Aluno aluno, BigDecimal nota){
			
		this.notas.put(aluno, nota);		
	}
	
	public Map<Aluno, BigDecimal> getAlunosComNotas() {
		return notas;
			
	}
	
	public BigDecimal getNotaAluno(Aluno aluno){
		return notas.get(aluno);
	}
	
	public Date getDataInicio(){
		return new Date(0);
	}
	
	public Date getDataFinal(){
		return new Date(0);
	}
	
	public BigDecimal getNotaMaxima(){
		return new BigDecimal(0);
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
		Atividade other = (Atividade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
