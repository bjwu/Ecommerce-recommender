package erecommender.DataModels;

public class JDBehaviorlog {
    private int userId;
    private int itemId;
    private String timeStamp;
    private int orderID;
    private int type;

    public JDBehaviorlog(){}

    public JDBehaviorlog(int userID, int itemID, String timeStamp, int orderID,int type){
        this.userId = userID;
        this.itemId = itemID;
        this.timeStamp = timeStamp;
        this.orderID = orderID;
        this.type = type;
    }


    public int getUserId() {
        return userId;
    }
    public String getStrUserId() { return Integer.toString(userId); }

    public int getItemId() {
        return itemId;
    }
    public String getStrItemId() { return Integer.toString(itemId); }


    public int getType(){
         return type;
    }
    public String getStrType() { return Integer.toString(type); }

    public int getOrderID(){
        return orderID;
    }
    public String getStrOrderID() { return Integer.toString(orderID); }


    public String getTimeStamp(){
        return timeStamp;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toText(){
        return userId+","+timeStamp+","+itemId+","+type+"\n";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(",");
        sb.append(timeStamp).append(",");
        sb.append(itemId).append(",");
        sb.append(type);
        return sb.toString();
    }

    public static JDBehaviorlog fromString(String line){
        String[] tokens = line.split(",");
        if (tokens.length != 5) {
            throw new RuntimeException("Invalid record: " + line);
        }

        JDBehaviorlog stdLog = new JDBehaviorlog();

        try {
            stdLog.userId = Integer.parseInt(tokens[0]);
            stdLog.itemId = Integer.parseInt(tokens[1]);
            stdLog.timeStamp = tokens[2];
            stdLog.orderID = Integer.parseInt(tokens[3]);
            stdLog.type = Integer.parseInt(tokens[4]);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return stdLog;
    }

}
