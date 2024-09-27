package com.lcwd.electronicstore.entities;

import java.util.List;

import org.hibernate.annotations.Cascade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
@Entity
@Table(name = "Category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
	@Id
	@Column(name="categiryId")
 private String categiryId;
	@Column(name="title",nullable = false,length = 60)
 private String title;
	@Column(name="description",length = 50)
 private String description;
 private String coverImage;
 @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch =FetchType.LAZY)
 private List<Product> productList = new ArrayList<>();

}
