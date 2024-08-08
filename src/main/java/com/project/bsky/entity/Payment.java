package com.project.bsky.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="payment")
public class Payment implements Serializable {
	@Id
	private Integer id;
	private String name;
	private String email;
	private String mobile;
	private String amount;
}
