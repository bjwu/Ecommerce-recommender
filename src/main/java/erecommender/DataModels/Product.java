package erecommender.DataModels;

public class Product {
    private int brandId;
    private int itemId;
    private String timeStamp;
    private int shopId;
    private int cate;

    public Product(){}

    public Product(int itemId, int brandId, int shopId,int cate,String timeStamp){
        this.itemId = itemId;
        this.brandId = brandId;
        this.shopId = shopId;
        this.timeStamp = timeStamp;
        this.cate = cate;
    }


    public int getBrandId() {
        return brandId;
    }
    public String getStrBrandId() { return Integer.toString(brandId); }

    public int getItemId() {
        return itemId;
    }
    public String getStrItemId() { return Integer.toString(itemId); }


    public int getCate(){
        return cate;
    }
    public String getStrCate() { return Integer.toString(cate); }

    public int getShopId() {
        return shopId;
    }
    public String getStrShopId() { return Integer.toString(shopId); }


    public String getTimeStamp(){
        return timeStamp;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public void setCate(int cate){
        this.cate = cate;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toText(){
        return timeStamp+","+itemId+","+"\n";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(itemId).append(",");
        sb.append(brandId).append(",");
        sb.append(shopId).append(",");
        sb.append(cate).append(",");
        sb.append(timeStamp);
        return sb.toString();
    }

    public static Product fromString(String line){
        String[] tokens = line.split(",");
        if (tokens.length != 5) {
            throw new RuntimeException("Invalid record: " + line);
        }

        Product stdLog = new Product();

        try {
            stdLog.itemId = Integer.parseInt(tokens[0]);
            stdLog.brandId = Integer.parseInt(tokens[1]);
            stdLog.shopId = Integer.parseInt(tokens[2]);
            stdLog.cate = Integer.parseInt(tokens[3]);
            stdLog.timeStamp = tokens[4];
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return stdLog;
    }
}
