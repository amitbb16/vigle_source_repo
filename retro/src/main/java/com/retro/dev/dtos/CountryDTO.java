package com.retro.dev.dtos;

public class CountryDTO {
    
	private Long id;
    private String ctryCd;
    private String ctryName;
    private String ctryDialCd;
    private String gmtDiff;
    private boolean isCtryApplicable;
	private String ctryContinent;
	
    
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCtryCd() {
		return ctryCd;
	}
	public void setCtryCd(String ctryCd) {
		this.ctryCd = ctryCd;
	}
	public String getCtryName() {
		return ctryName;
	}
	public void setCtryName(String ctryName) {
		this.ctryName = ctryName;
	}
	public String getCtryDialCd() {
		return ctryDialCd;
	}
	public void setCtryDialCd(String ctryDialCd) {
		this.ctryDialCd = ctryDialCd;
	}
	public String getGmtDiff() {
		return gmtDiff;
	}
	public void setGmtDiff(String gmtDiff) {
		this.gmtDiff = gmtDiff;
	}
	public boolean isCtryApplicable() {
		return isCtryApplicable;
	}
	public void setCtryApplicable(boolean isCtryApplicable) {
		this.isCtryApplicable = isCtryApplicable;
	}
	public String getCtryContinent() {
		return ctryContinent;
	}
	public void setCtryContinent(String ctryContinent) {
		this.ctryContinent = ctryContinent;
	}

    
}
