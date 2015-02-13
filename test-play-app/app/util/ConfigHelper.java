package util;


import play.Play;

/**
 * Created by aabramov on 12/6/14.
 */
public class ConfigHelper {

    public String get(String setting) {
        try {
            String ret = Play.application().configuration().getString(setting);
            if(ret == null || ret.length() == 0) throw new IllegalArgumentException();
            return ret;
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Invalid App Configuration. Setting: "+setting,  e);
        }
    }

    public int getInt(String setting) {
        try {
            int ret = Play.application().configuration().getInt(setting);
            return ret;
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Invalid App Configuration. Setting: "+setting, e);
        }
    }
}
