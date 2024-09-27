package com.lcwd.electronicstore.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.entities.OrderItem;
import com.lcwd.electronicstore.entities.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderDto {
	 private String orderId;
	    private String orderStatus="PENDING";
	    private String paymentStatus="NOTPAID";
	    private int orderAmount;
	    private String billingAddress;
	    private String billingPhone;
	    private String billingName;
	    private Date orderedDate=new Date();
	    private Date deliveredDate;
	    //private UserDto user;
	    private List<OrderItemDto> orderItems = new ArrayList<>();
}
