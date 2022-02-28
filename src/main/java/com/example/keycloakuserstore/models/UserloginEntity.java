package com.example.keycloakuserstore.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "USERLOGIN")
public class UserloginEntity {
    @Basic
    @Id
    @Column(name = "USERNAME")
    private String username;
    @Basic
    @Column(name = "HANDPHONE")
    private String handphone;
    @Basic
    @Column(name = "LOGINPWD")
    private String loginpwd;
    @Basic
    @Column(name = "TRADINGPWD")
    private String tradingpwd;
    @Basic
    @Column(name = "AUTHTYPE")
    private String authtype;
    @Basic
    @Column(name = "STATUS")
    private String status;
    @Basic
    @Column(name = "LOGINSTATUS")
    private String loginstatus;
    @Basic
    @Column(name = "LASTCHANGED")
    private Date lastchanged;
    @Basic
    @Column(name = "NUMBEROFDAY")
    private Short numberofday;
    @Basic
    @Column(name = "LASTLOGIN")
    private Date lastlogin;
    @Basic
    @Column(name = "ISRESET")
    private String isreset;
    @Basic
    @Column(name = "ISMASTER")
    private String ismaster;
    @Basic
    @Column(name = "TOKENID")
    private String tokenid;
    @Basic
    @Column(name = "OTPPWD")
    private String otppwd;
    @Basic
    @Column(name = "EXPSTATUS")
    private String expstatus;
    @Basic
    @Column(name = "LOGINDATETIME")
    private Date logindatetime;
    @Basic
    @Column(name = "EXPDATETIME")
    private Date expdatetime;
    @Basic
    @Column(name = "ISWARN")
    private String iswarn;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public void setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
    }

    public String getTradingpwd() {
        return tradingpwd;
    }

    public void setTradingpwd(String tradingpwd) {
        this.tradingpwd = tradingpwd;
    }

    public String getAuthtype() {
        return authtype;
    }

    public void setAuthtype(String authtype) {
        this.authtype = authtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginstatus() {
        return loginstatus;
    }

    public void setLoginstatus(String loginstatus) {
        this.loginstatus = loginstatus;
    }

    public Date getLastchanged() {
        return lastchanged;
    }

    public void setLastchanged(Date lastchanged) {
        this.lastchanged = lastchanged;
    }

    public Short getNumberofday() {
        return numberofday;
    }

    public void setNumberofday(Short numberofday) {
        this.numberofday = numberofday;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getIsreset() {
        return isreset;
    }

    public void setIsreset(String isreset) {
        this.isreset = isreset;
    }

    public String getIsmaster() {
        return ismaster;
    }

    public void setIsmaster(String ismaster) {
        this.ismaster = ismaster;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getOtppwd() {
        return otppwd;
    }

    public void setOtppwd(String otppwd) {
        this.otppwd = otppwd;
    }

    public String getExpstatus() {
        return expstatus;
    }

    public void setExpstatus(String expstatus) {
        this.expstatus = expstatus;
    }

    public Date getLogindatetime() {
        return logindatetime;
    }

    public void setLogindatetime(Date logindatetime) {
        this.logindatetime = logindatetime;
    }

    public Date getExpdatetime() {
        return expdatetime;
    }

    public void setExpdatetime(Date expdatetime) {
        this.expdatetime = expdatetime;
    }

    public String getIswarn() {
        return iswarn;
    }

    public void setIswarn(String iswarn) {
        this.iswarn = iswarn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserloginEntity that = (UserloginEntity) o;
        return Objects.equals(username, that.username) && Objects.equals(handphone, that.handphone) && Objects.equals(loginpwd, that.loginpwd) && Objects.equals(tradingpwd, that.tradingpwd) && Objects.equals(authtype, that.authtype) && Objects.equals(status, that.status) && Objects.equals(loginstatus, that.loginstatus) && Objects.equals(lastchanged, that.lastchanged) && Objects.equals(numberofday, that.numberofday) && Objects.equals(lastlogin, that.lastlogin) && Objects.equals(isreset, that.isreset) && Objects.equals(ismaster, that.ismaster) && Objects.equals(tokenid, that.tokenid) && Objects.equals(otppwd, that.otppwd) && Objects.equals(expstatus, that.expstatus) && Objects.equals(logindatetime, that.logindatetime) && Objects.equals(expdatetime, that.expdatetime) && Objects.equals(iswarn, that.iswarn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, handphone, loginpwd, tradingpwd, authtype, status, loginstatus, lastchanged, numberofday, lastlogin, isreset, ismaster, tokenid, otppwd, expstatus, logindatetime, expdatetime, iswarn);
    }
}
