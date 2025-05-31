package ui;

import dao.BookDAO;

import javax.swing.*;
import java.awt.*;

public class BookInsertDialog extends JDialog {
    private JTextField genreIdField, authorIdField, titleField, publisherField;

    public BookInsertDialog() {
        setTitle("도서 추가");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("장르 ID:"));
        genreIdField = new JTextField();
        panel.add(genreIdField);

        panel.add(new JLabel("저자 ID:"));
        authorIdField = new JTextField();
        panel.add(authorIdField);

        panel.add(new JLabel("도서명:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("출판사:"));
        publisherField = new JTextField();
        panel.add(publisherField);

        JButton submitBtn = new JButton("추가");
        submitBtn.addActionListener(e -> insertBook());
        panel.add(submitBtn);

        add(panel);
    }

    private void insertBook() {
        int genreId = Integer.parseInt(genreIdField.getText());
        int authorId = Integer.parseInt(authorIdField.getText());
        String title = titleField.getText().trim();
        String publisher = publisherField.getText().trim();

        boolean result = new BookDAO().insertBook(genreId, authorId, title, publisher);
        if (result) {
            JOptionPane.showMessageDialog(this, "도서가 추가되었습니다!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "도서 추가 실패.");
        }
    }
}
