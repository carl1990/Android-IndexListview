package com.cw.indexlistview.model;

import java.io.Serializable;

public class CityListCityModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String quanpin;
    private String jianpin;
    private String cityId;
    private String provinceId;
    private String provinceName;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getQuanpin()
    {
        return quanpin;
    }

    public void setQuanpin(String quanpin)
    {
        this.quanpin = quanpin;
    }

    public String getJianpin()
    {
        return jianpin;
    }

    public void setJianpin(String jianpin)
    {
        this.jianpin = jianpin;
    }

    public String getCityId()
    {
        return cityId;
    }

    public void setCityId(String cityId)
    {
        this.cityId = cityId;
    }

    public String getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(String provinceId)
    {
        this.provinceId = provinceId;
    }

    public String getProvinceName()
    {
        return provinceName;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }
}
