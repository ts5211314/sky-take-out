package generator.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 员工信息
* @TableName employee
*/
public class Employee implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @ApiModelProperty("主键")
    private Long id;
    /**
    * 姓名
    */
    @NotBlank(message="[姓名]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("姓名")
    @Length(max= 32,message="编码长度不能超过32")
    private String name;
    /**
    * 用户名
    */
    @NotBlank(message="[用户名]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("用户名")
    @Length(max= 32,message="编码长度不能超过32")
    private String username;
    /**
    * 密码
    */
    @NotBlank(message="[密码]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("密码")
    @Length(max= 64,message="编码长度不能超过64")
    private String password;
    /**
    * 手机号
    */
    @NotBlank(message="[手机号]不能为空")
    @Size(max= 11,message="编码长度不能超过11")
    @ApiModelProperty("手机号")
    @Length(max= 11,message="编码长度不能超过11")
    private String phone;
    /**
    * 性别
    */
    @NotBlank(message="[性别]不能为空")
    @Size(max= 2,message="编码长度不能超过2")
    @ApiModelProperty("性别")
    @Length(max= 2,message="编码长度不能超过2")
    private String sex;
    /**
    * 身份证号
    */
    @NotBlank(message="[身份证号]不能为空")
    @Size(max= 18,message="编码长度不能超过18")
    @ApiModelProperty("身份证号")
    @Length(max= 18,message="编码长度不能超过18")
    private String idNumber;
    /**
    * 状态 0:禁用，1:启用
    */
    @NotNull(message="[状态 0:禁用，1:启用]不能为空")
    @ApiModelProperty("状态 0:禁用，1:启用")
    private Integer status;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private Long createUser;
    /**
    * 修改人
    */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
    * 主键
    */
    private void setId(Long id){
    this.id = id;
    }

    /**
    * 姓名
    */
    private void setName(String name){
    this.name = name;
    }

    /**
    * 用户名
    */
    private void setUsername(String username){
    this.username = username;
    }

    /**
    * 密码
    */
    private void setPassword(String password){
    this.password = password;
    }

    /**
    * 手机号
    */
    private void setPhone(String phone){
    this.phone = phone;
    }

    /**
    * 性别
    */
    private void setSex(String sex){
    this.sex = sex;
    }

    /**
    * 身份证号
    */
    private void setIdNumber(String idNumber){
    this.idNumber = idNumber;
    }

    /**
    * 状态 0:禁用，1:启用
    */
    private void setStatus(Integer status){
    this.status = status;
    }

    /**
    * 创建时间
    */
    private void setCreateTime(Date createTime){
    this.createTime = createTime;
    }

    /**
    * 更新时间
    */
    private void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }

    /**
    * 创建人
    */
    private void setCreateUser(Long createUser){
    this.createUser = createUser;
    }

    /**
    * 修改人
    */
    private void setUpdateUser(Long updateUser){
    this.updateUser = updateUser;
    }


    /**
    * 主键
    */
    private Long getId(){
    return this.id;
    }

    /**
    * 姓名
    */
    private String getName(){
    return this.name;
    }

    /**
    * 用户名
    */
    private String getUsername(){
    return this.username;
    }

    /**
    * 密码
    */
    private String getPassword(){
    return this.password;
    }

    /**
    * 手机号
    */
    private String getPhone(){
    return this.phone;
    }

    /**
    * 性别
    */
    private String getSex(){
    return this.sex;
    }

    /**
    * 身份证号
    */
    private String getIdNumber(){
    return this.idNumber;
    }

    /**
    * 状态 0:禁用，1:启用
    */
    private Integer getStatus(){
    return this.status;
    }

    /**
    * 创建时间
    */
    private Date getCreateTime(){
    return this.createTime;
    }

    /**
    * 更新时间
    */
    private Date getUpdateTime(){
    return this.updateTime;
    }

    /**
    * 创建人
    */
    private Long getCreateUser(){
    return this.createUser;
    }

    /**
    * 修改人
    */
    private Long getUpdateUser(){
    return this.updateUser;
    }

}
