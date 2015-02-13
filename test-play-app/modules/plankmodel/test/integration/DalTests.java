package integration;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.config.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.*;
import org.junit.Test;
import play.db.DB;
import play.libs.Json;
import play.mvc.Result;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import usersdb.models.Users;


/**
 * Created by aabramov on 12/13/14.
 */
public class DalTests {


    @Test
    public void jdbctest() throws SQLException {
        //todo: connect to user db, get user & shard, connect to vendorsdb and pull out ebean object

        Map<String, Object> additionalConfiguration = new HashMap<String, Object>();
        additionalConfiguration.put("db.plankdb.driver", "org.postgresql.Driver");
        additionalConfiguration.put("db.plankdb.url", "jdbc:postgresql://localhost/plank");
        additionalConfiguration.put("db.plankdb.user", "uplank");
        additionalConfiguration.put("db.plankdb.password", "pplank");


        running(fakeApplication(additionalConfiguration), new Runnable() {
            public void run() {
                ResultSet rs = null;
                Statement stmt = null;
                Connection conn = null;
                try {
                    //check if we can get to postgres
                    conn = DB.getConnection("plankdb");
                    stmt = conn.createStatement();
                    String sql;
                    sql = "select * from vendors limit 10;";
                    rs = stmt.executeQuery(sql);

                    //STEP 5: Extract data from result set
                    while (rs.next()) {
                        //Retrieve by column name
                        String id = rs.getString("vendor_id");
                        String company_name = rs.getString("company_name");

                        //Display values
                        System.out.print("ID: " + id);
                        System.out.print(", company_name: " + company_name);
                    }
                }
                catch(SQLException e){
                    System.out.println("Exception: "+e.getMessage());
                    System.out.println("Exception: "+e.toString());

                }
                finally {
                    try { //STEP 6: Clean-up environment
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        System.out.println("Exception: " + e.getMessage());
                        System.out.println("Exception: " + e.toString());

                    }
                }

            }
        });



    }

    @Test
    public void testEbean() {


        Map<String, Object> additionalConfiguration = new HashMap<String, Object>();



        running(fakeApplication(additionalConfiguration), new Runnable() {
            public void run() {
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
// config.setDatabasePlatform(new PostgresPlatform());

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

            }
        });



    }


}


