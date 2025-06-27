import java.time.LocalDate;

class Medicine {
    private int id;
    private String name;
    private String company;
    private LocalDate expiryDate;
    private double price;
    private int quantity;
    private String category;

    public Medicine(int id, String name, String company, double price, int quantity, LocalDate expiryDate, String category) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    // Getters
    public int getId()
    {

        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getCompany()
    {
        return company;
    }
    public double getPrice()
    {
        return price;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public LocalDate getExpiryDate()
    {
        return expiryDate;
    }
    public String getCategory()
    {
        return category;
    }

    // Setters
    public void setId(int id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setCompany(String company)
    {
        this.company = company;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
}