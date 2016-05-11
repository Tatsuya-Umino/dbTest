package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;

@Entity
public class Message extends Model {

  @Id // アノテーション
  public Long id;
  @Required
  public String name;
  @Email
  public String mail;
  @Pattern(value="^[0-9a-zA-Z]*$", message="半角英数字で入力してください")
  public String message;
  public Date postdate;

  public static Finder<Long, Message> find =
    new Finder<Long, Message>(Long.class, Message.class);

  @Override
  public String toString() {
    return ("[id:" + id + ", name:" + name + ",mail:" + mail +
      ",message:" + message + ",date:" + postdate + "]");
  }

}
