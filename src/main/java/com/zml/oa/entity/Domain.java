package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
/**
 * 
* @ClassName: Domain  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author Administrator  
* @date 2018年7月17日  
*
 */
@Entity
@Table(name = "T_DOMAIN")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Domain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9079503173690486191L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	/**
	 * 域名名称
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 域名
	 */
	@Column(name="DOMAIN")
	private String domain;
	/**
	 * 项目归属
	 */
	@Column(name="SUBJECTION")
	private String subjection;
	/**
	 * 所属地区
	 */
	@Column(name="AREA")
	private String area;
	/**
	 * 负责人
	 */
	@Column(name="MANAGER")
	private String manager;
	/**
	 * 机房
	 */
	@Column(name="MOTOR_ROOM")
	private String motorRoom;
	
	/**
	 * 备注
	 */
	@Column(name="memo")
	private String memo;
	/**
	 * 状态（0 正常,1 下线）
	 */
	@Column(name="STATUS")
	private String status;
	/**
	 * 上线时间
	 */
	@Column(name="RELEASE_DATE")
	private Date releaseDate;
	/**
	 * 下线时间
	 */
	@Column(name="DISCARD_DATE")
	private Date discardDate;
	
	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="t_domain_project",joinColumns={@JoinColumn(name="domain_id")},inverseJoinColumns={@JoinColumn(name="project_id")})
	private Set<Project> projects = new HashSet<Project>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubjection() {
		return subjection;
	}
	public void setSubjection(String subjection) {
		this.subjection = subjection;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Date getDiscardDate() {
		return discardDate;
	}
	public void setDiscardDate(Date discardDate) {
		this.discardDate = discardDate;
	}
	public Set<Project> getProjects() {
		return projects;
	}
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	public String getMotorRoom() {
		return motorRoom;
	}
	public void setMotorRoom(String motorRoom) {
		this.motorRoom = motorRoom;
	}
}
