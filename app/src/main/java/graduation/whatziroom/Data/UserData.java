package graduation.whatziroom.Data;

/**
 * Created by mapl0 on 2017-10-04.
 */

public class UserData {

    private String PKey, Name, ID, PW, Email, Status, Acount, Longitude, Latitude, CreatedDate, UpdatedDate, UDID, Message;

    public UserData(String pkey, String name, String longitude, String latitude) {

        //위치 추적을 위해서 사용, 필요에 따라 생성자로 만들어서 사용하면 될듯, 명시는 해주고.
        setPKey(pkey);
        setName(name);
        setLongitude(longitude);
        setLatitude(latitude);

    }

    public String getPKey() {
        return PKey;
    }

    public void setPKey(String PKey) {
        this.PKey = PKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAcount() {
        return Acount;
    }

    public void setAcount(String acount) {
        Acount = acount;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getUDID() {
        return UDID;
    }

    public void setUDID(String UDID) {
        this.UDID = UDID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
