package moodle.dados.mensagem;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ag_mensagens")
@NamedQuery(name="byMensagemCustomizada", query="SELECT mensagemCustomizada FROM MensagemCustomizada mensagemCustomizada WHERE agente = ?1 AND action = ?2")
public class MensagemCustomizada {

	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String mensagem;
	private String destinatario;
	private BigInteger agente;
	private BigInteger action;

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	public BigInteger getAgente() {
		return agente;
	}
	public void setAgente(BigInteger agente) {
		this.agente = agente;
	}
	public BigInteger getAction() {
		return action;
	}
	public void setAction(BigInteger action) {
		this.action = action;
	}
	
}
