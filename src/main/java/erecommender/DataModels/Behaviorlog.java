package erecommender.DataModels;

public class Behaviorlog {
    private int userId;
    private long timeStamp;
    private String btag;
    private int cate;
    private int itemId;

    private Behaviorlog() {

    }

    public Behaviorlog(int userId, long timeStamp, String btag, int cate, int itemId){
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.btag = btag;
        this.cate = cate;
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStrUserId() { return Integer.toString(userId); }

    public String getbtag(){
        return btag;
    }

    public int getcate() {
        return cate;
    }

    public int getItemId() {return itemId; }

    public String getStrItemId() { return Integer.toString(itemId); }

    public long getTimeStamp(){
        return timeStamp;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setbtag(String btag) {
        this.btag = btag;
    }

    public void setcate(int cate){ this.cate = cate; }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toText(){
        return userId+","+timeStamp+","+btag+","+cate+","+itemId+"\n";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(",");
        sb.append(timeStamp).append(",");
        sb.append(btag).append(",");
        sb.append(cate).append(",");
        sb.append(itemId);
        return sb.toString();
    }


    public static Behaviorlog fromString(String line){
        String[] tokens = line.split(",");
        if (tokens.length != 5) {
            throw new RuntimeException("Invalid record: " + line);
        }

        Behaviorlog mrate = new Behaviorlog();

        try {
            mrate.userId = Integer.parseInt(tokens[0]);
            mrate.itemId = Integer.parseInt(tokens[1]);
            mrate.cate = Integer.parseInt(tokens[2]);
            mrate.btag = tokens[3];
            mrate.timeStamp= Long.parseLong(tokens[4]);



//
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return mrate;
    }
}
