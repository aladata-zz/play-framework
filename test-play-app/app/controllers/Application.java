package controllers;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.PostgresPlatform;
import play.Logger;
import play.libs.Json;
import play.mvc.*;

import usersdb.models.Users;
import views.html.*;

import java.util.ArrayList;
import plank.models.*;

public class Application extends Controller {


    //todo: remove JUNK
    public static Result index() {
        Logger.debug("hello world");

        return ok(index.render("Plank's new application is ready."));
    }

    public static Result iserror(String isError) {
        if(isError.startsWith("true"))
            throw new RuntimeException("my personal error");
        return ok(index.render("Plank's new application is ready."));
    }

    public static Result testdb() {
        ServerConfig config = new ServerConfig();
        config.setName("pgtest");

// Define DataSource parameters
        DataSourceConfig postgresDb = new DataSourceConfig();
        postgresDb.setDriver("org.postgresql.Driver");
        postgresDb.setUsername("uplank");
        postgresDb.setPassword("pplank");
        postgresDb.setUrl("jdbc:postgresql://localhost/plank-usersdb");
        postgresDb.setHeartbeatSql("select 1");

        config.setDataSourceConfig(postgresDb);

// set DDL options...
        config.setDdlGenerate(true);
        config.setDdlRun(true);

        config.setDefaultServer(false);
        config.setRegister(false);


// automatically determine the DatabasePlatform
// using the jdbc driver
        config.setDatabasePlatform(new PostgresPlatform());

// specify the entity classes (and listeners etc)
// ... if these are not specified Ebean will search
// ... the classpath looking for entity classes.
        config.addClass(Users.class);

// specify jars to search for entity beans
//config.addJar("someJarThatContainsEntityBeans.jar");

// create the EbeanServer instance
        EbeanServer server = EbeanServerFactory.create(config);

        String sql = "select 1 as count";
        SqlRow row = server.createSqlQuery(sql).findUnique();

        Integer i = row.getInteger("count");

        System.out.println("Got "+i+"  - DataSource good.");



        Users e = new Users();
        e.email = "aladata@gmail.com";
        e.user_id = "123";

        // will insert
        server.save(e);

        e.email = "aladata2@gmail.com";

        // this will update
        server.save(e);

        // find the inserted entity by its id
        Users e2 = server.find(Users.class, e.user_id);
        System.out.println("Got "+e2.email);

        server.delete(e);
        // can use delete by id when you don't have the bean
        //Ebean.delete(ESimple.class, e.getId());

        return ok("done db test");
    }

}
