package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;

import moodle.dados.Aluno;
import moodle.dados.Atividade;

public abstract class AtividadeParticipacao extends Atividade {
	
	
	@Transient
	private Set<Aluno> alunosParticipantes;
	
	
	public AtividadeParticipacao(){
		alunosParticipantes = new HashSet<Aluno>();
		notas = new HashMap<Aluno, BigDecimal>();
	}

	public Set<Aluno> getAlunosParticipantes() {
		return alunosParticipantes;
	}
	

	public void addAlunosParticipantes(Aluno aluno) {
		alunosParticipantes.add(aluno);
	}
	
	public boolean isAvaliativo(){
		return false;
	}
	
	
	
}
