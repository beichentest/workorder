/**
 * Project Name:SpringOA
 * File Name:User.java
 * Package Name:com.zml.oa.entity
 * Date:2014-11-8下午11:12:48
 *
 */
package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @ClassName: Delegate
 * @Description:TODO(代理人类)
 * @author: lichen
 * @date: 2018-8-29 下午15:12:48
 *
 */

@Entity
@Table(name = "T_DELEGATE")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Delegate implements Serializable{	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1834313619082384908L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
	@Column(name = "ASSIGNEE")
	private Integer assignee;
	
	@Column(name = "ATTORNEY")	
	private Integer attorney;
	
	@Column(name = "ATTORNEY_NAME")
	private String attorneyName;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "START_TIME")
	private Date startTime;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "END_TIME")
	private Date endTime;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "MEMO")
	private String memo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssignee() {
		return assignee;
	}

	public void setAssignee(Integer assignee) {
		this.assignee = assignee;
	}

	public Integer getAttorney() {
		return attorney;
	}

	public void setAttorney(Integer attorney) {
		this.attorney = attorney;
	}

	public String getAttorneyName() {
		return attorneyName;
	}

	public void setAttorneyName(String attorneyName) {
		this.attorneyName = attorneyName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Delegate [id=" + id + ", assignee=" + assignee + ", attorney=" + attorney + ", attorneyName="
				+ attorneyName + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", memo="
				+ memo + "]";
	}
}
