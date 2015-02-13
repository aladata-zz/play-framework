package usersdb.models;

/**
 * Created by aabramov on 12/11/14.
 */

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Users extends Model {

    @Id
    @Constraints.Min(10)
    public String user_id;

    @Constraints.Required
    public String email;

    public String temp;

   /* @Constraints.Required
    public String password;

    @Constraints.Required
    public String shard_id;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy/MM/dd HH:mm:ss")
    public Date created = new Date();

    @Constraints.Required
    public String createdBy;

    @Formats.DateTime(pattern = "yyyy/MM/dd HH:mm:ss")
    public Date lastModified= new Date();

    public String lastModifiedBy;*/


}
