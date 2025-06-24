public class Book {
    private String id; // 图书编号
    private String title; // 书名
    private String author; // 作者
    private double price; // 价格

    public Book(String id, String title, String author, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override // 通过重写字符串手动排版，但是好像只有最初的非GUI版本用到它了。。
    public String toString() {
        return String.format("ID: %s | 书名: %-10s | 作者: %-5s | 价格: %.2f",
                id, title, author, price);
    }
}