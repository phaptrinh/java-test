public enum Product {
    COKE("Coke", 10_000), PEPSI("Pepsi", 10_000), SODA("Soda", 20_000);
   
    private String name;
    private int price;
   
    private Product(String name, int price){
        this.name = name;
        this.price = price;
    }
   
    public String getName(){
        return name;
    }
   
    public int getPrice(){
        return price;
    }
}
