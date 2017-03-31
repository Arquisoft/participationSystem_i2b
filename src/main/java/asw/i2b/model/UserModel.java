package asw.i2b.model;

/**
 * Created by MIGUEL on 31/03/2017.
 */
public class UserModel {

    private String login;
    private boolean admin = false;

    public UserModel(String login){
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAdmin(boolean admin) {
        admin = admin;
    }


    public boolean isAdmin(){
        return admin;
    }
}
