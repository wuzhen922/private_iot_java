package com.iot.dao.model;

import java.util.Date;

public class User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.username
     *
     * @mbggenerated
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.password
     *
     * @mbggenerated
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.userGroup
     *
     * @mbggenerated
     */
    private String usergroup;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.extraInfo
     *
     * @mbggenerated
     */
    private String extrainfo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.regTime
     *
     * @mbggenerated
     */
    private Date regtime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.username
     *
     * @return the value of user.username
     *
     * @mbggenerated
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.username
     *
     * @param username the value for user.username
     *
     * @mbggenerated
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.password
     *
     * @return the value of user.password
     *
     * @mbggenerated
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.password
     *
     * @param password the value for user.password
     *
     * @mbggenerated
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.userGroup
     *
     * @return the value of user.userGroup
     *
     * @mbggenerated
     */
    public String getUsergroup() {
        return usergroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.userGroup
     *
     * @param usergroup the value for user.userGroup
     *
     * @mbggenerated
     */
    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup == null ? null : usergroup.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.extraInfo
     *
     * @return the value of user.extraInfo
     *
     * @mbggenerated
     */
    public String getExtrainfo() {
        return extrainfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.extraInfo
     *
     * @param extrainfo the value for user.extraInfo
     *
     * @mbggenerated
     */
    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo == null ? null : extrainfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.regTime
     *
     * @return the value of user.regTime
     *
     * @mbggenerated
     */
    public Date getRegtime() {
        return regtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.regTime
     *
     * @param regtime the value for user.regTime
     *
     * @mbggenerated
     */
    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }
}