package erecommender;

public class SampleLog {
    int userId;
    long timeStamp;
    int adgroup_id;
    String pid;
    int noclk;


    public SampleLog() {

    }

    public SampleLog(int userId, long timeStamp, int adgroup_id, String pid, int noclk){
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.adgroup_id = adgroup_id;
        this.pid = pid;
        this.noclk = noclk;
    }

    public int getUserId() {
        return userId;
    }

    public int getAdgroup_id(){
        return adgroup_id;
    }

    public String getPid() {
        return pid;
    }

    public int getNoclk() {return noclk; }

    public long getTimeStamp(){
        return timeStamp;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAdgroup_id(int adgroup_id) {
        this.adgroup_id = adgroup_id;
    }

    public void setPid(String pid){ this.pid = pid; }

    public void setNoclk(int noclk) {
        this.noclk = noclk;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toText(){
        return userId+","+timeStamp+","+adgroup_id+","+pid+","+noclk+"\n";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(",");
        sb.append(timeStamp).append(",");
        sb.append(adgroup_id).append(",");
        sb.append(pid).append(",");
        sb.append(noclk);
        return sb.toString();
    }


    public static SampleLog fromString(String line){
        String[] tokens = line.split(",");
        if (tokens.length != 5) {
            throw new RuntimeException("Invalid record: " + line);
        }

        SampleLog mrate = new SampleLog();

        try {
            mrate.userId = Integer.parseInt(tokens[0]);
            mrate.timeStamp= Long.parseLong(tokens[1]);
            mrate.adgroup_id = Integer.parseInt(tokens[2]);
//            mrate.pid = String.parseString(tokens[3]);
            mrate.noclk = Integer.parseInt(tokens[4]);

        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return mrate;
    }
}
