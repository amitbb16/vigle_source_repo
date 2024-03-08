package com.retro.dev.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Long userid;

    @NotBlank
    @Size(max = 200)
    private String name;
    @NotBlank
	private Long serviceId;
    @NotBlank
    private Double actualprice;
    @NotBlank
    private Double discountprice;
    @NotBlank
    private LocalDateTime fromdate;
    @NotBlank
    private LocalDateTime todate;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private EStatus estatus;

    
    
    
    public Discount() {
		super();
	}

	public Discount( Long userid, String name,  Long serviceId,  Double actualprice,  Double discountprice,  LocalDateTime fromdate,  LocalDateTime todate,  EStatus estatus) {
        this.userid = userid;
        this.name = name;
        this.serviceId = serviceId;
        this.actualprice = actualprice;
        this.discountprice = discountprice;
        this.fromdate = fromdate;
        this.todate = todate;
        this.estatus = estatus;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Double getActualprice() {
		return actualprice;
	}

	public void setActualprice(Double actualprice) {
		this.actualprice = actualprice;
	}

	public Double getDiscountprice() {
		return discountprice;
	}

	public void setDiscountprice(Double discountprice) {
		this.discountprice = discountprice;
	}

	public LocalDateTime getFromdate() {
		return fromdate;
	}

	public void setFromdate(LocalDateTime fromdate) {
		this.fromdate = fromdate;
	}

	public LocalDateTime getTodate() {
		return todate;
	}

	public void setTodate(LocalDateTime todate) {
		this.todate = todate;
	}

	public EStatus getEstatus() {
		return estatus;
	}

	public void setEstatus(EStatus estatus) {
		this.estatus = estatus;
	}
    
    
    
}
