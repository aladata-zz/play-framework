package security;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import util.EncryptHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


/*
 * Manages token wrapping/unwrapping
 * Token Format:
 * token  = [ base64 [AES [hash [ subtoken], subtoken], AES-IV ]]
 * subtoken = [token guid, userid, auth date/time, incoming IP, shard id]
 */
public class Token {

    public String tokenId;
    public String userid;
    public String createdDateTime;
    public String userIP;
    public String shardId; //Vendor shard
    public String hash;

    private String key_;


    public Token(String token, int expirePeriod, String key){
        this.key_ = key;
        this.parse(token, expirePeriod);

    }


    public Token(String userid, String userIP, String shardId, String key) {
        this.tokenId = java.util.UUID.randomUUID().toString();;
        this.userid = userid;
        this.createdDateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        this.userIP = userIP;
        this.shardId = shardId;
        this.key_ = key;
    }


    private void parse(String token, int expirePeriod) {

        try {
            String decrypted = EncryptHelper.decrypt(token);
            JsonNode node = Json.parse(decrypted);

            this.tokenId = node.findPath("tokenId").textValue();
            this.userid = node.findPath("userid").textValue();
            this.createdDateTime = node.findPath("createdDateTime").textValue();
            this.userIP = node.findPath("userIP").textValue();
            this.shardId = node.findPath("shardId").textValue(); //Vendor shard
            this.hash = node.findPath("hash").textValue();

            //validate hash
            if(!this.hash.equals(this.hash())) throw new IllegalArgumentException("Token hash check failed");

            //verify token not expired
            Date createdDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(this.createdDateTime);
            long diff = (new Date()).getTime() - createdDate.getTime();
            long diffHours = diff / (60 * 60 * 1000);

            if(diffHours > expirePeriod) throw new IllegalArgumentException("Access token expired");


        }
        catch(IllegalArgumentException e) {
            throw e;
        }
        catch(Exception e){
            throw new IllegalArgumentException("Invalid Access token", e);
        }


    }

    @Override
    public String toString() {
        this.hash = this.hash();
        String subtoken = Json.toJson(this).toString();
        String encrypted = EncryptHelper.encrypt(subtoken);
        return encrypted;
    }


    private String hash()
    {
        String toEncrypt = this.tokenId+this.userid+this.createdDateTime+this.userIP+this.shardId;
        return EncryptHelper.hash(toEncrypt, this.key_);
    }
}
