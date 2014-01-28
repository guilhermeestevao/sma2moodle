package moodle.dados.mensagem;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ag_formas_de_envio")
public class FormaEnvio {

	@Id@GeneratedValue
	private BigInteger id;
	private String f_envio;
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getF_envio() {
		return f_envio;
	}
	public void setF_envio(String f_envio) {
		this.f_envio = f_envio;
	}
	
}
