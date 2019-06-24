package erecommender.Recommendation;

public class RecommendationLog {
    private int userId;
    private int itemId;

    private RecommendationLog() {

    }

    public RecommendationLog(int userId,  int itemId){
        this.userId = userId;
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStrUserId() { return Integer.toString(userId); }


    public int getItemId() {return itemId; }

    public String getStrItemId() { return Integer.toString(itemId); }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(",");
        sb.append(itemId);
        return sb.toString();
    }


    public static RecommendationLog fromString(String line){
        String[] tokens = line.split(",");
//        if (tokens.length != 5) {
//            throw new RuntimeException("Invalid record: " + line);
//        }

        RecommendationLog rmlog = new RecommendationLog();

        try {
            rmlog.userId = Integer.parseInt(tokens[0]);
            rmlog.itemId = Integer.parseInt(tokens[1]);

        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return rmlog;
    }


}
