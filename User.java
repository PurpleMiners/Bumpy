package clover.hamar_bumpy;


public class User {
    public String fname,lname,uemail,pwd,mno;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String fname,String lname,String uemail,String pwd,String mno)
    {
        this.fname=fname;
        this.lname=lname;
        this.mno=mno;
        this.uemail=uemail;
        this.pwd=pwd;
    }

}
