package dto;

public class Member {
    private String user_id, password, name, gender;
    private int age;

    public Member(String user_id, String password, String name, int age, String gender) {
        this.user_id = user_id;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getUserId() { return user_id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }

    public void setUserId(String user_id) { this.user_id = user_id; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
}

