package moodle.dados;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;



@Entity
@Table(name = "mdl_files")
@NamedQuery(name = "FilesByFolder", query = "SELECT files FROM Files files WHERE contextid = ?1")
public class Files implements Serializable {

	@Id@GeneratedValue
	private Long id;
	private String contenthash;
	private String pathnamehash;
	private BigInteger contextid;
	private String component;
	private String filearea;
	private BigInteger itemid;
	private String filepath;
	private String filename;
	private BigInteger userid;
	private BigInteger filesize;
	private String mimetype;
	private BigInteger status;
	private String source;
	private String author;
	private String license;
	private Long timecreated;
	private Long timemodified;
	private BigInteger sortorder= new BigInteger("0");
	private BigInteger referencefileid;
	private BigInteger referencelastsync;
	private BigInteger referencelifetime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContenthash() {
		return contenthash;
	}
	public void setContenthash(String contenthash) {
		this.contenthash = contenthash;
	}
	public String getPathnamehash() {
		return pathnamehash;
	}
	public void setPathnamehash(String pathnamehash) {
		this.pathnamehash = pathnamehash;
	}
	public BigInteger getContextid() {
		return contextid;
	}
	public void setContextid(BigInteger contextid) {
		this.contextid = contextid;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getFilearea() {
		return filearea;
	}
	public void setFilearea(String filearea) {
		this.filearea = filearea;
	}
	public BigInteger getItemid() {
		return itemid;
	}
	public void setItemid(BigInteger itemid) {
		this.itemid = itemid;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public BigInteger getUserid() {
		return userid;
	}
	public void setUserid(BigInteger userid) {
		this.userid = userid;
	}
	public BigInteger getFilesize() {
		return filesize;
	}
	public void setFilesize(BigInteger filesize) {
		this.filesize = filesize;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public BigInteger getStatus() {
		return status;
	}
	public void setStatus(BigInteger status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public Long getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Long timecreated) {
		this.timecreated = timecreated;
	}
	public Long getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Long timemodified) {
		this.timemodified = timemodified;
	}
	public BigInteger getSortorder() {
		return sortorder;
	}
	public void setSortorder(BigInteger sortorder) {
		this.sortorder = sortorder;
	}
	public BigInteger getReferencefileid() {
		return referencefileid;
	}
	public void setReferencefileid(BigInteger referencefileid) {
		this.referencefileid = referencefileid;
	}
	public BigInteger getReferencelastsync() {
		return referencelastsync;
	}
	public void setReferencelastsync(BigInteger referencelastsync) {
		this.referencelastsync = referencelastsync;
	}
	public BigInteger getReferencelifetime() {
		return referencelifetime;
	}
	public void setReferencelifetime(BigInteger referencelifetime) {
		this.referencelifetime = referencelifetime;
	}
	

	
	
}
