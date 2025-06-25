import javax.swing.*;
import java.awt.*;

import java.util.List;

public class BookManagerGUI {
    private final BookManager manager = new BookManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookManagerGUI().ShowGUI());
    }

    private void ShowGUI() { // 创建主窗口喵

        JFrame frame = new JFrame("喵喵图书管理系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("添加图书", Add());
        tabbedPane.addTab("管理图书", manage());
        tabbedPane.addTab("查询图书", Search());

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel Add() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField idField = new JTextField(15); // 我感觉调这里的值就能改变文本框大小，但是好像没效果，不知道为什么
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField priceField = new JTextField(15);

        panel.add(new JLabel("图书ID:"));
        panel.add(idField);
        panel.add(new JLabel("书名:"));
        panel.add(titleField);
        panel.add(new JLabel("作者:"));
        panel.add(authorField);
        panel.add(new JLabel("价格:"));
        panel.add(priceField);

        JButton addButton = new JButton("添加图书");
        addButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                double price = Double.parseDouble(priceField.getText());

                manager.addBook(new Book(id, title, author, price));
                JOptionPane.showMessageDialog(null, "添加成功喵~", "成功", JOptionPane.INFORMATION_MESSAGE);

                // 清空输入框
                idField.setText("");
                titleField.setText("");
                authorField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "价格必须是数字喵！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(addButton);

        return panel;
    }

    private JPanel manage() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultListModel<Book> listModel = new DefaultListModel<>();
        JList<Book> bookList = new JList<>(listModel);
        bookList.setCellRenderer(new BookListRenderer());

        refreshBookList(listModel);

        JScrollPane scrollPane = new JScrollPane(bookList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JButton deleteButton = new JButton("删除选中");
        deleteButton.addActionListener(e -> {
            Book selected = bookList.getSelectedValue();
            if (selected != null) {
                manager.deleteBook(selected.getId());
                refreshBookList(listModel);
            } else {
                JOptionPane.showMessageDialog(null, "请先选择一本书喵~", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton refreshButton = new JButton("刷新列表");
        refreshButton.addActionListener(e -> refreshBookList(listModel));

        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel Search() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("查询");

        searchPanel.add(new JLabel("输入书名或ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        DefaultListModel<Book> result = new DefaultListModel<>();
        JList<Book> resultList = new JList<>(result);
        resultList.setCellRenderer(new BookListRenderer());
        JScrollPane resultScroll = new JScrollPane(resultList);

        searchButton.addActionListener(e -> { // 这里对两种查询方法做了统合，简化了操作，使之更适配GUI程序
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                result.clear();

                // 先按ID查
                Book book = manager.searchByid(keyword);
                if (book != null) {
                    result.addElement(book);
                }

                // 再按书名查
                List<Book> books = manager.searchByname(keyword);
                for (Book b : books) {
                    if (!result.contains(b)) {
                        result.addElement(b);
                    }
                }

                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "没有找到匹配的图书喵~", "查询结果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultScroll, BorderLayout.CENTER);

        return panel;
    }

    private void refreshBookList(DefaultListModel<Book> model) { // 刷新图书列表
        model.clear();
        manager.DisplayAll().forEach(model::addElement);
    }

    static class BookListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Book) {
                Book book = (Book) value;
                setText(String.format("%s - 《%s》 作者: %s 价格: ¥%.2f",
                        book.getId(), book.getTitle(), book.getAuthor(), book.getPrice()));
            }
            return this;
        }
    }

    /*
     * static class BookManager { // 原来纯后端版本的BookManager，这里直接拿来改造成GUI版本了
     * private Map<String, Book> books = new HashMap<>();
     * private String data = "booklist.txt"; // 数据永久化存储用，但其实在考虑要不要直接上数据库比较好
     * 
     * public BookManager() {
     * loadData();
     * }
     * 
     * public void addBook(Book book) {
     * books.put(book.getId(), book);
     * saveData();
     * }
     * 
     * public void deleteBook(String id) {
     * books.remove(id);
     * saveData();
     * }
     * 
     * public void updateBook(String id, String title, String author, double price)
     * {
     * Book book = books.get(id);
     * book.setAuthor(author);
     * book.setTitle(title);
     * book.setPrice(price);
     * saveData();
     * }
     * 
     * public Book getBookById(String id) {
     * return books.get(id);
     * }
     * 
     * public List<Book> searchByTitle(String keywd) {
     * List<Book> result = new ArrayList<>();
     * for (Book book : books.values()) {
     * if (book.getTitle().contains(keywd)) {
     * result.add(book);
     * }
     * }
     * return result;
     * }
     * 
     * public void displayall() {
     * for (Book book : books.values()) {
     * System.out.println(book);
     * }
     * }
     * 
     * private void saveData() { // 暂时没写
     * 
     * }
     * 
     * private void loadData() {
     * 
     * }
     * 
     * public List<Book> displayAll() {
     * return new ArrayList<>(books.values());
     * }
     * }
     */
}
