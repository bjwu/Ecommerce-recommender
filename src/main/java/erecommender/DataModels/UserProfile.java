package erecommender.DataModels;

public class UserProfile {
    private int userId;
    private String age;
    private  String sex;
    private String timeStamp;
    private int level;
    private String city_level;
    private String province;
    private String city;
    private String county;

    public UserProfile(){}

    public UserProfile(int userId,  String age, String sex, String timeStamp, int level,
                       String city_level, String province, String city, String county){

        this.userId = userId;
        this.age = age;
        this.sex = sex;
        this.timeStamp = timeStamp;
        this.level = level;
        this.city_level = city_level;
        this.province = province;
        this.city = city;
        this.county = county;

    }


    public int getUserId() {
        return userId;
    }
    public String getStrUserId() { return Integer.toString(userId); }

    public int getLevel() {
        return level;
    }
    public String getStrLevel() { return Integer.toString(level); }


    public String getAge() { return age; }
    public String getSex() { return sex; }
    public String getTimeStamp(){
        return timeStamp;
    }
    public String getCityLevel() { return city_level; }
    public String getProvince() { return province; }
    public String getcity() { return city; }
    public String getCounty() { return county; }


    public void setUserId(int userId) {
        this.userId = userId;
    }



    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toText(){
        return userId+","+timeStamp+","+level+","+"\n";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(",");
        sb.append(age).append(",");
        sb.append(sex).append(",");
        sb.append(timeStamp).append(",");
        sb.append(level).append(",");
        sb.append(city_level).append(",");
        sb.append(province).append(",");
        sb.append(city).append(",");
        sb.append(county);
        return sb.toString();
    }

    public static UserProfile fromString(String line){
        String[] tokens = line.split(",");
        if (tokens.length != 9) {
            throw new RuntimeException("Invalid record: " + line);
        }

        UserProfile stdLog = new UserProfile();

        try {
            stdLog.userId = Integer.parseInt(tokens[0]);
            stdLog.age = tokens[1];
            stdLog.sex = tokens[2];
            stdLog.timeStamp = tokens[3];
            stdLog.level = Integer.parseInt(tokens[4]);
            stdLog.city_level = tokens[5];
            stdLog.province = tokens[6];
            stdLog.city = tokens[7];
            stdLog.county = tokens[8];

        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return stdLog;
    }
}
