package com.lcwd.electronicstore.dto;

import org.aspectj.internal.lang.annotation.ajcPrivileged;

import com.lcwd.electronicstore.entities.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
 private String categiryId;
 @NotBlank
 @Size(min = 3, message=  "Title Must be Minimum four charector")
 private String title;
 
 @NotBlank(message = "Description must not be blank")
 private String description;
 private String coverImage;
}
